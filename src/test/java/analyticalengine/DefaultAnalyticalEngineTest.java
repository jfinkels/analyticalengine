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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyticalengine.cards.Card;
import analyticalengine.io.UnknownCard;

/**
 * Tests for the DefaultAnalayticalEngine class.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class DefaultAnalyticalEngineTest {

    /** The Analytical Engine instance to test. */
    private AnalyticalEngine engine = null;
    /** The attendant required to operate the Analytical Engine. */
    private Attendant attendant = null;

    /** Creates the Analytical Engine to test. */
    @Before
    public void setUp() {
        this.engine = new DefaultAnalyticalEngine();
        this.attendant = new DefaultAttendant();
        CardReader reader = new ArrayListCardReader();
        CurvePrinter curvePrinter = new AWTCurvePrinter();
        Mill mill = new DefaultMill();
        Printer printer = new StringPrinter();
        Store store = new HashMapStore();

        this.attendant.setCardReader(reader);

        this.engine.setAttendant(attendant);
        this.engine.setCardReader(reader);
        this.engine.setCurvePrinter(curvePrinter);
        this.engine.setMill(mill);
        this.engine.setPrinter(printer);
        this.engine.setStore(store);
    }

    /** Resets the Analytical Engine being tested. */
    @After
    public void tearDown() {
        // this technically doesn't need to be done, since we are creating new
        // objects in set up code anyway
        this.attendant.reset();
        this.engine.reset();
    }

    /** Test arithmetic operations. */
    @Test
    public void testArithmetic() {
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/test_arithmetic.ae");
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        this.engine.run();

        assertEquals("8\n4\n6\n12\n", this.attendant.finalReport());
    }

    /**
     * Tests the {@link analyticalengine.DefaultAnalyticalEngine#run()} method.
     */
    @Test
    public void testRun() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/ex0.ae");
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        // The final report should indicate a 3333, according to the program.
        assertEquals("3333", this.attendant.finalReport());
    }

    /** Tests the division operator. */
    @Test
    public void testDivide() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/ex1.ae");
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        // The final report should indicate a "357\n4"
        assertEquals("357" + System.lineSeparator() + "4",
                this.attendant.finalReport());
    }

    /** Test arithmetic operations. */
    @Test
    public void testLoad() {
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/test_load.ae");
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        this.engine.run();

        BigInteger quotient = DefaultMill.MAX.divide(new BigInteger("2"));
        assertEquals("1\n0\n" + quotient + "\n1\n0\n0\n",
                this.attendant.finalReport());
    }

    /** Test the shift operators. */
    @Test
    public void testShifts() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/ex2.ae");
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        assertEquals("357142857" + System.lineSeparator() + "4000000",
                this.attendant.finalReport());
    }

}
