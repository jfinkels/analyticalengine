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
    public void advance(int n) throws IndexOutOfBoundsException {
        if (this.currentCard + n >= this.cards.size()) {
            throw new IndexOutOfBoundsException("No more cards to read.");
        }
        this.currentCard += n;
    }

    @Override
    public List<Card> cards() {
        return Collections.unmodifiableList(this.cards);
    }

    @Override
    public void mountCards(List<Card> cards) {
        // mount a copy of the cards
        Collections.copy(this.cards, cards);
    }

    @Override
    public Card readAndAdvance() throws Halt {
        if (this.currentCard + 1 == this.cards.size()) {
            throw new Halt("Reached end of card chain.");
        }
        this.currentCard += 1;
        return this.cards.get(this.currentCard);
    }

    @Override
    public void reverse(int n) throws IndexOutOfBoundsException {
        if (this.currentCard - n < -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot reverse beyond beginning.");
        }
        this.currentCard -= n;
    }

    @Override
    public void unmountCards() {
        this.currentCard = -1;
        this.cards.clear();
    }

}
