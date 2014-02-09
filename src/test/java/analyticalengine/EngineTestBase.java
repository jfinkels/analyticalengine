/**
 * EngineTestBase.java - base class for tests that use an Analytical Engine
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import analyticalengine.cards.Card;
import analyticalengine.io.ProgramReader;
import analyticalengine.io.UnknownCard;

/**
 * Base class for tests that use an Analytical Engine.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class EngineTestBase {

    /** The attendant required to operate the Analytical Engine. */
    private Attendant attendant = null;
    /** The Analytical Engine instance to test. */
    private AnalyticalEngine engine = null;
    /** The mill used by the Analytical Engine. */
    private Mill mill = null;
    /** The store used by the Analytical Engine. */
    private Store store = null;

    /**
     * Gets the attendant that operates the Analytical Engine under test.
     * 
     * @return The operator of the Analytical Engine under test.
     */
    Attendant attendant() {
        return this.attendant;
    }

    /**
     * Gets the Analytical Engine object used for testing.
     * 
     * @return The Analytical Engine used for testing.
     */
    AnalyticalEngine engine() {
        return this.engine;
    }

    /**
     * Gets the mill of the Analytical Engine.
     * 
     * @return The mill of the Analytical Engine.
     */
    Mill mill() {
        return this.mill;
    }

    /**
     * Runs the program stored in the specified filename.
     * 
     * @param filename
     *            The name of the file to test, relative to the test resources
     *            directory.
     */
    void runProgram(final String filename) {
        try {
            Path program = Paths.get(this.getClass()
                    .getResource("/" + filename).toURI());
            List<Card> cards = ProgramReader.fromPath(program);
            this.attendant.loadProgram(cards);
            this.engine.run();
        } catch (BadCard | IOException | UnknownCard | URISyntaxException
                | LibraryLookupException e) {
            TestUtils.fail(e);
        }
    }

    /**
     * Runs the program (sequence of card strings) given in the specified
     * string.
     * 
     * @param program
     *            The program to run.
     * @throws LibraryLookupException
     *             if there is a problem included a built-in library function.
     * @throws IOException
     *             if there is a problem reading a list of cards from a file.
     * @throws UnknownCard
     *             if the specified program includes an unknown card.
     * @throws BadCard
     *             if there is a syntax error on one of the cards.
     */
    void runProgramString(final String program) throws UnknownCard, BadCard,
            IOException, LibraryLookupException {
        List<Card> cards = ProgramReader.fromString(program);
        this.attendant.loadProgram(cards);
        this.engine.run();
    }

    /** Creates the Analytical Engine to test. */
    @Before
    public void setUp() {
        this.attendant = new DefaultAttendant();
        Library library = new DefaultLibrary();
        CardReader reader = new ArrayListCardReader();

        this.attendant.setCardReader(reader);
        this.attendant.setLibrary(library);

        this.engine = new DefaultAnalyticalEngine();
        this.engine.setAttendant(this.attendant);
        this.engine.setCardReader(reader);

        this.store = new HashMapStore();
        this.mill = new DefaultMill();
        this.engine.setStore(this.store);
        this.engine.setMill(this.mill);
        this.engine.setCurvePrinter(new AWTCurvePrinter());
        this.engine.setPrinter(new StringPrinter());
    }

    /**
     * Gets the store used by the Analytical Engine.
     * 
     * @return The store used by the Analytical Engine.
     */
    Store store() {
        return this.store;
    }

    /** Resets the Analytical Engine being tested. */
    @After
    public void tearDown() {
        // this technically doesn't need to be done, since we are creating new
        // objects in set up code anyway
        this.attendant.reset();
        this.engine.reset();
    }
}
