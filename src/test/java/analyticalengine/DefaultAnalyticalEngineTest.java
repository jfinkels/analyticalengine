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
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import analyticalengine.cards.Card;
import analyticalengine.io.CardParser;
import analyticalengine.io.UnknownCard;

/**
 * Tests for the DefaultAnalayticalEngine class.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class DefaultAnalyticalEngineTest extends EngineTestBase {

    /** Tests attendant annotations. */
    @Test
    public void testAnnotation() {
        runProgramString("A write annotation foo");
        assertEquals("foo\n", this.attendant().finalReport());
    }

    /** Test arithmetic operations. */
    @Test
    public void testArithmetic() {
        runProgram("test_arithmetic.ae");
        assertEquals("8\n4\n6\n12\n", this.attendant().finalReport());
    }

    /** Test backward. */
    @Test
    public void testBackward() {
        runProgramString("CF+1\nH\nN1 3\nL1\nP\nCB+5");
        assertEquals("3\n", this.attendant().finalReport());
    }

    /** Test bad addresses. */
    @Test
    public void testBadAddress() {
        for (String operator : Arrays.asList("L", "Z", "S")) {
            for (String prime : Arrays.asList("", "'")) {
                System.out.println("Testing " + operator + " " + prime);
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

    /** Tests conditional back. */
    @Test
    public void testConditionalBackward() {
        runProgram("ex5.ae");
        assertEquals("720\n", this.attendant().finalReport());
    }

    /** Tests conditional forward. */
    @Test
    public void testConditionalForward() {
        runProgram("ex6.ae");
        assertEquals("141421356237309504880\n", this.attendant().finalReport());
    }

    /** Tests the division operator. */
    @Test
    public void testDivide() {
        runProgram("ex1.ae");
        assertEquals("357\n4\n", this.attendant().finalReport());
    }

    /** Test halt. */
    @Test
    public void testHalt() {
        // should halt before any output is produced
        runProgramString("H\nN1 3\nL1\nP");
        assertEquals("", this.attendant().finalReport());
    }

    /** Test arithmetic operations. */
    @Test
    public void testLoad() {
        runProgram("test_load.ae");
        BigInteger quotient = DefaultMill.MAX.divide(new BigInteger("2"));
        assertEquals("1\n0\n" + quotient + "\n1\n0\n0\n", this.attendant()
                .finalReport());
    }

    /** Test for the bell. */
    @Test
    public void testBell() {
        runProgramString("B");
        // TODO assert that standard output contains indication of bell
    }

    /** Test for cards that should have been removed by the attendant. */
    @Test
    public void testShouldHaveBeenRemoved() {
        List<Card> cards = null;
        try {
            cards = Arrays.asList(CardParser.toCard("}{"));
            this.attendant().loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard | LibraryLookupException e) {
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

    /**
     * Tests the {@link analyticalengine.DefaultAnalyticalEngine#run()} method.
     */
    @Test
    public void testRun() {
        runProgram("ex0.ae");
        assertEquals("3333\n", this.attendant().finalReport());
    }

    /** Test the shift operators. */
    @Test
    public void testShifts() {
        runProgram("ex2.ae");
        assertEquals("357142857\n4000000\n", this.attendant().finalReport());
    }

}
