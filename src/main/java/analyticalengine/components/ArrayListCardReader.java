package analyticalengine.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import analyticalengine.components.cards.Card;

public class ArrayListCardReader implements CardReader {

    private List<Card> cards = new ArrayList<Card>();
    // always points to the index of the card before the next card to read
    private int currentCard = -1;
    
    @Override
    public Card readAndAdvance() {
        this.advance(1);
        return this.cards.get(this.currentCard);
    }

    @Override
    public void mountCards(List<Card> cards) {
        // mount a copy of the cards
        Collections.copy(this.cards, cards);
    }

    @Override
    public void unmountCards() {
        this.currentCard = -1;
        this.cards.clear();
    }

    @Override
    public void advance(int n) {
        if (this.currentCard + n >= this.cards.size()) {
            throw new IndexOutOfBoundsException("No more cards to read.");
        }
        this.currentCard += n;
    }

    @Override
    public void reverse(int n) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<Card> cards() {
        return Collections.unmodifiableList(this.cards);
    }

}
