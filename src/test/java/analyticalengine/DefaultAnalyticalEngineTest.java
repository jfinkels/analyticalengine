/**
 * DefaultAnalyticalEngineTest.java - tests for DefaultAnalyticalEngine
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import analyticalengine.attendant.Library;
import analyticalengine.attendant.LibraryLookupException;
import analyticalengine.cards.Card;
import analyticalengine.components.DefaultMill;
import analyticalengine.components.HashMapStore;
import analyticalengine.io.CardParser;
import analyticalengine.io.UnknownCard;

/**
 * Tests for the DefaultAnalayticalEngine class.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultAnalyticalEngineTest extends EngineTestBase {

    /**
     * The tolerance for comparing floating point values computed by the
     * Analytical Engine to floating point values computed by the Java virtual
     * machine.
     * 
     * In these tests, we set the Analytical Engine to use 20 decimal points,
     * so we set the tolerance to {@code 1 / 20.0}.
     */
    public static final double TOLERANCE = 1 / 20.0;

    /**
     * Joins all the elements of {@code args} with new line characters in
     * between each one.
     * 
     * @param endingNewLine
     *            Whether to place a new line character at the end of the
     *            resulting string.
     * @param args
     *            The strings to join.
     * @return The given strings joined with new line characters.
     */
    private static String join(final boolean endingNewLine,
            final String... args) {
        if (args.length == 0) {
            if (endingNewLine) {
                return System.lineSeparator();
            }
            return "";
        }
        if (args.length == 1) {
            if (endingNewLine) {
                return args[0] + System.lineSeparator();
            }
            return args[0];
        }
        String[] newArgs = new String[args.length - 1];
        System.arraycopy(args, 1, newArgs, 0, args.length - 1);
        return args[0] + System.lineSeparator() + join(endingNewLine, newArgs);
    }

    /**
     * Convenience method for {@link #join(boolean, String...)} with first
     * argument set to {@code true}.
     * 
     * @param args
     *            The strings to join.
     * @return The given strings joined with new line characters, plus another
     *         new line at the end of the string.
     */
    private static String join(final String... args) {
        return join(true, args);
    }

    /** Test for alternation (the "if-else" control statement). */
    @Test
    public void testAlternation() {
        runProgram("test_alternation.ae");
        assertEquals("900\n", this.attendant().finalReport());
    }

    /**
     * Tests attendant annotations.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testAnnotation()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        runProgramString("A write annotation foo");
        assertEquals("foo\n", this.attendant().finalReport());
    }

    /** Test arithmetic operations. */
    @Test
    public void testArithmetic() {
        runProgram("test_arithmetic.ae");
        String finalReport = this.attendant().finalReport();
        assertEquals(join("8", "4", "6", "12"), finalReport);
    }

    /** Test for loop with condition check at the end (a "do-while" loop). */
    @Test
    public void testBackLoop() {
        runProgram("test_backloop.ae");
        String[] squares = new String[10];
        for (int i = 1; i < 11; i++) {
            squares[i - 1] = String.valueOf(i * i);
        }
        assertEquals(join(squares), this.attendant().finalReport());
    }

    /**
     * Test backward.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testBackward()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        runProgramString(join("CF+1", "H", "N1 3", "L1", "P", "CB+5"));
        assertEquals("3\n", this.attendant().finalReport());
    }

    /** Test bad addresses. */
    @Test
    public void testBadAddress() {
        for (String operator : Arrays.asList("L", "Z", "S")) {
            for (String prime : Arrays.asList("", "'")) {
                List<Card> cards = null;
                try {
                    String badCard = operator + "1.1" + prime;
                    cards = Arrays.asList(CardParser.toCard(badCard));
                    this.attendant().loadProgram(cards);
                } catch (BadCard | IOException | UnknownCard
                        | LibraryLookupException e) {
                    TestUtils.fail(e);
                }

                try {
                    this.engine().run();
                    TestUtils.shouldHaveThrownException();
                } catch (BadCard e) {
                    assertTrue(true);
                }

                // reset after each test
                this.engine().reset();
                this.attendant().reset();
            }
        }
    }

    /**
     * Test for advancing or reversing beyond the bounds of the card chain.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testBadAdvance()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        try {
            runProgramString("CF+1");
            TestUtils.shouldHaveThrownException();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        this.engine().reset();
        this.attendant().reset();

        try {
            runProgramString("CB+2");
            TestUtils.shouldHaveThrownException();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /**
     * Test bad number arguments.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if the specified program includes a syntax error
     */
    @Test
    public void testBadNumber()
            throws UnknownCard, IOException, LibraryLookupException, BadCard {
        int badAddress = HashMapStore.MAX_ADDRESS + 1;
        try {
            runProgramString("N" + badAddress + " 0");
            TestUtils.shouldHaveThrownException();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        this.engine().reset();
        this.attendant().reset();

        try {
            runProgramString("N-1 0");
            TestUtils.shouldHaveThrownException();
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }

        this.engine().reset();
        this.attendant().reset();

        BigInteger badValue = HashMapStore.MAX_VALUE.add(BigInteger.ONE);
        try {
            runProgramString("N1 " + badValue);
            TestUtils.shouldHaveThrownException();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        this.engine().reset();
        this.attendant().reset();

        badValue = HashMapStore.MIN_VALUE.subtract(BigInteger.ONE);
        try {
            runProgramString("N1 " + badValue);
            TestUtils.shouldHaveThrownException();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /** Test bad shift arguments. */
    @Test
    public void testBadShift() {
        for (String operator : Arrays.asList("<", ">")) {
            for (String number : Arrays.asList("1.1", "-1", "101")) {
                List<Card> cards = null;
                try {
                    String badCard = operator + number;
                    cards = Arrays.asList(CardParser.toCard(badCard));
                    this.attendant().loadProgram(cards);
                } catch (BadCard | IOException | UnknownCard
                        | LibraryLookupException e) {
                    TestUtils.fail(e);
                }

                try {
                    this.engine().run();
                    TestUtils.shouldHaveThrownException();
                } catch (BadCard e) {
                    assertTrue(true);
                }

                // reset after each test
                this.engine().reset();
                this.attendant().reset();
            }
        }
    }

    /**
     * Test for the bell.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testBell()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        runProgramString("B");
        // TODO assert that standard output contains indication of bell
    }

    /** Tests conditional back. */
    @Test
    public void testConditionalBackward() {
        runProgram("test_cback.ae");
        assertEquals("720\n", this.attendant().finalReport());
    }

    /** Tests conditional forward. */
    @Test
    public void testConditionalForward() {
        runProgram("test_cforward.ae");
        String expected = "141421356237309504880\n";
        assertEquals(expected, this.attendant().finalReport());
    }

    /** Tests the division operator. */
    @Test
    public void testDivide() {
        runProgram("test_divide.ae");
        assertEquals(join("357", "4"), this.attendant().finalReport());
    }

    /**
     * Test for loop with condition check at the beginning (a "while" loop).
     */
    @Test
    public void testForwardLoop() {
        runProgram("test_forwardloop.ae");
        assertEquals("10\n", this.attendant().finalReport());
    }

    /**
     * Test halt.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testHalt()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        // should halt before any output is produced
        runProgramString(join(false, "H", "N1 3", "L1", "P"));
        assertEquals("", this.attendant().finalReport());
    }

    /** Tests for include a library function with the file extension. */
    @Test
    public void testIncludeWithExtension() {
        runProgram("test_include_with_extension.ae");
        double expected = Math.pow(Math.E, 2);
        double actual = Double.valueOf(this.attendant().finalReport());
        assertEquals(expected, actual, TOLERANCE);
    }

    /** Test loading numbers operations. */
    @Test
    public void testLoad() {
        runProgram("test_load.ae");
        BigInteger quotient = DefaultMill.MAX.add(BigInteger.ONE)
                .divide(new BigInteger("2"));
        assertEquals(join("1", "0", quotient.toString(), "1", "0", "0"),
                this.attendant().finalReport());
    }

    /** Test loading a number into the prime axis. */
    @Test
    public void testLoadPrime() {
        runProgram("test_loadprime.ae");
        BigInteger quotient = DefaultMill.MAX.add(BigInteger.ONE)
                .divide(new BigInteger("2"));
        assertEquals(join(quotient.toString(), "1"),
                this.attendant().finalReport());
    }

    /**
     * Tests for printing a null value.
     * 
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    @Test
    public void testPrintNull()
            throws BadCard, UnknownCard, IOException, LibraryLookupException {
        runProgramString("P");
        // TODO assert that standard error contains indication of an attempt to
        // print a null character
    }

    /**
     * Tests the {@link analyticalengine.DefaultAnalyticalEngine#run()} method.
     */
    @Test
    public void testRun() {
        runProgram("test_basic.ae");
        assertEquals("3333\n", this.attendant().finalReport());
    }

    /** Test the shift operators. */
    @Test
    public void testShifts() {
        runProgram("test_shift.ae");
        assertEquals(join("357142857", "4000000"),
                this.attendant().finalReport());
    }

    /** Test for cards that should have been removed by the attendant. */
    @Test
    public void testShouldHaveBeenRemoved() {
        List<Card> cards = null;
        try {
            cards = Arrays.asList(CardParser.toCard("}{"));
            this.attendant().loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard
                | LibraryLookupException e) {
            TestUtils.fail(e);
        }

        try {
            this.engine().run();
            TestUtils.shouldHaveThrownException();
        } catch (BadCard e) {
            assertTrue(true);
            // } finally {
            // // reset after each test
            // this.engine().reset();
            // this.attendant().reset();
        }
    }

    /** Tests the "A write numbers as" instruction. */
    @Test
    public void testWriteAs() {
        runProgram("test_writeas.ae");
        String expected = "Total price for 337 items is"
                + " 1,114 pounds, 18 shillings, 2 pence.\n";
        assertEquals(expected, this.attendant().finalReport());
    }
}
