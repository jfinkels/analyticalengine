package analyticalengine.components;

import java.util.List;

import analyticalengine.components.cards.Card;

public interface CardReader {
    void advance(int n);

    //CardChain cards();

    void mountCards(List<Card> cardChain);

    Card readAndAdvance() throws Halt;

    void reverse(int n);

    void unmountCards();

}
