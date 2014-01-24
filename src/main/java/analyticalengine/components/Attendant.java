package analyticalengine.components;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import analyticalengine.components.cards.Card;
import analyticalengine.newio.UnknownCard;


public interface Attendant {
    void loadProgram(List<Card> cards) throws BadCard, IOException, UnknownCard;

    void setStripComments(boolean stripComments);
    
    void receiveOutput(String printed);

    void annotate(String message);

    void writeInColumns();

    void writeInRows();

    String finalReport();

    void writeNewline();

    void setCardReader(CardReader reader);

    void reset();

    void setLibraryPaths(List<Path> paths);

    void writeWithDecimal();

    void setFormat(String argument);
}
