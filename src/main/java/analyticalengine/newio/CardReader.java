package analyticalengine.newio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import analyticalengine.components.cards.Card;

public class CardReader {

    private CardReader() {
        // intentionally unimplemented
    }

    public static List<Card> fromPath(Path path) throws FileNotFoundException,
            IOException, UnknownCard {
        return fromPath(path, Charset.defaultCharset());
    }

    public static List<Card> fromPath(Path path, Charset charset)
            throws FileNotFoundException, IOException, UnknownCard {
        List<Card> cards = new ArrayList<Card>();
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                if (!currentLine.isEmpty()) {
                    cards.add(CardParser.toCard(currentLine));
                }
                currentLine = reader.readLine();
            }
        }
        return cards;
    }

    public static List<Card> fromString(String input) throws UnknownCard {
        return fromString(input, System.lineSeparator());
    }

    public static List<Card> fromString(String input, String lineSeparator)
            throws UnknownCard {
        List<Card> cards = new ArrayList<Card>();
        for (String line : input.split(lineSeparator)) {
            cards.add(CardParser.toCard(line));
        }
        return cards;
    }
}
