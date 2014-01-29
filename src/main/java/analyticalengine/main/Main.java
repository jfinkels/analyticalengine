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

import analyticalengine.AWTCurvePrinter;
import analyticalengine.AnalyticalEngine;
import analyticalengine.ArrayListCardReader;
import analyticalengine.Attendant;
import analyticalengine.BadCard;
import analyticalengine.Card;
import analyticalengine.CardReader;
import analyticalengine.CurvePrinter;
import analyticalengine.DefaultAnalyticalEngine;
import analyticalengine.DefaultAttendant;
import analyticalengine.DefaultMill;
import analyticalengine.HashMapStore;
import analyticalengine.Mill;
import analyticalengine.Printer;
import analyticalengine.Store;
import analyticalengine.StringPrinter;
import analyticalengine.io.UnknownCard;

public class Main {
//    static void usage() {
//        up("Options:");
//        up("  -c    Don't mount comment cards");
//        up("  -l    List program as mounted by attendant");
//        up("  -n    No execution of program");
//        up("  -p    Punch copy of program as mounted by attendant");
//        up("  -sLST Use LST as library search template");
//        up("  -t    Print trace of program execution");
//        up("  -u    Print this message");
//    }
    
    public static void main(String[] args) {
        AnalyticalEngine engine = new DefaultAnalyticalEngine();
        Attendant attendant = new DefaultAttendant();
        CardReader reader = new ArrayListCardReader();
        CurvePrinter curvePrinter = new AWTCurvePrinter();
        Mill mill = new DefaultMill();
        Printer printer = new StringPrinter();
        Store store = new HashMapStore();

        attendant.setCardReader(reader);

        engine.setAttendant(attendant);
        engine.setCardReader(reader);
        engine.setCurvePrinter(curvePrinter);
        engine.setMill(mill);
        engine.setPrinter(printer);
        engine.setStore(store);

        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            // TODO real argument parsing
            Path program = Paths.get(args[1]);
            cards = analyticalengine.io.CardReader.fromPath(program);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownCard e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            attendant.loadProgram(cards);
        } catch (BadCard e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnknownCard e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Finally, run the Analytical Engine with the specified program.
        engine.run();
    }
}
