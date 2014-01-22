package analyticalengine.components;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardChain;

public interface CardReader {
    void advance(int n);

    //CardChain cards();

    void mountCards(CardChain cards);

    Card readAndAdvance() throws Halt;

    void reverse(int n);

    void unmountCards();

}
