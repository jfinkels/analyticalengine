package analyticalengine.newio;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analyticalengine.TestUtils;
import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardType;

public class CardReaderTest {

    public static final String TESTFILE = "src/test/resources/analyticalengine/components/ex0.ae";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFromFile() {
        // A write in rows
        // A write numbers as 9
        // N120 10000
        // N121 3
        // /
        // L120
        // L121
        // S122'
        // P
        List<Card> expected = Arrays.asList(new Card(CardType.WRITEROWS),
                new Card(CardType.WRITEPICTURE, new String[] { "9" }),
                new Card(CardType.NUMBER, new String[] { "120", "10000" }),
                new Card(CardType.NUMBER, new String[] { "121", "3" }),
                new Card(CardType.DIVIDE), new Card(CardType.LOAD,
                        new String[] { "120" }), new Card(CardType.LOAD,
                        new String[] { "121" }), new Card(CardType.STOREPRIME,
                        new String[] { "122" }), new Card(CardType.PRINT));

        try {
            List<Card> cards = CardReader.fromFile(new File(TESTFILE));
            assertEquals(9, cards.size());
            for (int i = 0; i < cards.size(); i++) {
                assertEquals(expected.get(i).type(), cards.get(i).type());
                assertArrayEquals(expected.get(i).arguments(), cards.get(i)
                        .arguments());
            }
        } catch (IOException | UnknownCard e) {
            TestUtils.fail(e);
        }
    }

    @Test
    public void testFromString() {
        fail("Not yet implemented");
    }

}
