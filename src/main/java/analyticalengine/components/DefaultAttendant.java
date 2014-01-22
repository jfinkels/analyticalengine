package analyticalengine.components;

import java.awt.datatransfer.StringSelection;
import java.util.List;

import analyticalengine.components.cards.Card;

public class DefaultAttendant implements Attendant {

    private CardReader cardReader = null;
    private String report = "";
    private boolean writeInRows = true;

    @Override
    public void reset() {
        this.report = "";
        this.cardReader.unmountCards();
    }

    @Override
    public void loadProgram(List<Card> cards) {
        // TODO Auto-generated method stub
        // TODO needs to translate cycle, write as, decimal expansion cards,
        // etc.

        // ...
        List<Card> newCards = cards;
        // ...

        this.cardReader.mountCards(newCards);
    }

    @Override
    public void receiveOutput(String printed) {
        if (this.writeInRows) {
            this.report += printed;
        } else {
            // TODO do something different
        }
    }

    @Override
    public void annotate(String message) {
        if (this.writeInRows) {
            this.report += message;
        } else {
            // TODO do something different
        }
    }

    @Override
    public void writeInColumns() {
        this.writeInRows = false;
    }

    @Override
    public void writeInRows() {
        this.writeInRows = true;
    }

    @Override
    public String finalReport() {
        return this.report;
    }

    @Override
    public void writeNewline() {
        this.report += System.getProperty("line.separator");
    }

    @Override
    public void setCardReader(CardReader reader) {
        this.cardReader = reader;
    }

}
