/**
 * EngineTestBase.java - base class for tests that use an Analytical Engine
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
package analyticalengine;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.Before;

import analyticalengine.attendant.AttendantTestBase;
import analyticalengine.attendant.LibraryLookupException;
import analyticalengine.cards.BadCard;
import analyticalengine.cards.UnknownCard;
import analyticalengine.components.DefaultMill;
import analyticalengine.components.HashMapStore;
import analyticalengine.components.Mill;
import analyticalengine.components.NullCurvePrinter;
import analyticalengine.components.Store;
import analyticalengine.components.StringPrinter;

/**
 * Base class for tests that use an Analytical Engine.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class EngineTestBase extends AttendantTestBase {

    /** The Analytical Engine instance to test. */
    private AnalyticalEngine engine = null;
    /** The mill used by the Analytical Engine. */
    private Mill mill = null;
    /** The store used by the Analytical Engine. */
    private Store store = null;

    /**
     * Gets the Analytical Engine object used for testing.
     * 
     * @return The Analytical Engine used for testing.
     */
    protected AnalyticalEngine engine() {
        return this.engine;
    }

    /**
     * Gets the mill of the Analytical Engine.
     * 
     * @return The mill of the Analytical Engine.
     */
    protected Mill mill() {
        return this.mill;
    }

    /**
     * Runs the program stored in the specified filename.
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
    protected void runProgram(final String filename) throws BadCard, URISyntaxException, IOException, UnknownCard, LibraryLookupException {
        this.loadProgram(filename);
        this.engine.run();
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
    protected void runProgramString(final String program)
            throws UnknownCard, BadCard, IOException, LibraryLookupException {
        this.loadProgramString(program);
        this.engine.run();
    }

    /** Creates the Analytical Engine to test. */
    @Before
    @Override
    public void setUp() {
        super.setUp();
        this.engine = new DefaultAnalyticalEngine();
        this.engine.setAttendant(this.attendant());
        this.engine.setCardReader(this.reader());

        this.store = new HashMapStore();
        this.mill = new DefaultMill();
        this.engine.setStore(this.store);
        this.engine.setMill(this.mill);
        this.engine.setCurvePrinter(new NullCurvePrinter());
        this.engine.setPrinter(new StringPrinter());
    }

    /**
     * Gets the store used by the Analytical Engine.
     * 
     * @return The store used by the Analytical Engine.
     */
    protected Store store() {
        return this.store;
    }

    /** Resets the Analytical Engine being tested. */
    @After
    @Override
    public void tearDown() {
        // this technically doesn't need to be done, since we are creating new
        // objects in set up code anyway
        super.tearDown();
        this.engine.reset();
    }
}
