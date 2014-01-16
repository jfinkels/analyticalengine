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

    void run() {
        // ...
    }
}
