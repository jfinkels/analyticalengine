package analyticalengine.components;

import java.util.List;

import analyticalengine.components.cards.Card;

public interface CardReader {
    void advance(int n);
    List<Card> cards();
    void mountCards(List<Card> cards);
    Card readAndAdvance() throws Halt;
    void reverse(int n);
    void unmountCards();
    
}
