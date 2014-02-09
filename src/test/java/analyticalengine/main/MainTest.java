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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyticalengine.TestUtils;

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

    /**
     * Test for setting the library path.
     * 
     * @throws IOException
     *             if there is a problem opening or writing to a temporary
     *             file.
     */
    @Test
    public void testLibraryPath() throws IOException {
        // create a temporary file that will include a function from another
        Path tempProgram = Files.createTempFile(null, null);

        // create two temporary directories
        Path tempDir1 = Files.createTempDirectory(null);
        Path tempDir2 = Files.createTempDirectory(null);

        // create two temporary files with the same name, one in each directory
        Path tempFile1 = Files.createTempFile(tempDir1, null, ".ae");
        Path baseName = tempFile1.getFileName();
        Path tempFile2 = tempDir2.resolve(baseName);
        Files.createFile(tempFile2);

        // ensure the files are deleted when the JVM exits
        tempProgram.toFile().deleteOnExit();
        tempFile1.toFile().deleteOnExit();
        tempFile2.toFile().deleteOnExit();
        tempDir1.toFile().deleteOnExit();
        tempDir2.toFile().deleteOnExit();

        // write two different programs, one in each of the temporary files
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile1,
                Charset.defaultCharset())) {
            writer.write("N1 2\n");
            writer.write("N2 3\n");
            writer.write("*\n");
            writer.write("L1\n");
            writer.write("L2\n");
            writer.write("S3\n");
            // program should print 6 as output
            writer.write("P\n");
        }

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile2,
                Charset.defaultCharset())) {
            writer.write("N1 4\n");
            writer.write("N2 7\n");
            writer.write("*\n");
            writer.write("L1\n");
            writer.write("L2\n");
            writer.write("S3\n");
            // program should print 28 as output
            writer.write("P\n");
        }

        // write a program that includes the file with baseName
        String withoutExtension = baseName.toString().replace(".ae", "");
        try (BufferedWriter writer = Files.newBufferedWriter(tempProgram,
                Charset.defaultCharset())) {
            writer.write("A include from library cards for "
                    + withoutExtension + "\n");
        }

        // list tempDir2 first, so we expect 8 as output
        String[] argv = new String[] { "-s", tempDir2 + ":" + tempDir1,
                tempProgram.toString() };
        Main.main(argv);
        String output = this.stdout.toString().toLowerCase();
        assertTrue(output.contains("4"));
        assertTrue(output.contains("7"));
        assertTrue(output.contains("28"));
    }

    /** Test for listing the cards only. */
    @Test
    public void testList() {
        String testfile;
        try {
            testfile = Paths.get(
                    this.getClass().getResource("/test_basic.ae").toURI())
                    .toString();
        } catch (URISyntaxException e) {
            TestUtils.fail(e);
            return;
        }
        String[] argv = new String[] { "-l", testfile };
        Main.main(argv);
        String output = this.stdout.toString().toLowerCase();
        assertTrue(output.contains("number"));
        assertTrue(output.contains("120"));
        assertTrue(output.contains("10000"));
        assertTrue(output.contains("121"));
        assertTrue(output.contains("3"));
        assertTrue(output.contains("divide"));
        assertTrue(output.contains("load"));
        assertTrue(output.contains("store"));
    }

    /** Test for no command-line arguments. */
    @Test
    public void testNoArguments() {
        String testfile;
        try {
            testfile = Paths.get(
                    this.getClass().getResource("/test_basic.ae").toURI())
                    .toString();
        } catch (URISyntaxException e) {
            TestUtils.fail(e);
            return;
        }
        String[] argv = new String[] { testfile };
        Main.main(argv);
    }

    /** Test for stripping comments. */
    @Test
    public void testStripComments() {
        String testfile;
        try {
            URI fileUri = this.getClass().getResource("/test_divide.ae")
                    .toURI();
            testfile = Paths.get(fileUri).toString();
        } catch (URISyntaxException e) {
            TestUtils.fail(e);
            return;
        }
        String[] argv = new String[] { "-c", testfile };
        Main.main(argv);
        String output = this.stdout.toString();
        this.oldStdout.println(output);
        assertFalse(output.contains("Executing card COMMENT"));
    }

    /** Test for verbose information. */
    @Test
    public void testVerbose() {
        String testfile;
        try {
            testfile = Paths.get(
                    this.getClass().getResource("/test_basic.ae").toURI())
                    .toString();
        } catch (URISyntaxException e) {
            TestUtils.fail(e);
            return;
        }
        String[] argv = new String[] { "-v", "1", testfile };
        Main.main(argv);
        // TODO test something
    }

    /** Test for very verbose information. */
    @Test
    public void testVeryVerbose() {
        String testfile;
        try {
            testfile = Paths.get(
                    this.getClass().getResource("/test_basic.ae").toURI())
                    .toString();
        } catch (URISyntaxException e) {
            TestUtils.fail(e);
            return;
        }
        String[] argv = new String[] { "-v", "2", testfile };
        Main.main(argv);
        // TODO test something
    }
}
