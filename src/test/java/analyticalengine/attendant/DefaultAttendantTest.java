/**
 * DefaultAttendantTest.java -
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
package analyticalengine.attendant;

import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import analyticalengine.BadCard;
import analyticalengine.TestUtils;
import analyticalengine.cards.Card;
import analyticalengine.io.CardParser;
import analyticalengine.io.UnknownCard;

/**
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultAttendantTest {

    private Attendant attendant = null;
    private Library library = null;

    @Before
    public void setUp() {
        this.attendant = new DefaultAttendant();
        this.library = new DefaultLibrary();
        this.attendant.setLibrary(this.library);
    }

    /** Tests for attempting to load an unknown library file. */
    @Test
    public void testUnknownLibraryFile() {
        String cardString = "A include from library cards for bogus";
        Card card = null;
        try {
            card = CardParser.toCard(cardString);
        } catch (UnknownCard exception) {
            TestUtils.fail(exception);
        }
        List<Card> program = Arrays.asList(card);
        try {
            this.attendant.loadProgram(program);
            TestUtils.shouldHaveThrownException();
        } catch (BadCard | IOException | UnknownCard exception) {
            TestUtils.fail(exception);
        } catch (LibraryLookupException exception) {
            assertTrue(true);
        }
    }

    /**
     * Tests that a library function with a bad card in it causes an exception.
     * 
     * @throws IOException
     *             If there is a problem creating the temporary file.
     */
    @Test
    public void testLibraryBadCard() throws IOException {
        // Create a temporary directory to add to the library.
        Path tempDir = Files.createTempDirectory(null);
        this.library.addLibraryPath(tempDir);

        // Create a temporary file in the directory created above that contains
        // a bad card (that is, a bad instruction for the Analytical Engine).
        Path tempFile = Files.createTempFile(tempDir, null, ".ae");

        // Ensure the temporary files and directories are deleted when the JVM
        // exits.
        tempFile.toFile().deleteOnExit();
        tempDir.toFile().deleteOnExit();

        // Write a program in the temporary file that contains a bad card.
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile,
                Charset.defaultCharset())) {
            writer.write("XXX\n");
        }

        // Create a new program that tries to include the library file
        // containing the bad card.
        Path baseName = tempFile.getFileName();
        String cardString = "A include from library cards for " + baseName;
        Card card = null;
        try {
            card = CardParser.toCard(cardString);
        } catch (UnknownCard exception) {
            TestUtils.fail(exception);
        }
        List<Card> cards = Arrays.asList(card);
        try {
            this.attendant.loadProgram(cards);
            TestUtils.shouldHaveThrownException();
        } catch (BadCard |UnknownCard exception) {
            TestUtils.fail(exception);
        } catch (LibraryLookupException exception) {
            assertTrue(true);
        }
    }
}
