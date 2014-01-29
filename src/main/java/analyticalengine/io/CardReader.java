/**
 * CardReader.java - reads a sequence of cards from a text file
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 * 
 * This file is part of analyticalengine.
 * 
 * analyticalengine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * analyticalengine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * analyticalengine. If not, see <http://www.gnu.org/licenses/>.
 */
package analyticalengine.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import analyticalengine.cards.Card;

public final class CardReader {

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
