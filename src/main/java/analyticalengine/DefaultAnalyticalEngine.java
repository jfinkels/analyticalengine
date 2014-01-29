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

public class DefaultAnalyticalEngine implements AnalyticalEngine {
    private Attendant attendant = null;
    private Mill mill = null;
    private Store store = null;
    private CardReader cardReader = null;
    private Printer printer = null;
    private CurvePrinter curvePrinter = null;

    /**
     * The logger for this class.
     */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(DefaultAnalyticalEngine.class);

    @Override
    public void reset() {
        this.mill.reset();
        this.store.reset();
        this.curvePrinter.reset();
    }

    // void inputCards(List<Card> cards) {
    // this.cardReader.mountCards(cards);
    // }

    private void executeCard(Card card) throws Bell, Halt, Error {
        LOG.debug("Executing card {}", card);

        // These variables show up in more than one case, but we cannot define
        // them within each case because each case has the same (switch-level)
        // scope, even though it would be more readable that way.
        int numCards;
        long address;
        BigInteger value;

        switch (card.type()) {
        case ADD:
            this.mill.setOperation(Operation.ADD);
            break;
        case ANNOTATE:
            this.attendant.annotate(card.argument(0));
            break;
        case BACKWARD:
            numCards = Integer.parseInt(card.argument(0));
            this.cardReader.reverse(numCards);
            break;
        case BELL:
            throw new Bell("Card indicated bell", card);
        case CBACKWARD:
            if (this.mill.hasRunUp()) {
                numCards = Integer.parseInt(card.argument(0));
                this.cardReader.reverse(numCards);
            }
            break;
        case CFORWARD:
            if (this.mill.hasRunUp()) {
                numCards = Integer.parseInt(card.argument(0));
                this.cardReader.advance(numCards);
                break;
            }
        case COMMENT:
            LOG.debug("Comment: " + card.argument(0));
            break;
        case DIVIDE:
            this.mill.setOperation(Operation.DIVIDE);
            break;
        case SETX:
            this.curvePrinter.setX(this.mill.mostRecentValue());
            break;
        case SETY:
            this.curvePrinter.setY(this.mill.mostRecentValue());
            break;
        case DRAW:
            this.curvePrinter.draw();
            break;
        case FORWARD:
            numCards = Integer.parseInt(card.argument(0));
            this.cardReader.advance(numCards);
            break;
        case HALT:
            throw new Halt("Card indicated halt", card);
        case LOAD:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                this.mill.transferIn(value, false);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse load address", exception);
            }
            break;
        case LOADPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                this.mill.transferIn(value, true);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse load address", exception);
            }
            break;
        case LSHIFT:
            try {
                int shift = Integer.parseInt(card.argument(0));
                if (shift < 0 || shift > 100) {
                    throw new Error("Bad stepping up card");
                }
                this.mill.leftShift(shift);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse step up value", exception);
            }
            break;
        case MOVE:
            this.curvePrinter.move();
            break;
        case MULTIPLY:
            this.mill.setOperation(Operation.MULTIPLY);
            break;
        case NUMBER:
            address = Long.parseLong(card.argument(0));
            if (address < 0 || address >= this.store.maxAddress()) {
                throw new Error("Address out of bounds: " + address);
            }
            value = new BigInteger(card.argument(1));
            if (value.compareTo(this.mill.maxValue()) > 0) {
                throw new Error("Value too large to store: " + value);
            }
            this.store.put(address, value);
            break;
        case PRINT:
            value = this.mill.mostRecentValue();
            String printed = this.printer.print(value);
            this.attendant.receiveOutput(printed);
            break;
        case RSHIFT:
            try {
                int shift = Integer.parseInt(card.argument(0));
                if (shift < 0 || shift > 100) {
                    throw new Error("Bad stepping up card");
                }
                this.mill.rightShift(shift);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse step down value", exception);
            }
            break;
        case STORE:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.mill.transferOut(false);
                this.store.put(address, value);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse store address", exception);
            }
            break;
        case STOREPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.mill.transferOut(true);
                this.store.put(address, value);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse store address", exception);
            }
            break;
        case SUBTRACT:
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
                this.mill.transferIn(value, false);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse zload address", exception);
            }
            break;
        case ZLOADPRIME:
            try {
                address = Long.parseLong(card.argument(0));
                value = this.store.get(address);
                this.store.put(address, BigInteger.ZERO);
                this.mill.transferIn(value, true);
            } catch (NumberFormatException exception) {
                throw new Error("Failed to parse zload address", exception);
            }
            break;
        case NEWLINE:
            this.attendant.writeNewline();
            break;
        case WRITECOLUMNS:
            this.attendant.writeInColumns();
            break;
        case WRITEROWS:
            this.attendant.writeInRows();
            break;
        case WRITEPICTURE:
            this.attendant.setFormat(card.argument(0));
            break;
        case WRITEDECIMAL:
            this.attendant.writeWithDecimal();
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
            // these should all be removed by attendant before loading cards
            throw new Error("Attendant failed to remove card", card);
        default:
            break;
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Card currentCard = this.cardReader.readAndAdvance();
                try {
                    this.executeCard(currentCard);
                } catch (Bell bell) {
                    LOG.info("Bell!");
                }
            }
        } catch (Error error) {
            // TODO do something
        } catch (Halt halt) {
            // This would print the stack trace for the Halt exception.
            // LOG.info("Program halted.", halt);
            LOG.info("Program halted.");
        } catch (IndexOutOfBoundsException exception) {
            LOG.error(
                    "Program indicated advance or reverse beyond boundary of card chain.",
                    exception);
            throw exception;
        }
    }

    @Override
    public void setAttendant(Attendant attendant) {
        this.attendant = attendant;
    }

    @Override
    public void setMill(Mill mill) {
        this.mill = mill;
    }

    @Override
    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public void setCardReader(CardReader reader) {
        this.cardReader = reader;
    }

    @Override
    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void setCurvePrinter(CurvePrinter printer) {
        this.curvePrinter = printer;
    }
}
