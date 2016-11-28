/**
 * DefaultAnalyticalEngine.java - basic implementation of analytical engine
 * 
 * Copyright 2014-2016 Jeffrey Finkelstein.
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
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.attendant.Attendant;
import analyticalengine.attendant.WriteDirection;
import analyticalengine.cards.BadCard;
import analyticalengine.cards.Bell;
import analyticalengine.cards.Card;
import analyticalengine.cards.Halt;
import analyticalengine.components.CardReader;
import analyticalengine.components.CurvePrinter;
import analyticalengine.components.Mill;
import analyticalengine.components.Operation;
import analyticalengine.components.Printer;
import analyticalengine.components.Store;

/**
 * A basic implementation of the Analytical Engine interface.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultAnalyticalEngine implements AnalyticalEngine {

    /**
     * The logger for this class.
     */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(DefaultAnalyticalEngine.class);

    /** The attendant that operates the Analytical Engine. */
    private Attendant attendant = null;

    /** The device that maintains the sequence of cards being read. */
    private CardReader cardReader = null;

    /** The device that plots curves as output. */
    private CurvePrinter curvePrinter = null;

    /** The mill that performs the arithmetic logic for the Engine. */
    private Mill mill = null;

    /** The device that prints numbers as output. */
    private Printer printer = null;

    /** The memory for the Engine. */
    private Store store = null;

    /**
     * Performs the advance or reverse specified by the given card.
     * 
     * This includes conditional and unconditional advances and reverses.
     * 
     * @param card
     *            A combinatorial card.
     * @throws IllegalArgumentException
     *             if the card is not a combinatorial card.
     */
    private void applyCombinatorialCard(final Card card) {
        int numCards = Integer.parseInt(card.argument(0));
        boolean hasRunUp = this.mill.hasRunUp();
        switch (card.type()) {
        case CBACKWARD:
            if (hasRunUp) {
                LOG.debug("Reversing (due to run-up) by {} cards", numCards);
                this.cardReader.reverse(numCards);
            } else {
                LOG.debug("Skipped reversing due to no run-up");
            }
            break;
        case CFORWARD:
            if (hasRunUp) {
                LOG.debug("Advancing (due to run-up) by {} cards", numCards);
                this.cardReader.advance(numCards);
            } else {
                LOG.debug("Skipped advancing due to no run-up");
            }
            break;
        case FORWARD:
            LOG.debug("Advancing card reader by {} cards", numCards);
            this.cardReader.advance(numCards);
            break;
        case BACKWARD:
            LOG.debug("Reversing by {} cards", numCards);
            this.cardReader.reverse(numCards);
            break;
        default:
            throw new IllegalArgumentException(
                    "Expected combinatorial card, not " + card);
        }
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
        switch (card.type()) {
        // Instructions affecting the mill: arithmetic and shift instructions
        case ADD:
        case DIVIDE:
        case MULTIPLY:
        case SUBTRACT:
            this.setOperation(card);
            break;
        case RSHIFTN:
        case LSHIFTN:
            this.setShift(card);
            break;
        // Control flow instructions: advance and reverse instructions
        case CBACKWARD:
        case CFORWARD:
        case BACKWARD:
        case FORWARD:
            this.applyCombinatorialCard(card);
            break;
        // Memory access instructions: store, load, and zero-load
        case LOAD:
        case LOADPRIME:
        case NUMBER:
        case STORE:
        case STOREPRIME:
        case ZLOAD:
        case ZLOADPRIME:
            this.handleMemoryAccess(card);
            break;
        // Curve printer instructions
        case DRAW:
        case MOVE:
        case SETX:
        case SETY:
            this.handleCurveDrawing(card);
            break;
        // Action instructions: halt, ring bell, or print
        case BELL:
        case HALT:
        case PRINT:
            this.handleActionCard(card);
            break;
        // Attendant action instructions
        case ANNOTATE:
        case NEWLINE:
        case WRITECOLUMNS:
        case WRITEROWS:
        case WRITEPICTURE:
            this.handleAttendantAction(card);
            break;
        // Debugging instructions
        case COMMENT:
        case TRACEON:
        case TRACEOFF:
            this.handleDebuggingCard(card);
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
     * Performs the Analytical Engine action specified by the given card.
     * 
     * This includes ringing the bell to attract the attention of the
     * attendant, halting the machine, and sending a value from the mill to the
     * printer.
     * 
     * @param card
     *            The action to perform.
     * @throws Bell
     *             if the card indicates the bell should be rung.
     * @throws Halt
     *             if the card indicates the machine should halt.
     * @throws IllegalArgumentException
     *             if the card is not an action card.
     */
    private void handleActionCard(final Card card) throws Bell, Halt {
        switch (card.type()) {
        case BELL:
            throw new Bell("Card indicated bell", card);
        case HALT:
            LOG.debug("Received halt card.");
            throw new Halt("Card indicated halt", card);
        case PRINT:
            Optional<BigInteger> value = this.mill.mostRecentValue();
            if (!value.isPresent()) {
                LOG.error("No value is available for printing. Not printing.");
                break;
            }
            String printed = this.printer.print(value.get());
            LOG.debug("Attendant received value from printer: {}", printed);
            this.attendant.receiveOutput(printed);
            break;
        default:
            throw new IllegalArgumentException("Expected action card, not "
                    + card);
        }
    }

    /**
     * Instructs the attendant to perform the action specified by the card.
     * 
     * This includes annotating and formatting numbers printed to the final
     * report.
     * 
     * @param card
     *            An attendant action card.
     * @throws IllegalArgumentException
     *             if the card is not an attendant action card.
     */
    private void handleAttendantAction(final Card card) {
        switch (card.type()) {
        case ANNOTATE:
            String message = card.argument(0);
            LOG.debug("Annotating in final report: {}", message);
            this.attendant.annotate(message);
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
        default:
            throw new IllegalArgumentException(
                    "Expected attendant action card, not " + card);
        }

    }

    /**
     * Performs the specified action on the curve drawing device.
     * 
     * This includes setting the coordinates of the stylus and drawing with it.
     * 
     * @param card
     *            A curve drawing card.
     * @throws IllegalArgumentException
     *             if the card is not a curve drawing card.
     */
    private void handleCurveDrawing(final Card card) {
        Optional<BigInteger> value;
        switch (card.type()) {
        case DRAW:
            LOG.debug("Drawing on the curve printer");
            this.curvePrinter.draw();
            break;
        case MOVE:
            LOG.debug("Moving curve printer's stylus.");
            this.curvePrinter.move();
            break;
        case SETX:
            value = this.mill.mostRecentValue();
            if (!value.isPresent()) {
                LOG.error("No value is available for setting the x value of"
                          + " the curve printer; not setting a value.");
                break;
            }
            LOG.debug("Setting the x value of the curve printer: {}", value);
            this.curvePrinter.setX(value.get());
            break;
        case SETY:
            value = this.mill.mostRecentValue();
            if (!value.isPresent()) {
                LOG.error("No value is available for setting the y value of"
                          + " the curve printer; not setting a value.");
                break;
            }
            LOG.debug("Setting the y value of the curve printer: {}", value);
            this.curvePrinter.setY(value.get());
            break;
        default:
            throw new IllegalArgumentException(
                    "Expected curve drawing card, not " + card);
        }
    }

    /**
     * Handles Analytical Engine debugging cards, including comment and trace
     * cards.
     * 
     * @param card
     *            A debugging card.
     * @throws IllegalArgumentException
     *             if the specified card is not a debugging card.
     */
    private void handleDebuggingCard(final Card card) {
        switch (card.type()) {
        case COMMENT:
            LOG.debug("Comment: " + card.argument(0));
            break;
        case TRACEON:
            // TODO this should just change the logging level or something
            break;
        case TRACEOFF:
            // TODO this should just change the logging level or something
            break;
        default:
            throw new IllegalArgumentException("Expected debugging card, not "
                    + card);
        }

    }

    /**
     * Performs the memory access specified by the given card.
     * 
     * This includes reading from and writing to memory.
     * 
     * @param card
     *            A memory access card.
     * @throws BadCard
     *             if the card has a syntax error or induces a runtime error in
     *             the Analytical Engine.
     * @throws IllegalArgumentException
     *             if the card is not a memory access card.
     */
    private void handleMemoryAccess(final Card card) throws BadCard {
        long address;
        try {
            address = Long.parseLong(card.argument(0));
        } catch (NumberFormatException e) {
            throw new BadCard("Illegal number format", card, e);
        }
        BigInteger value;
        switch (card.type()) {
        case LOAD:
            value = this.store.get(address);
            LOG.debug("Loading from {} into ingress axis: {}", address, value);
            this.mill.transferIn(value);
            break;
        case LOADPRIME:
            value = this.store.get(address);
            LOG.debug("Loading from {} into prime axis: {}", address, value);
            this.mill.transferIn(value, true);
            break;
        case NUMBER:
            value = new BigInteger(card.argument(1));
            LOG.debug("Loading number {} into address {}", value, address);
            this.store.put(address, value);
            break;
        case STORE:
            value = this.mill.transferOut();
            this.store.put(address, value);
            break;
        case STOREPRIME:
            value = this.mill.transferOut(true);
            this.store.put(address, value);
            break;
        case ZLOAD:
            value = this.store.get(address);
            this.store.put(address, BigInteger.ZERO);
            this.mill.transferIn(value);
            break;
        case ZLOADPRIME:
            value = this.store.get(address);
            this.store.put(address, BigInteger.ZERO);
            this.mill.transferIn(value, true);
            break;
        default:
            throw new IllegalArgumentException(
                    "Expected memory access card, not " + card);
        }

    }

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
                    this.attendant.onBell(currentCard);;
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
            // LOG.error("Advance or reverse beyond boundary of card chain.",
            // e);
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
    public void setCurvePrinter(final CurvePrinter printer) {
        this.curvePrinter = printer;
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
     * Sets the arithmetic operation to perform on the mill based on the
     * specified card.
     * 
     * {@code card} must have a type corresponding to one of the four
     * arithmetic operations. If not, an {@code IllegalArgumentException} will
     * be thrown.
     * 
     * @param card
     *            A card whose type corresponds to one of the four arithmetic
     *            operations.
     * @throws IllegalArgumentException
     *             if {@code type} does not correspond to one of the four
     *             arithmetic operations.
     */
    private void setOperation(final Card card) {
        Operation operation;
        String name;
        switch (card.type()) {
        case ADD:
            operation = Operation.ADD;
            name = "addition";
            break;
        case DIVIDE:
            operation = Operation.DIVIDE;
            name = "division";
            break;
        case MULTIPLY:
            operation = Operation.MULTIPLY;
            name = "multiplication";
            break;
        case SUBTRACT:
            operation = Operation.SUBTRACT;
            name = "subtraction";
            break;
        default:
            throw new IllegalArgumentException("Expected arithmetic card: "
                    + card);
        }
        LOG.debug("Setting operation on mill: {}", name);
        this.mill.setOperation(operation);
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
     * Performs the left or right shift specified by the given card.
     * 
     * @param card
     *            A left or right shift card.
     * @throws BadCard
     *             if there is a syntax error on the card.
     * @throws IllegalArgumentException
     *             if the card is not a shift card.
     */
    private void setShift(final Card card) throws BadCard {
        int shift;
        try {
            shift = Integer.parseInt(card.argument(0));
        } catch (NumberFormatException e) {
            throw new BadCard("Failed to parse step up value", card, e);
        }

        switch (card.type()) {
        case LSHIFTN:
            LOG.debug("Performing left shift on mill by {}", shift);
            try {
                this.mill.leftShift(shift);
            } catch (IllegalArgumentException e) {
                throw new BadCard("Shift value is out of bounds", card, e);
            }
            break;
        case RSHIFTN:
            LOG.debug("Performing right shift on mill by {}", shift);
            try {
                this.mill.rightShift(shift);
            } catch (IllegalArgumentException e) {
                throw new BadCard("Shift value is out of bounds", card, e);
            }
            break;
        default:
            throw new IllegalArgumentException("Expected shift card, not "
                    + card);
        }
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
}
