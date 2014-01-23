package analyticalengine.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import analyticalengine.components.cards.Card;

public class ArrayListCardReader implements CardReader {

    private List<Card> cardChain = new ArrayList<Card>();
    // always points to the index of the card before the next card to read
    private int currentCard = -1;

    @Override
    public void advance(int n) throws IndexOutOfBoundsException {
        if (this.currentCard + n >= this.cardChain.size()) {
            throw new IndexOutOfBoundsException("No more cards to read.");
        }
        this.currentCard += n;
    }

//    @Override
//    public CardChain cards() {
//        return Collections.unmodifiableList(this.cards);
//    }

    @Override
    public void mountCards(List<Card> cardChain) {
        Collections.copy(this.cardChain, cardChain);
    }

    @Override
    public Card readAndAdvance() throws Halt {
        if (this.currentCard + 1 == this.cardChain.size()) {
            throw new Halt("Reached end of card chain.");
        }
        this.currentCard += 1;
        return this.cardChain.get(this.currentCard);
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
        this.cardChain.clear();
    }

}
