package analyticalengine.newio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import analyticalengine.components.cards.Card;

public class CardReader {

    public static List<Card> fromFile(File input) throws FileNotFoundException, IOException {
        List<Card> cards = new Vector<Card>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                cards.add(CardParser.toCard(currentLine));
                currentLine = reader.readLine();
            }
        }
        return cards;
    }

    public static List<Card> fromFilename(String filename) throws IOException {
        return fromFile(new File(filename));
    }

    public static List<Card> fromString(String input) {
        return fromString(input, System.lineSeparator());
    }

    public static List<Card> fromString(String input, String lineSeparator) {
        List<Card> cards = new Vector<Card>();
        for (String line : input.split(lineSeparator)) {
            cards.add(CardParser.toCard(line));
        }
        return cards;
    }
}
