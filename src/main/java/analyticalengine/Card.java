package analyticalengine;

// A single Card

public class Card {
    String text; // Contents of card
    int index; // Index in chain of cards
    CardSource source; // Index in list of sources

    public Card(String s, int i, CardSource si) {
        text = s;
        index = i;
        source = si;
    }

    public String toString() {
        String s = "";

        s += (index + 1) + ". (" + source.sourceName + ":"
                + ((index - source.startIndex) + 1) + ") " + text;
        return s;
    }
}
