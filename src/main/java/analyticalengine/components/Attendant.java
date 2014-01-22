package analyticalengine.components;

import analyticalengine.components.cards.CardChain;

public interface Attendant {
    void loadProgram(CardChain cards) throws BadCard;

    void setStripComments(boolean stripComments);
    
    void receiveOutput(String printed);

    void annotate(String message);

    void writeInColumns();

    void writeInRows();

    String finalReport();

    void writeNewline();

    void setCardReader(CardReader reader);

    void reset();
}
