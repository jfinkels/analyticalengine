package analyticalengine.components;

import java.util.List;

import analyticalengine.components.cards.Card;

class AnalyticalEngine {
    private Mill mill = null;
    private Store store = null;
    private CardReader cardReader = null;
    private Annunciator annunciator = null;

    void inputCards(List<Card> cards) {
        this.cardReader.mountCards(cards);
    }

    private void executeCard(Card card) throws Bell, Halt {
        // TODO here is where we create an entry for each instruction in the
        // instruction set
        switch (card.type()) {
        case ADD:
            this.mill.setOperation(Operation.ADD);
            break;
        case ADVANCE:
            break;
        case ANNOTATE:
            break;
        case BELL:
            break;
        case COMMENT:
            break;
        case DECIMALEXPAND:
            break;
        case DIVIDE:
            this.mill.setOperation(Operation.DIVIDE);
            break;
        case DRAWSETX:
            break;
        case DRAWSETY:
            break;
        case DRAWTO:
            break;
        case HALT:
            break;
        case INCLUDE:
            break;
        case LOAD:
            break;
        case LOADPRIME:
            break;
        case LSHIFT: {
            int shift = Integer.parseInt(card.argument(0));
            if (shift < 0 || shift > 100) {
                throw new Error("Bad stepping up card");
            }
            this.mill.leftShift(shift);
        }
            break;
        case MOVETO:
            break;
        case MULTIPLY:
            this.mill.setOperation(Operation.MULTIPLY);
            break;
        case NUMBER:
            break;
        case PRINT:
            break;
        case REVERSE:
            break;
        case RSHIFT: {
            int shift = Integer.parseInt(card.argument(0));
            if (shift < 0 || shift > 100) {
                throw new Error("Bad stepping up card");
            }
            this.mill.rightShift(Integer.parseInt(card.argument(0)));
        }
            break;
        case STORE:
            break;
        case STOREPRIME:
            break;
        case SUBTRACT:
            this.mill.setOperation(Operation.SUBTRACT);
            break;
        case TRACE:
            break;
        case ZLOAD:
            break;
        case ZLOADPRIME:
            break;
        default:
            break;

        }
    }

    void run() {
        try {
            while (true) {
                Card currentCard = this.cardReader.readAndAdvance();
                try {
                    this.executeCard(currentCard);
                } catch (Bell bell) {
                    // TODO do something
                }
            }
        } catch (Halt halt) {
            // TODO do something
        } catch (IndexOutOfBoundsException exception) {
            // TODO do something
        }
    }
}
