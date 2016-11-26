/**
 * CardParserTest.java - tests for CardParser
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
package analyticalengine.cards;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import analyticalengine.TestUtils;

/**
 * Tests for {@link analyticalengine.cards.Card}.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class CardTest {

    /** Test for parsing strings into cards. */
    @Test
    public void testToCard() {
        try {
            assertEquals(CardType.ADD, Card.fromString("+").type());
            assertEquals(CardType.SUBTRACT, Card.fromString("-").type());
            assertEquals(CardType.MULTIPLY, Card.fromString("*").type());
            assertEquals(CardType.DIVIDE, Card.fromString("/").type());

            assertEquals(CardType.CFORWARD, Card.fromString("CF?").type());
            assertEquals(CardType.FORWARD, Card.fromString("CF+").type());
            assertEquals(CardType.CBACKWARD, Card.fromString("CB?").type());
            assertEquals(CardType.BACKWARD, Card.fromString("CB+").type());

            assertEquals(CardType.BELL, Card.fromString("B").type());
            assertEquals(CardType.PRINT, Card.fromString("P").type());
            assertEquals(CardType.HALT, Card.fromString("H").type());

            assertEquals(CardType.SETX, Card.fromString("DX").type());
            assertEquals(CardType.SETY, Card.fromString("DY").type());
            assertEquals(CardType.DRAW, Card.fromString("D+").type());
            assertEquals(CardType.MOVE, Card.fromString("D-").type());

            assertEquals(CardType.NUMBER, Card.fromString("N123 4").type());

            assertEquals(CardType.LOAD, Card.fromString("L123").type());
            assertEquals(CardType.LOADPRIME, Card.fromString("L12'").type());
            assertEquals(CardType.STORE, Card.fromString("S123").type());
            assertEquals(CardType.STOREPRIME, Card.fromString("S123'").type());
            assertEquals(CardType.ZLOAD, Card.fromString("Z123").type());
            assertEquals(CardType.ZLOADPRIME, Card.fromString("Z123'").type());

            assertEquals(CardType.LSHIFT, Card.fromString("<").type());
            assertEquals(CardType.RSHIFT, Card.fromString(">").type());
            assertEquals(CardType.LSHIFTN, Card.fromString("<9").type());
            assertEquals(CardType.RSHIFTN, Card.fromString(">9").type());

            assertEquals(CardType.BACKSTART, Card.fromString("(").type());
            assertEquals(CardType.CBACKSTART, Card.fromString("(?").type());
            assertEquals(CardType.BACKEND, Card.fromString(")").type());
            assertEquals(CardType.FORWARDSTART, Card.fromString("{").type());
            assertEquals(CardType.CFORWARDSTART, Card.fromString("{?").type());
            assertEquals(CardType.FORWARDEND, Card.fromString("}").type());
            assertEquals(CardType.ALTERNATION, Card.fromString("}{").type());

            assertEquals(CardType.COMMENT, Card.fromString(". foo").type());
            assertEquals(CardType.COMMENT, Card.fromString("  foo").type());

            assertEquals(CardType.TRACEON, Card.fromString("T1").type());
            assertEquals(CardType.TRACEOFF, Card.fromString("T0").type());

            assertEquals(CardType.INCLUDE,
                    Card.fromString("A include cards foo.ae").type());
            assertEquals(CardType.INCLUDELIB,
                    Card.fromString("A include from library cards for sin")
                            .type());

            assertEquals(CardType.DECIMALEXPAND,
                    Card.fromString("A set decimal places to 9").type());

            assertEquals(CardType.WRITEPICTURE,
                    Card.fromString("A write numbers as 9.999").type());
            assertEquals(CardType.WRITEDECIMAL, Card
                    .fromString("A write numbers with decimal point").type());

            assertEquals(CardType.WRITEROWS,
                    Card.fromString("A write in rows").type());
            assertEquals(CardType.WRITECOLUMNS,
                    Card.fromString("A write in columns").type());
            assertEquals(CardType.ANNOTATE,
                    Card.fromString("A write annotation foo").type());
            assertEquals(CardType.NEWLINE,
                    Card.fromString("A write new line").type());
        } catch (UnknownCard e) {
            TestUtils.fail(e);
        }

        try {
            Card.fromString("$ bogus card");
            TestUtils.shouldHaveThrownException();
        } catch (UnknownCard e) {
            assertTrue(true);
        }
    }
}
