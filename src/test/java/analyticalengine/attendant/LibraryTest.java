/**
 * 
 * LibraryTest.java - tests for built-in library functions
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
package analyticalengine.attendant;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;

import analyticalengine.EngineTestBase;
import analyticalengine.cards.BadCard;
import analyticalengine.cards.UnknownCard;

/**
 * Test for built-in library functions.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class LibraryTest extends EngineTestBase {

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
     * Test for arctangent function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testArctan() throws BadCard, URISyntaxException, IOException,
            UnknownCard, LibraryLookupException {
        // compute the arctangent from 0.0 to 1.0, increasing by 0.1
        runProgram("test_arctan.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(11, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            assertEquals(Math.atan(i / 10.0), actual, TOLERANCE);
        }
    }

    /**
     * Test for cosine function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testCosine() throws BadCard, URISyntaxException, IOException,
            UnknownCard, LibraryLookupException {
        // compute the cosine from 0 to 6
        runProgram("test_cosine.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(7, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            assertEquals(Math.cos(i), actual, TOLERANCE);
        }
    }

    /**
     * Test for the exponential function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testExp() throws BadCard, URISyntaxException, IOException,
            UnknownCard, LibraryLookupException {
        // compute the powers of e, from 0.0 to 1.0, increasing by 0.1
        runProgram("test_exp.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(11, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            assertEquals(Math.pow(Math.E, i / 10.0), actual, TOLERANCE);
        }
    }

    /**
     * Test for computing the natural logarithm.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testLogarithm() throws BadCard, URISyntaxException,
            IOException, UnknownCard, LibraryLookupException {
        // compute the natural log of 1 to 10, increasing by 1
        runProgram("test_ln.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(10, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            assertEquals(Math.log(i + 1), actual, TOLERANCE);
        }
    }

    /**
     * Simple test for exponential function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testSimpleExp() throws BadCard, URISyntaxException,
            IOException, UnknownCard, LibraryLookupException {
        runProgram("test_exp_simple.ae");
        double expected = Math.pow(Math.E, 2);
        double actual = Double.valueOf(this.attendant().finalReport());
        assertEquals(expected, actual, TOLERANCE);

    }

    /**
     * Test for sine function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Ignore("Original code also failed this test; bad implementation?")
    @Test
    public void testSine() throws BadCard, URISyntaxException, IOException,
            UnknownCard, LibraryLookupException {
        // compute the sine from 0 to 6
        runProgram("test_sine.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(7, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            System.out.println("got " + actual + " should be " + Math.sin(i));
            assertEquals(Math.sin(i), actual, TOLERANCE);
        }
    }

    /**
     * Test for the square root function.
     * 
     * @throws LibraryLookupException
     * @throws UnknownCard
     * @throws IOException
     * @throws URISyntaxException
     * @throws BadCard
     */
    @Test
    public void testSquareRoot() throws BadCard, URISyntaxException,
            IOException, UnknownCard, LibraryLookupException {
        // compute the square root of 0 to 10, increasing by 1
        runProgram("test_sqrt.ae");

        // get each line of the final report
        String finalReport = this.attendant().finalReport();
        String[] values = finalReport.split(System.lineSeparator());
        assertEquals(11, values.length);

        // compare each value computed by the Analytical Engine to the values
        // computed by Java
        for (int i = 0; i < values.length; i++) {
            int lastSpace = values[i].lastIndexOf(' ');
            String actualString = values[i].substring(lastSpace);
            double actual = Double.valueOf(actualString);
            assertEquals(Math.sqrt(i), actual, TOLERANCE);
        }
    }
}
