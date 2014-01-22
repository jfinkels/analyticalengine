package analyticalengine.components;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardChain;
import analyticalengine.newio.ArrayListCardChain;

public class ArrayListCardReader implements CardReader {

    private CardChain cards = new ArrayListCardChain();
    // always points to the index of the card before the next card to read
    private int currentCard = -1;

    @Override
    public void advance(int n) throws IndexOutOfBoundsException {
        if (this.currentCard + n >= this.cards.size()) {
            throw new IndexOutOfBoundsException("No more cards to read.");
        }
        this.currentCard += n;
    }

//    @Override
//    public CardChain cards() {
//        return Collections.unmodifiableList(this.cards);
//    }

    @Override
    public void mountCards(CardChain cards) {
        // mount a copy of the cards
        this.cards = cards.shallowCopy();
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
