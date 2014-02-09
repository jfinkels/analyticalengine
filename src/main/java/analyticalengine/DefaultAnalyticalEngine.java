/**
 * DefaultAnalyticalEngine.java - basic implementation of analytical engine
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 * 
 * This file is part of analyticalengine.
 * 
 * analyticalengine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * analyticalengine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * analyticalengine. If not, see <http://www.gnu.org/licenses/>.
 */
package analyticalengine;

import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.cards.Card;

/**
 * A basic implementation of the Analytical Engine interface.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class DefaultAnalyticalEngine implements AnalyticalEngine {

    /** The attendant that operates the Analytical Engine. */
    private Attendant attendant = null;
    /** The mill that performs the arithmetic logic for the Engine. */
    private Mill mill = null;
    /** The memory for the Engine. */
    private Store store = null;
    /** The device that maintains the sequence of cards being read. */
    private CardReader cardReader = null;
    /** The device that prints numbers as output. */
    private Printer printer = null;
    /** The device that plots curves as output. */
    private CurvePrinter curvePrinter = null;

    /**
     * The logger for this class.
     */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(DefaultAnalyticalEngine.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.mill.reset();
        this.store.reset();
        this.curvePrinter.reset();
    }

    /**
     * Performs the instruction specified by the given card.
     * 
     * @param card
     *            The card containing the instruction for the Engine.
     * @throws Bell
     *             if the card indicates that a bell on the Analytical Engine
     *             should be rung.
     * @throws Halt
     *             if the card indicates that the Engine should halt execution
     *             immediately.
     * @throws BadCard
     *             If the specified card has invalid syntax.
     */
    private void executeCard(final Card card) throws Bell, Halt, BadCard {
        // These variables show up in more than one case, but we cannot define
        // them within each case because each case has the same (switch-level)
        // scope, even though it would be more readable that way.
        int numCards;
        long address;
        BigInteger value;

        switch (card.type()) {
        case ADD:
            LOG.debug("Setting operation on mill: addition.");
            this.mill.setOperation(Operation.ADD);
            break;
        case ANNOTATE:
            String message = card.argument(0);
            LOG.debug("Annotating in final report: {}", message);
            this.attendant.annotate(message);
            break;
        case BACKWARD:
            numCards = Integer.parseInt(card.argument(0));
            LOG.debug("Reversing by {} cards", numCards);
            this.cardReader.reverse(numCards);
            break;
        case BELL:
            throw new Bell("Card indicated bell", card);
        case CBACKWARD:
            if (this.mill.hasRunUp()) {
                numCards = Integer.parseInt(card.argument(0));
                LOG.debug("Reversing (due to run-up) by {} cards", numCards);
                this.cardReader.reverse(numCards);
            } else {
                LOG.debug("Skipped reversing due to no run-up");
            }
            break;
        case CFORWARD:
            if (this.mill.hasRunUp()) {
                numCards = Integer.parseInt(card.argument(0));
                LOG.debug("Advancing (due to run-up) by {} cards", numCards);
                this.cardReader.advance(numCards);
            } else {
                LOG.debug("Skipped advancing due to no run-up");
            }
            break;
        case COMMENT:
            LOG.debug("Comment: " + card.argument(0));
            break;
        case DIVIDE:
            LOG.debug("Setting operation on mill: division.");
            this.mill.setOperation(Operation.DIVIDE);
            break;
        case SETX:
            value = this.mill.mostRecentValue();
            LOG.debug("Setting the x value of the curve printer: {}", value);
            this.curvePrinter.setX(value);
            break;
        case SETY:
            value = this.mill.mostRecentValue();
            LOG.debug("Setting the y value of the curve printer: {}", value);
            this.curvePrinter.setY(value);
            break;
        case DRAW:
            LOG.debug("Drawing on the curve printer");
            this.curvePrinter.draw();
            break;
        case FORWARD:
            numCards = Integer.parseInt(card.argument(0));
            LOG.debug("Advancing card reader by {} cards", numCards);
            this.cardReader.advance(numCards);
            break;
        case HALT:
            LOG.debug("Received halt card.");
            throw new Halt("Card indicated halt", card);
        case LOAD:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                LOG.debug("Loading from {} into ingress axis: {}", address,
                        value);
                this.mill.transferIn(value);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse load address", card);
            }
            break;
        case LOADPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                LOG.debug("Loading from {} into prime axis: {}", address,
                        value);
                this.mill.transferIn(value, true);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse load address", card);
            }
            break;
        case LSHIFTN:
            try {
                int shift = Integer.parseInt(card.argument(0));
                LOG.debug("Performing left shift on mill by {}", shift);
                this.mill.leftShift(shift);
            } catch (NumberFormatException e) {
                throw new BadCard("Failed to parse step up value", card);
            } catch (IllegalArgumentException e) {
                throw new BadCard("Shift value is out of bounds", card);
            }
            break;
        case MOVE:
            LOG.debug("Moving curve printer's stylus.");
            this.curvePrinter.move();
            break;
        case MULTIPLY:
            LOG.debug("Setting operation on mill: multiplication.");
            this.mill.setOperation(Operation.MULTIPLY);
            break;
        case NUMBER:
            address = Long.parseLong(card.argument(0));
            if (address < 0 || address >= this.store.maxAddress()) {
                throw new BadCard("Address out of bounds: " + address, card);
            }
            value = new BigInteger(card.argument(1));
            if (value.compareTo(this.mill.maxValue()) > 0) {
                throw new BadCard("Value too large to store: " + value, card);
            }
            LOG.debug("Loading number {} into address {}", value, address);
            this.store.put(address, value);
            break;
        case PRINT:
            value = this.mill.mostRecentValue();
            if (value == null) {
                LOG.error("No value is available for printing. Not printing.");
                break;
            }
            String printed = this.printer.print(value);
            LOG.debug("Attendant receives value from the printer: {}", printed);
            this.attendant.receiveOutput(printed);
            break;
        case RSHIFTN:
            try {
                int shift = Integer.parseInt(card.argument(0));
                LOG.debug("Performing right shift on mill by {}", shift);
                this.mill.rightShift(shift);
            } catch (NumberFormatException e) {
                throw new BadCard("Failed to parse step up value", card);
            } catch (IllegalArgumentException e) {
                throw new BadCard("Shift value is out of bounds", card);
            }
            break;
        case STORE:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.mill.transferOut();
                this.store.put(address, value);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse store address", card);
            }
            break;
        case STOREPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.mill.transferOut(true);
                this.store.put(address, value);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse store address", card);
            }
            break;
        case SUBTRACT:
            LOG.debug("Setting operation on mill: subtraction.");
            this.mill.setOperation(Operation.SUBTRACT);
            break;
        case TRACEON:
            // TODO this should just change the logging level or something
            break;
        case TRACEOFF:
            // TODO this should just change the logging level or something
            break;
        case ZLOAD:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                this.store.put(address, BigInteger.ZERO);
                this.mill.transferIn(value);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse zload address", card);
            }
            break;
        case ZLOADPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                this.store.put(address, BigInteger.ZERO);
                this.mill.transferIn(value, true);
            } catch (NumberFormatException exception) {
                throw new BadCard("Failed to parse zload address", card);
            }
            break;
        case NEWLINE:
            LOG.debug("Writing a new line in the final report.");
            this.attendant.writeNewline();
            break;
        case WRITECOLUMNS:
            LOG.debug("Writing in rows.");
            this.attendant.writeInDirection(WriteDirection.COLUMNS);
            break;
        case WRITEROWS:
            LOG.debug("Writing in columns.");
            this.attendant.writeInDirection(WriteDirection.ROWS);
            break;
        case WRITEPICTURE:
            String format = card.argument(0);
            LOG.debug("Setting format string for numbers in final report: {}",
                    format);
            this.attendant.setFormat(format);
            break;
        case ALTERNATION:
        case BACKEND:
        case BACKSTART:
        case DECIMALEXPAND:
        case CBACKSTART:
        case CFORWARDSTART:
        case FORWARDEND:
        case FORWARDSTART:
        case INCLUDE:
        case INCLUDELIB:
        case LSHIFT:
        case RSHIFT:
        case WRITEDECIMAL:
            // these should all be removed by attendant before loading cards
            throw new BadCard("Attendant failed to remove card", card);
        default:
            break;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @throws BadCard
     *             {@inheritDoc}
     */
    @Override
    public void run() throws BadCard {
        try {
            while (true) {
                Card currentCard = this.cardReader.readAndAdvance();
                try {
                    this.executeCard(currentCard);
                } catch (Bell bell) {
                    LOG.info("Bell!");
                }
            }
        } catch (BadCard e) {
            // LOG.error("Program error", e);
            throw e;
        } catch (Halt e) {
            // This would print the stack trace for the Halt exception.
            // LOG.info("Program halted.", e);
            LOG.info("Program halted.");
        } catch (IndexOutOfBoundsException e) {
            LOG.error(
                    "Program indicated advance or reverse beyond boundary of card chain.",
                    e);
            throw e;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @param attendant
     *            {@inheritDoc}
     */
    @Override
    public void setAttendant(final Attendant attendant) {
        this.attendant = attendant;
    }

    /**
     * {@inheritDoc}
     * 
     * @param mill
     *            {@inheritDoc}
     */
    @Override
    public void setMill(final Mill mill) {
        this.mill = mill;
    }

    /**
     * {@inheritDoc}
     * 
     * @param store
     *            {@inheritDoc}
     */
    @Override
    public void setStore(final Store store) {
        this.store = store;
    }

    /**
     * {@inheritDoc}
     * 
     * @param reader
     *            {@inheritDoc}
     */
    @Override
    public void setCardReader(final CardReader reader) {
        this.cardReader = reader;
    }

    /**
     * {@inheritDoc}
     * 
     * @param printer
     *            {@inheritDoc}
     */
    @Override
    public void setPrinter(final Printer printer) {
        this.printer = printer;
    }

    /**
     * {@inheritDoc}
     * 
     * @param printer
     *            {@inheritDoc}
     */
    @Override
    public void setCurvePrinter(final CurvePrinter printer) {
        this.curvePrinter = printer;
    }
}
