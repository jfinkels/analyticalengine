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
            List<Card> cards = analyticalengine.io.CardReader
                    .fromPath(program);
            this.attendant.loadProgram(cards);
            this.engine.run();
        } catch (BadCard | IOException | UnknownCard | URISyntaxException | LibraryLookupException e) {
            TestUtils.fail(e);
        }
    }

    /**
     * Runs the program (sequence of card strings) given in the specified
     * string.
     * 
     * @param program
     *            The program to run.
     */
    void runProgramString(final String program) {
        try {
            List<Card> cards = analyticalengine.io.CardReader
                    .fromString(program);
            this.attendant.loadProgram(cards);
            this.engine.run();
        } catch (BadCard | IOException | UnknownCard | LibraryLookupException e) {
            TestUtils.fail(e);
        }
    }

    /** Creates the Analytical Engine to test. */
    @Before
    public void setUp() {
        this.engine = new DefaultAnalyticalEngine();
        this.attendant = new DefaultAttendant();
        Library library = new DefaultLibrary();
        CardReader reader = new ArrayListCardReader();
        CurvePrinter curvePrinter = new AWTCurvePrinter();
        Mill mill = new DefaultMill();
        Printer printer = new StringPrinter();
        Store store = new HashMapStore();

        this.attendant.setCardReader(reader);
        this.attendant.setLibrary(library);
        
        this.engine.setAttendant(this.attendant);
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
}
