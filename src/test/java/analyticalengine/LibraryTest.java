/**
 * LibraryTest.java - tests for built-in library functions
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

import org.junit.Test;

/**
 * Test for built-in library functions.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
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
     * Simple test for exponential function.
     */
    @Test
    public void testSimpleExp() {
        runProgram("test_exp_simple.ae");
        double expected = Math.pow(Math.E, 2);
        double actual = Double.valueOf(this.attendant().finalReport());
        assertEquals(expected, actual, TOLERANCE);

    }

    /** Test for the exponential function. */
    @Test
    public void testExp() {
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
}
