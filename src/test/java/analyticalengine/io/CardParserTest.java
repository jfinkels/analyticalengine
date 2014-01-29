package analyticalengine.io;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import analyticalengine.CardType;
import analyticalengine.TestUtils;
import analyticalengine.io.CardParser;
import analyticalengine.io.UnknownCard;

public class CardParserTest {

    @Test
    public void testToCard() {
        try {
            assertEquals(CardType.ADD, CardParser.toCard("+").type());
            assertEquals(CardType.SUBTRACT, CardParser.toCard("-").type());
            assertEquals(CardType.MULTIPLY, CardParser.toCard("*").type());
            assertEquals(CardType.DIVIDE, CardParser.toCard("/").type());

            assertEquals(CardType.CFORWARD, CardParser.toCard("CF?").type());
            assertEquals(CardType.FORWARD, CardParser.toCard("CF+").type());
            assertEquals(CardType.CBACKWARD, CardParser.toCard("CB?").type());
            assertEquals(CardType.BACKWARD, CardParser.toCard("CB+").type());

            assertEquals(CardType.BELL, CardParser.toCard("B").type());
            assertEquals(CardType.PRINT, CardParser.toCard("P").type());
            assertEquals(CardType.HALT, CardParser.toCard("H").type());

            assertEquals(CardType.SETX, CardParser.toCard("DX").type());
            assertEquals(CardType.SETY, CardParser.toCard("DY").type());
            assertEquals(CardType.DRAW, CardParser.toCard("D+").type());
            assertEquals(CardType.MOVE, CardParser.toCard("D-").type());

            assertEquals(CardType.NUMBER, CardParser.toCard("N123 456").type());

            assertEquals(CardType.LOAD, CardParser.toCard("L123").type());
            assertEquals(CardType.LOADPRIME, CardParser.toCard("L123'").type());
            assertEquals(CardType.STORE, CardParser.toCard("S123").type());
            assertEquals(CardType.STOREPRIME, CardParser.toCard("S123'")
                    .type());
            assertEquals(CardType.ZLOAD, CardParser.toCard("Z123").type());
            assertEquals(CardType.ZLOADPRIME, CardParser.toCard("Z123'")
                    .type());

            assertEquals(CardType.LSHIFT, CardParser.toCard("<").type());
            assertEquals(CardType.RSHIFT, CardParser.toCard(">").type());
            assertEquals(CardType.LSHIFTN, CardParser.toCard("<9").type());
            assertEquals(CardType.RSHIFTN, CardParser.toCard(">9").type());

            assertEquals(CardType.BACKSTART, CardParser.toCard("(").type());
            assertEquals(CardType.CBACKSTART, CardParser.toCard("(?").type());
            assertEquals(CardType.BACKEND, CardParser.toCard(")").type());
            assertEquals(CardType.FORWARDSTART, CardParser.toCard("{").type());
            assertEquals(CardType.CFORWARDSTART, CardParser.toCard("{?")
                    .type());
            assertEquals(CardType.FORWARDEND, CardParser.toCard("}").type());
            assertEquals(CardType.ALTERNATION, CardParser.toCard("}{").type());

            assertEquals(CardType.COMMENT, CardParser.toCard(". foo").type());
            assertEquals(CardType.COMMENT, CardParser.toCard("  foo").type());

            assertEquals(CardType.TRACEON, CardParser.toCard("T1").type());
            assertEquals(CardType.TRACEOFF, CardParser.toCard("T0").type());

            assertEquals(CardType.INCLUDE,
                    CardParser.toCard("A include cards foo.ae").type());
            assertEquals(CardType.INCLUDELIB,
                    CardParser.toCard("A include from library cards for sin")
                            .type());

            assertEquals(CardType.DECIMALEXPAND,
                    CardParser.toCard("A set decimal places to 9").type());

            assertEquals(CardType.WRITEPICTURE,
                    CardParser.toCard("A write numbers as 9.999").type());
            assertEquals(CardType.WRITEDECIMAL,
                    CardParser.toCard("A write numbers with decimal point")
                            .type());

            assertEquals(CardType.WRITEROWS,
                    CardParser.toCard("A write in rows").type());
            assertEquals(CardType.WRITECOLUMNS,
                    CardParser.toCard("A write in columns").type());
            assertEquals(CardType.ANNOTATE,
                    CardParser.toCard("A write annotation foo").type());
            assertEquals(CardType.NEWLINE,
                    CardParser.toCard("A write new line").type());
        } catch (UnknownCard e) {
            TestUtils.fail(e);
        }

        try {
            CardParser.toCard("$ bogus card");
            TestUtils.shouldHaveThrownException();
        } catch (UnknownCard e) {
            // intentionally unimplemented
        }
    }
}
