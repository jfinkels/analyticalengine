/**
 * AttendantTestBase.java - base class for tests that use an Analytical Engine
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import analyticalengine.cards.BadCard;
import analyticalengine.cards.Card;
import analyticalengine.cards.UnknownCard;
import analyticalengine.components.ArrayListCardReader;
import analyticalengine.components.CardReader;

/**
 * Base class for tests that require an Attendant.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class AttendantTestBase {

    /** The attendant required to operate the Analytical Engine. */
    private Attendant attendant = null;

    /** The library used by the attendant for including requested functions. */
    private Library library = null;

    /** The card reader that the attendant accesses. */
    private CardReader reader = null;

    /**
     * Gets the attendant that operates the Analytical Engine under test.
     * 
     * @return The operator of the Analytical Engine under test.
     */
    protected Attendant attendant() {
        return this.attendant;
    }

    /**
     * Returns the library used by the attendant.
     * 
     * @return The library used by the attendant.
     */
    protected Library library() {
        return this.library;
    }

    /**
     * Loads the program stored in the specified filename.
     * 
     * @param filename
     *            The name of the file to test, relative to the test resources
     *            directory.
     * @throws URISyntaxException
     *             if there is a problem loading the file.
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    protected void loadProgram(final String filename)
            throws URISyntaxException, IOException, UnknownCard, BadCard,
            LibraryLookupException {
        Path program = Paths
                .get(this.getClass().getResource("/" + filename).toURI());
        List<Card> cards = new ArrayList<Card>();
        for (String line : Files.readAllLines(program)) {
            cards.add(Card.fromString(line));
        }
        this.attendant.loadProgram(cards);
    }

    /**
     * Loads the program (sequence of card strings) given in the specified
     * string.
     * 
     * @param program
     *            The program to load.
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    protected void loadProgramString(final String program)
            throws UnknownCard, BadCard, IOException, LibraryLookupException {
        List<Card> cards = new ArrayList<Card>();
        for (String line : program.split(System.lineSeparator())) {
            cards.add(Card.fromString(line));
        }
        this.attendant().loadProgram(cards);
    }

    /**
     * Returns the card reader that the attendant accesses.
     * 
     * @return The card reader that the attendant accesses.
     */
    public CardReader reader() {
        return this.reader;
    }

    /** Creates the Analytical Engine to test. */
    @Before
    public void setUp() {
        this.attendant = new DefaultAttendant();
        this.library = new DefaultLibrary();
        this.reader = new ArrayListCardReader();

        this.attendant.setCardReader(this.reader);
        this.attendant.setLibrary(this.library);
    }

    /** Resets the attendant being tested. */
    @After
    public void tearDown() {
        // this technically doesn't need to be done, since we are creating new
        // objects in set up code anyway
        this.attendant.reset();
    }
}
