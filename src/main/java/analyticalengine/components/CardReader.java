package analyticalengine.components;

import java.util.List;

import analyticalengine.components.cards.Card;

public interface CardReader {
    Card readAndAdvance();
    void mountCards(List<Card> cards);
    void unmountCards();
    void advance(int n);
    void reverse(int n);
    List<Card> cards();
    
}
