package analyticalengine.newio;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resources;

import org.junit.Test;

import analyticalengine.TestUtils;
import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardType;

public class CardReaderTest {

    public static final String TESTFILE = "src/test/resources/analyticalengine/components/ex0.ae";

    // A write in rows
    // A write numbers as 9
    // N120 10000
    // N121 3
    // /
    // L120
    // L121
    // S122'
    // P
    public static List<Card> EXPECTED = Arrays.asList(new Card(
            CardType.WRITEROWS), new Card(CardType.WRITEPICTURE,
            new String[] { "9" }), new Card(CardType.NUMBER, new String[] {
            "120", "10000" }), new Card(CardType.NUMBER, new String[] { "121",
            "3" }), new Card(CardType.DIVIDE), new Card(CardType.LOAD,
            new String[] { "120" }), new Card(CardType.LOAD,
            new String[] { "121" }), new Card(CardType.STOREPRIME,
            new String[] { "122" }), new Card(CardType.PRINT));

    private static void assertCardsEqual(List<Card> expected, List<Card> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            Card expectedCard = expected.get(i);
            Card actualCard = actual.get(i);
            assertEquals(expectedCard.type(), actualCard.type());
            assertEquals(expectedCard.numArguments(),
                    actualCard.numArguments());
            for (int j = 0; j < actualCard.numArguments(); j++) {
                assertEquals(expectedCard.argument(j), actualCard.argument(j));
            }
        }

    }

    @Test
    public void testFromFile() {
        try {
            List<Card> cards = CardReader.fromFile(new File(TESTFILE));
            assertCardsEqual(EXPECTED, cards);
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }
    }

    @Test
    public void testFromString() {
        String program = "A write in rows\n" + "A write numbers as 9\n"
                + "N120 10000\n" + "N121 3\n" + "/\n" + "L120\n" + "L121\n"
                + "S122'\n" + "P\n";
        try {
            List<Card> cards = CardReader.fromString(program);
            assertCardsEqual(EXPECTED, cards);
        } catch (UnknownCard e) {
            TestUtils.fail(e);
        }
    }

}
