/**
 * Main.java - driver program that interprets and runs engine programs
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import analyticalengine.AnalyticalEngine;
import analyticalengine.BadCard;
import analyticalengine.DefaultAnalyticalEngine;
import analyticalengine.attendant.Attendant;
import analyticalengine.attendant.DefaultAttendant;
import analyticalengine.attendant.DefaultLibrary;
import analyticalengine.attendant.Library;
import analyticalengine.attendant.LibraryLookupException;
import analyticalengine.cards.Card;
import analyticalengine.cards.UnknownCard;
import analyticalengine.components.ArrayListCardReader;
import analyticalengine.components.CardReader;
import analyticalengine.components.DefaultMill;
import analyticalengine.components.HashMapStore;
import analyticalengine.components.NullCurvePrinter;
import analyticalengine.components.StringPrinter;
import analyticalengine.gui.JFrameCurvePrinter;

/**
 * A command-line driver for the Analytical Engine simulation.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public final class Main {

    /** Instantiation disallowed. */
    private Main() {
        // intentionally unimplemented
    }

    /**
     * The logger for this class.
     */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(Main.class);

    /**
     * The main method for the command-line program.
     * 
     * @param argv
     *            The command-line arguments to the program.
     */
    public static void main(final String[] argv) {
        Arguments arguments = new Arguments();
        JCommander argparser = new JCommander(arguments, argv);

        if (arguments.help() || arguments.args().size() != 1) {
            argparser.usage();
            return;
        }

        if (arguments.verbosity() == 1) {
            // TODO increase debugging level to info
            LOG.debug("Requested verbosity 1; not yet implemented.");
        } else if (arguments.verbosity() == 2) {
            // TODO increase debugging level to debug
            LOG.debug("Requested verbosity 2; not yet implemented.");
        }

        // Create and hook up the components of the engine.
        //
        // The engine has a mill (ALU), a store (memory), a card reader
        // (instructions), a printer, a curve printer (plotter), and an
        // attendant (operator).
        //
        // The attendant has access to a library of built-in functions, and
        // access to the card reader used by the engine.
        Attendant attendant = new DefaultAttendant();
        CardReader cardReader = new ArrayListCardReader();
        attendant.setCardReader(cardReader);
        Library library = new DefaultLibrary();
        attendant.setLibrary(library);

        // apply any attendant-specific configuration from command-line args
        library.addLibraryPaths(arguments.libraryPath());
        // always search the current directory as well
        library.addLibraryPath(Paths.get("."));
        attendant.setStripComments(arguments.stripComments());

        AnalyticalEngine engine = new DefaultAnalyticalEngine();
        engine.setAttendant(attendant);
        engine.setCardReader(cardReader);
        engine.setMill(new DefaultMill());
        engine.setPrinter(new StringPrinter());
        engine.setStore(new HashMapStore());
        // if this is a headless execution, ignore curve printer commands
        if (arguments.headless()) {
            engine.setCurvePrinter(new NullCurvePrinter());
        } else {
            engine.setCurvePrinter(new JFrameCurvePrinter());
        }

        // load the file specified in the command-line argument
        Path program = Paths.get(arguments.args().get(0));
        List<Card> cards = new ArrayList<Card>();
        try {
            for (String line : Files.readAllLines(program)) {
                cards.add(Card.fromString(line));
            }
        } catch (IOException e) {
            LOG.error("Could not open file", e);
            return;
        } catch (UnknownCard e) {
            LOG.error("Unknown card", e);
            return;
        }

        // instruct the attendant to load the card chain into the machine
        try {
            attendant.loadProgram(cards);
        } catch (BadCard e) {
            LOG.error("Attendant encountered bad card", e);
            return;
        } catch (IOException e) {
            LOG.error("Attendant could not locate library file", e);
            return;
        } catch (UnknownCard e) {
            LOG.error("Included file contains unknown card", e);
            return;
        } catch (LibraryLookupException e) {
            LOG.error("Attendant failed to load library file", e);
            return;
        }

        if (arguments.listOnly()) {
            try {
                for (Card card : cardReader.cards()) {
                    System.out.println(card.toText());
                }
            } catch (UnknownCard e) {
                LOG.error("Encountered unknown card", e);
            }
            return;
        }

        // finally, run the analytical engine with the specified program
        try {
            engine.run();
        } catch (BadCard e) {
            LOG.error("Encountered invalid card", e);
            return;
        }

        // print the attendant's report to standard output
        System.out.println(attendant.finalReport());
    }
}
