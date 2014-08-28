/**
 * CardParserTest.java - tests for CardParser
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
package analyticalengine.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import analyticalengine.TestUtils;
import analyticalengine.cards.CardType;

/**
 * Tests for {@link analyticalengine.io.CardParser}.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class CardParserTest {

    /** Test for parsing strings into cards. */
    @Test
    public void testToCard() {
        try {
            assertEquals(CardType.ADD, CardParser.toCard("+").type());
            assertEquals(CardType.SUBTRACT, CardParser.toCard("-").type());
            assertEquals(CardType.MULTIPLY, CardParser.toCard("*").type());
            assertEquals(CardType.DIVIDE, CardParser.toCard("/").type());

            assertEquals(CardType.CFORWARD, CardParser.toCard("CF?").type());
            assertEquals(CardType.FORWARD, CardParser.toCard("CF+").type());
            assertEquals(CardType.CBACKWARD, CardParser.toCard("CB?").type());
            assertEquals(CardType.BACKWARD, CardParser.toCard("CB+").type());

            assertEquals(CardType.BELL, CardParser.toCard("B").type());
            assertEquals(CardType.PRINT, CardParser.toCard("P").type());
            assertEquals(CardType.HALT, CardParser.toCard("H").type());

            assertEquals(CardType.SETX, CardParser.toCard("DX").type());
            assertEquals(CardType.SETY, CardParser.toCard("DY").type());
            assertEquals(CardType.DRAW, CardParser.toCard("D+").type());
            assertEquals(CardType.MOVE, CardParser.toCard("D-").type());

            assertEquals(CardType.NUMBER, CardParser.toCard("N123 4").type());

            assertEquals(CardType.LOAD, CardParser.toCard("L123").type());
            assertEquals(CardType.LOADPRIME, CardParser.toCard("L12'").type());
            assertEquals(CardType.STORE, CardParser.toCard("S123").type());
            assertEquals(CardType.STOREPRIME, CardParser.toCard("S123'")
                    .type());
            assertEquals(CardType.ZLOAD, CardParser.toCard("Z123").type());
            assertEquals(CardType.ZLOADPRIME, CardParser.toCard("Z123'")
                    .type());

            assertEquals(CardType.LSHIFT, CardParser.toCard("<").type());
            assertEquals(CardType.RSHIFT, CardParser.toCard(">").type());
            assertEquals(CardType.LSHIFTN, CardParser.toCard("<9").type());
            assertEquals(CardType.RSHIFTN, CardParser.toCard(">9").type());

            assertEquals(CardType.BACKSTART, CardParser.toCard("(").type());
            assertEquals(CardType.CBACKSTART, CardParser.toCard("(?").type());
            assertEquals(CardType.BACKEND, CardParser.toCard(")").type());
            assertEquals(CardType.FORWARDSTART, CardParser.toCard("{").type());
            assertEquals(CardType.CFORWARDSTART, CardParser.toCard("{?")
                    .type());
            assertEquals(CardType.FORWARDEND, CardParser.toCard("}").type());
            assertEquals(CardType.ALTERNATION, CardParser.toCard("}{").type());

            assertEquals(CardType.COMMENT, CardParser.toCard(". foo").type());
            assertEquals(CardType.COMMENT, CardParser.toCard("  foo").type());

            assertEquals(CardType.TRACEON, CardParser.toCard("T1").type());
            assertEquals(CardType.TRACEOFF, CardParser.toCard("T0").type());

            assertEquals(CardType.INCLUDE,
                    CardParser.toCard("A include cards foo.ae").type());
            assertEquals(CardType.INCLUDELIB,
                    CardParser.toCard("A include from library cards for sin")
                            .type());

            assertEquals(CardType.DECIMALEXPAND,
                    CardParser.toCard("A set decimal places to 9").type());

            assertEquals(CardType.WRITEPICTURE,
                    CardParser.toCard("A write numbers as 9.999").type());
            assertEquals(CardType.WRITEDECIMAL,
                    CardParser.toCard("A write numbers with decimal point")
                            .type());

            assertEquals(CardType.WRITEROWS,
                    CardParser.toCard("A write in rows").type());
            assertEquals(CardType.WRITECOLUMNS,
                    CardParser.toCard("A write in columns").type());
            assertEquals(CardType.ANNOTATE,
                    CardParser.toCard("A write annotation foo").type());
            assertEquals(CardType.NEWLINE,
                    CardParser.toCard("A write new line").type());
        } catch (UnknownCard e) {
            TestUtils.fail(e);
        }

        try {
            CardParser.toCard("$ bogus card");
            TestUtils.shouldHaveThrownException();
        } catch (UnknownCard e) {
            assertTrue(true);
        }
    }
}
