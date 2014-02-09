/**
 * CardType.java - indicates the instruction represented by a card
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
package analyticalengine.cards;

/**
 * Enumerates possible instructions that a {@link analyticalengine.cards.Card}
 * represents.
 * 
 * Each instruction has a particular number of arguments, as indicated by the
 * {@link #numArguments()} method.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public enum CardType {
    /** Add the next two numbers loaded into the mill. */
    ADD(0),
    /** An "else if" statement. */
    ALTERNATION(0),
    /**
     * Instructs the attendant to write a specific string to the final report.
     */
    ANNOTATE(1),
    /** The end of a backward loop. */
    BACKEND(0),
    /** The start of an unconditional backward loop. */
    BACKSTART(0),
    /** Go backwards unconditionally. */
    BACKWARD(1),
    /** Ring the bell to alert the attendant. */
    BELL(0),
    /** The start of a conditional backward loop. */
    CBACKSTART(0),
    /** Go backward on the condition that the current axis is zero. */
    CBACKWARD(1),
    /** Go forward on the condition that the current axis is zero. */
    CFORWARD(1),
    /** The start of a conditional forward loop. */
    CFORWARDSTART(0),
    /**
     * A comment in the program which does not affect the behavior of the
     * program.
     */
    COMMENT(1),
    /**
     * Instruct the attendant to expand decimals by the specified number of
     * places.
     */
    DECIMALEXPAND(1),
    /** Divide the next two numbers loaded into the mill. */
    DIVIDE(0),
    /** Draw on the curve printer to the location previously specified. */
    DRAW(0),
    /** Go forward unconditionally. */
    FORWARD(1),
    /** The end of a forward loop. */
    FORWARDEND(0),
    /** The start of an unconditional forward loop. */
    FORWARDSTART(0),
    /** Halts the Analytical Engine immediately. */
    HALT(0),
    /**
     * Instructs the attendant to include the cards listed in the specified
     * file.
     */
    INCLUDE(1),
    /**
     * Instructs the attendant to include the cards listed in the specified
     * library file.
     */
    INCLUDELIB(1),
    /**
     * Loads a number from the store into a main ingress axis of the mill.
     */
    LOAD(1),
    /**
     * Loads a number from the store into the prime ingress axis of the mill.
     */
    LOADPRIME(1),
    /**
     * Shift the decimal place on the loaded numbers to the left by an amount
     * specified in a previous card.
     */
    LSHIFT(0),
    /**
     * Shift the decimal place on the loaded numbers to the left by the
     * specified amount.
     */
    LSHIFTN(1),
    /**
     * Moves the pen of the curve printer to the location previously specified.
     */
    MOVE(0),
    /**
     * Multiply the next two numbers loaded into the mill.
     */
    MULTIPLY(0),
    /**
     * Instructs the attendant to move to the next line in the final report.
     */
    NEWLINE(0),
    /** Stores a number immediately in the specified address of the store. */
    NUMBER(2),
    /** Prints the most recently used axis to the printer. */
    PRINT(0),
    /**
     * Shift the decimal place on the loaded numbers to the right by an amount
     * specified in a previous card.
     */
    RSHIFT(0),
    /**
     * Shift the decimal place on the loaded numbers to the right by the
     * specified amount.
     */
    RSHIFTN(1),
    /** Sets the x coordinate of the curve printer. */
    SETX(0),
    /** Sets the y coordinate of the curve printer. */
    SETY(0),
    /**
     * Stores the contents of the most recently used main axis in the store.
     */
    STORE(1),
    /** Stores the contents of the prime axis in the store. */
    STOREPRIME(1),
    /** Subtract the next two numbers loaded into the mill. */
    SUBTRACT(0),
    /** Disable trace information from this point on. */
    TRACEOFF(0),
    /** Enable trace information from this point on. */
    TRACEON(0),
    /**
     * Instructs the attendant to write the output from the printer in columns.
     */
    WRITECOLUMNS(0),
    /**
     * Instructs the attendant to write the output from the printer accounting
     * a shifted decimal point.
     */
    WRITEDECIMAL(0),
    /**
     * Instructs the attendant to write the numeric output from the printer in
     * a specified format.
     */
    WRITEPICTURE(1),
    /**
     * Instructs the attendant to write the output from the printer in rows.
     */
    WRITEROWS(0),
    /**
     * Loads a number from the store into a main ingress axis of the mill,
     * replacing it with a zero in the store.
     */
    ZLOAD(1),
    /**
     * Loads a number from the store into the prime ingress axis of the mill,
     * replacing it with a zero in the store.
     */
    ZLOADPRIME(1);

    /** The number of arguments required by this instruction. */
    private int numArguments;

    /**
     * Stores the number of arguments required by this instruction.
     * 
     * @param numArguments
     *            The number of arguments required by this instruction.
     */
    CardType(final int numArguments) {
        this.numArguments = numArguments;
    }

    /**
     * Returns the number of arguments required by this instruction.
     * 
     * @return The number of arguments required by this instruction.
     */
    public int numArguments() {
        return this.numArguments;
    }
}
