package analyticalengine.newio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardChain;

public class CardReader {

    public static CardChain fromFile(File input) throws FileNotFoundException, IOException {
        CardChain cards = new ArrayListCardChain();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                cards.add(CardParser.toCard(currentLine));
                currentLine = reader.readLine();
            }
        }
        return cards;
    }

    public static CardChain fromFilename(String filename) throws IOException {
        return fromFile(new File(filename));
    }

    public static CardChain fromString(String input) {
        return fromString(input, System.lineSeparator());
    }

    public static CardChain fromString(String input, String lineSeparator) {
        CardChain cards = new ArrayListCardChain();
        for (String line : input.split(lineSeparator)) {
            cards.add(CardParser.toCard(line));
        }
        return cards;
    }
}
