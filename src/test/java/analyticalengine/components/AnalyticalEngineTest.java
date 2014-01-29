package analyticalengine.components;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.TestUtils;
import analyticalengine.components.cards.Card;
import analyticalengine.components.gui.AWTCurvePrinter;
import analyticalengine.newio.UnknownCard;

public class AnalyticalEngineTest {

    private AnalyticalEngine engine = null;
    private Attendant attendant = null;

    @Before
    public void setUp() throws Exception {
        this.engine = new DefaultAnalyticalEngine();
        this.attendant = new DefaultAttendant();
        CardReader reader = new ArrayListCardReader();
        CurvePrinter curvePrinter = new AWTCurvePrinter();
        Mill mill = new DefaultMill();
        Printer printer = new StringPrinter();
        Store store = new HashMapStore();

        this.attendant.setCardReader(reader);

        this.engine.setAttendant(attendant);
        this.engine.setCardReader(reader);
        this.engine.setCurvePrinter(curvePrinter);
        this.engine.setMill(mill);
        this.engine.setPrinter(printer);
        this.engine.setStore(store);
    }

    @After
    public void tearDown() throws Exception {
        // this technically doesn't need to be done, since we are creating new
        // objects in set up code anyway
        this.attendant.reset();
        this.engine.reset();
    }

    @Test
    public void testRun() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/components/ex0.ae");
            cards = analyticalengine.newio.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        // The final report should indicate a 3333, according to the program.
        assertEquals("3333", this.attendant.finalReport());
    }

    @Test
    public void testDivide() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/components/ex1.ae");
            cards = analyticalengine.newio.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        // The final report should indicate a "357\n4"
        assertEquals("357" + System.lineSeparator() + "4",
                this.attendant.finalReport());
    }


    @Test
    public void testShifts() {
        // First, load the cards specified in the filename given as an
        // argument.
        List<Card> cards = null;
        try {
            Path program = Paths
                    .get("src/test/resources/analyticalengine/components/ex2.ae");
            cards = analyticalengine.newio.CardReader.fromPath(program);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Second, instruct the attendant to load the card chain into the
        // machine.
        try {
            this.attendant.loadProgram(cards);
        } catch (BadCard | IOException | UnknownCard e) {
            TestUtils.fail(e);
        }

        // Finally, run the Analytical Engine with the specified program.
        this.engine.run();

        assertEquals("357142857" + System.lineSeparator() + "4000000",
                this.attendant.finalReport());
    }

    
}
