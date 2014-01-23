package analyticalengine.newmain;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import analyticalengine.components.AnalyticalEngine;
import analyticalengine.components.ArrayListCardReader;
import analyticalengine.components.Attendant;
import analyticalengine.components.BadCard;
import analyticalengine.components.CardReader;
import analyticalengine.components.CurvePrinter;
import analyticalengine.components.DefaultAnalyticalEngine;
import analyticalengine.components.DefaultAttendant;
import analyticalengine.components.DefaultMill;
import analyticalengine.components.HashMapStore;
import analyticalengine.components.Mill;
import analyticalengine.components.Printer;
import analyticalengine.components.Store;
import analyticalengine.components.StringPrinter;
import analyticalengine.components.cards.Card;
import analyticalengine.components.gui.AWTCurvePrinter;

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
            cards = analyticalengine.newio.CardReader.fromPath(program);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            attendant.loadProgram(cards);
        } catch (BadCard | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Finally, run the Analytical Engine with the specified program.
        engine.run();
    }
}
