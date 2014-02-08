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

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import analyticalengine.io.UnknownCard;

/**
 * Test for built-in library functions.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class LibraryTest extends EngineTestBase {

    /** Simple test for exponential function. 
     * @throws LibraryLookupException 
     * @throws IOException 
     * @throws UnknownCard 
     * @throws BadCard */
    @Test
    public void testSimpleExp() throws BadCard, UnknownCard, IOException, LibraryLookupException {
        runProgramString("A set decimal places to 20\nN0 2\nA include from library cards for exp\nL0\nP");
        System.out.println(this.attendant().finalReport());
    }

    
    /** Test for the exponential function. */
    @Test
    public void testExp() {
        runProgram("test_exp.ae");

        // these are the values exp(0.0), exp(0.1), exp(0.2), etc.
        assertTrue(this.attendant().finalReport()
                .contains("1.00000000000000000000"));
        assertTrue(this.attendant().finalReport()
                .contains("1.10517091807564762481"));
        assertTrue(this.attendant().finalReport()
                .contains("1.22140275816016983392"));
        assertTrue(this.attendant().finalReport()
                .contains("1.34985880757600310398"));
        assertTrue(this.attendant().finalReport()
                .contains("1.49182469764127031782"));
        assertTrue(this.attendant().finalReport()
                .contains("1.64872127070012814685"));
        assertTrue(this.attendant().finalReport()
                .contains("1.82211880039050897488"));
        assertTrue(this.attendant().finalReport()
                .contains("2.01375270747047652162"));
        assertTrue(this.attendant().finalReport()
                .contains("2.22554092849246760458"));
        assertTrue(this.attendant().finalReport()
                .contains("2.45960311115694966380"));
        assertTrue(this.attendant().finalReport()
                .contains("2.71828182845904523536"));
    }
}
