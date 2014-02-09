/**
 * ProgramReaderTest.java - tests for ProgramReader
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import analyticalengine.TestUtils;
import analyticalengine.cards.Card;
import analyticalengine.cards.CardType;

/**
 * Tests for {@link analyticalengine.io.ProgramReader}.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class ProgramReaderTest {

    /**
     * The program that should be parsed from the test file.
     * 
     * This is the list of cards representing the following program.
     * 
     * <pre>
     * <code>
     *      A write in rows
     *      A write numbers as 9
     *      N120 10000
     *      N121 3
     *      /
     *      L120
     *      L121
     *      S122'
     *      P
     * </code>
     * </pre>
     * 
     */
    public static final List<Card> EXPECTED = Arrays.asList(new Card(
            CardType.WRITEROWS), new Card(CardType.WRITEPICTURE,
            new String[] { "9" }), new Card(CardType.NUMBER, new String[] {
            "120", "10000" }), new Card(CardType.NUMBER, new String[] { "121",
            "3" }), new Card(CardType.DIVIDE), new Card(CardType.LOAD,
            new String[] { "120" }), new Card(CardType.LOAD,
            new String[] { "121" }), new Card(CardType.STOREPRIME,
            new String[] { "122" }), new Card(CardType.PRINT));

    /**
     * Asserts that the two specified lists have the same sequence of cards.
     * 
     * If any pair of cards does not have the same type or arguments, an
     * {@link AssertionError} will be raised.
     * 
     * @param expected
     *            The expected list of cards.
     * @param actual
     *            The actual list of cards.
     */
    private static void assertCardsEqual(final List<Card> expected,
            final List<Card> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Card expectedCard = expected.get(i);
            Card actualCard = actual.get(i);
            assertEquals(expectedCard.type(), actualCard.type());
            assertEquals(expectedCard.numArguments(),
                    actualCard.numArguments());
            for (int j = 0; j < actualCard.numArguments(); j++) {
                assertEquals(expectedCard.argument(j), actualCard.argument(j));
            }
        }

    }

    /** Tests reading a sequence of cards from a text file. */
    @Test
    public void testFromFile() {
        try {
            Path testfile = Paths.get(this.getClass().getResource("/test_basic.ae")
                    .toURI());
            List<Card> cards = ProgramReader.fromPath(testfile);
            assertCardsEqual(EXPECTED, cards);
        } catch (IOException | UnknownCard | URISyntaxException e) {
            TestUtils.fail(e);
        }
    }

    /** Tests reading a sequence of cards from a string. */
    @Test
    public void testFromString() {
        String program = "A write in rows\n" + "A write numbers as 9\n"
                + "N120 10000\n" + "N121 3\n" + "/\n" + "L120\n" + "L121\n"
                + "S122'\n" + "P\n";
        try {
            List<Card> cards = ProgramReader.fromString(program);
            assertCardsEqual(EXPECTED, cards);
        } catch (UnknownCard e) {
            TestUtils.fail(e);
        }
    }

}
