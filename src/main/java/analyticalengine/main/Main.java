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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.AWTCurvePrinter;
import analyticalengine.AnalyticalEngine;
import analyticalengine.ArrayListCardReader;
import analyticalengine.Attendant;
import analyticalengine.BadCard;
import analyticalengine.CardReader;
import analyticalengine.CurvePrinter;
import analyticalengine.DefaultAnalyticalEngine;
import analyticalengine.DefaultAttendant;
import analyticalengine.DefaultLibrary;
import analyticalengine.DefaultMill;
import analyticalengine.HashMapStore;
import analyticalengine.Library;
import analyticalengine.LibraryLookupException;
import analyticalengine.Mill;
import analyticalengine.Printer;
import analyticalengine.Store;
import analyticalengine.StringPrinter;
import analyticalengine.cards.Card;
import analyticalengine.io.ProgramReader;
import analyticalengine.io.UnknownCard;

import com.beust.jcommander.JCommander;

/**
 * A command-line driver for the Analytical Engine simulation.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
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

        if (arguments.help || arguments.args.size() != 1) {
            argparser.usage();
            return;
        }

        if (arguments.verbosity == 1) {
            // TODO increase debugging level to info
        } else if (arguments.verbosity == 2) {
            // TODO increase debugging level to debug
        }

        // create the analytical engine and necessary components
        AnalyticalEngine engine = new DefaultAnalyticalEngine();
        Attendant attendant = new DefaultAttendant();
        CardReader reader = new ArrayListCardReader();
        CurvePrinter curvePrinter = new AWTCurvePrinter();
        Library library = new DefaultLibrary();
        Mill mill = new DefaultMill();
        Printer printer = new StringPrinter();
        Store store = new HashMapStore();

        // hook up the components of the engine
        attendant.setCardReader(reader);
        attendant.setLibrary(library);

        engine.setAttendant(attendant);
        engine.setCardReader(reader);
        engine.setCurvePrinter(curvePrinter);
        engine.setMill(mill);
        engine.setPrinter(printer);
        engine.setStore(store);

        // apply any configuration specified on command line
        library.addLibraryPaths(arguments.libraryPath);
        // always search the current directory as well
        library.addLibraryPath(Paths.get("."));
        attendant.setStripComments(arguments.stripComments);

        // load the file specified in the command-line argument
        List<Card> cards;
        try {
            Path program = Paths.get(arguments.args.get(0));
            cards = ProgramReader.fromPath(program);
        } catch (IOException e) {
            LOG.error("Failed to load specified program", e);
            return;
        } catch (UnknownCard e) {
            LOG.error("Unknown card in file", e);
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

        if (arguments.listOnly) {
            for (Card card : reader.cards()) {
                System.out.println(card);
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
