package analyticalengine.newmain;

import java.io.IOException;

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
import analyticalengine.components.cards.CardChain;
import analyticalengine.components.gui.AWTCurvePrinter;

public class Main {
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

        CardChain cards = null;
        try {
            // TODO real argument parsing
            cards = analyticalengine.newio.CardReader.fromFilename(args[1]);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            attendant.loadProgram(cards);
        } catch (BadCard e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        engine.run();
    }
}
