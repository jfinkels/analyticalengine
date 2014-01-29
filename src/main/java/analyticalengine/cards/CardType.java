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

public enum CardType {
    ADD(0), ALTERNATION(0), ANNOTATE(1), BACKEND(0), BACKSTART(0), BACKWARD(1), BELL(
            0), CBACKSTART(0), CBACKWARD(1), CFORWARD(0), CFORWARDSTART(0), COMMENT(
            1), DECIMALEXPAND(1), DIVIDE(0), DRAW(0), FORWARD(0), FORWARDEND(0), FORWARDSTART(
            0), HALT(0), INCLUDE(1), INCLUDELIB(1), LOAD(1), LOADPRIME(1), LSHIFT(
            0), LSHIFTN(1), MOVE(0), MULTIPLY(0), NEWLINE(0), NUMBER(2), PRINT(
            0), RSHIFT(0), RSHIFTN(1), SETX(0), SETY(0), STORE(1), STOREPRIME(
            1), SUBTRACT(0), TRACEOFF(0), TRACEON(0), WRITECOLUMNS(0), WRITEDECIMAL(
            0), WRITEPICTURE(1), WRITEROWS(0), ZLOAD(1), ZLOADPRIME(1);

    private int numArguments;

    CardType(int numArguments) {
        this.numArguments = numArguments;
    }

    public int numArguments() {
        return this.numArguments;
    }
}
