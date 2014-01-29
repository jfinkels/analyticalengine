/**
 * MainTest.java - tests for Main
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
package analyticalengine.main;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link analyticalengine.main.Main}.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class MainTest {

    /** The original standard error. */
    private PrintStream oldStderr;

    /** The original standard output. */
    private PrintStream oldStdout;

    /**
     * An output stream used to capture and examine messages printed to
     * standard error.
     */
    private OutputStream stderr;

    /**
     * An output stream used to capture and examine messages printed to
     * standard output.
     */
    private OutputStream stdout;

    /** Enables capturing standard output and standard error. */
    @Before
    public void setUp() {

        this.oldStderr = System.err;
        this.oldStdout = System.out;

        this.stdout = new ByteArrayOutputStream();
        this.stderr = new ByteArrayOutputStream();
        PrintStream newStdout = new PrintStream(this.stdout);
        PrintStream newStderr = new PrintStream(this.stderr);

        System.setOut(newStdout);
        System.setErr(newStderr);

    }

    /** Restores standard output and standard error. */
    @After
    public void tearDown() {
        System.out.flush();
        System.err.flush();
        System.setOut(this.oldStdout);
        System.setErr(this.oldStderr);
    }

    /** Test for parsing arguments. */
    @Test
    public void testArgumentParsing() {
        String[] argv;

        // simply run a program
        argv = new String[] { "src/test/resources/analyticalengine/ex0.ae" };
        Main.main(argv);

        // list but don't run the program
        argv = new String[] { "-l",
                "src/test/resources/analyticalengine/ex0.ae" };
        Main.main(argv);
        String output = this.stdout.toString();
        assertTrue(output.toLowerCase().contains("number"));
        assertTrue(output.toLowerCase().contains("120"));
        assertTrue(output.toLowerCase().contains("10000"));
        assertTrue(output.toLowerCase().contains("121"));
        assertTrue(output.toLowerCase().contains("3"));
        assertTrue(output.toLowerCase().contains("divide"));
        assertTrue(output.toLowerCase().contains("load"));
        assertTrue(output.toLowerCase().contains("store"));

        // display verbose information
        argv = new String[] { "-v", "1",
                "src/test/resources/analyticalengine/ex0.ae" };
        Main.main(argv);

        // display more verbose information
        argv = new String[] { "-v", "2",
                "src/test/resources/analyticalengine/ex0.ae" };
        Main.main(argv);
    }
}
