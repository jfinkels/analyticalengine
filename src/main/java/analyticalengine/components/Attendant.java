package analyticalengine.components;

import java.util.List;

import analyticalengine.components.cards.Card;

public interface Attendant {
    void loadProgram(List<Card> cards);
    void receiveOutput(String printed);
    void annotate(String message);
    void writeInColumns();
    void writeInRows();
    String finalReport();
    void writeNewline();
    void setCardReader(CardReader reader);
    void reset();
}
