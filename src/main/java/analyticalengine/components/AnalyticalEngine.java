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

    private void executeCard(Card card) throws Bell, Halt{
        // TODO here is where we create an entry for each instruction in the
        // instruction set
        switch (card.type()) {

        }
    }

    void run() {
        try {
            Card currentCard = this.cardReader.readAndAdvance();
            while (currentCard != null) {
                try {
                    this.executeCard(currentCard);
                } catch (Bell bell) {
                    // TODO do something
                }
                currentCard = this.cardReader.readAndAdvance();
            }
        } catch (Halt halt) {
            // TODO do something
        } catch (IndexOutOfBoundsException exception) {
            // TODO do something
        }
    }
}
