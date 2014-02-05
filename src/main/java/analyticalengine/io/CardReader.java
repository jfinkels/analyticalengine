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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import analyticalengine.cards.Card;

/**
 * Reads an Analytical Engine program from a string or file and returns the
 * card chain that it represents.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public final class CardReader {

    /** Instantiation disallowed. */
    private CardReader() {
        // intentionally unimplemented
    }

    /**
     * Convenience method for {@link #fromPath(Path, Charset)} where the
     * charset is the default charset of this Java virtual machine.
     * 
     * @param path
     *            The path of the file containing the program.
     * @return A card chain, given as a {@link java.util.List} of
     *         {@link analyticalengine.cards.Card} instances.
     * @throws IOException
     *             if there is a problem reading the file.
     * @throws UnknownCard
     *             if a line in the file does not correspond to a known type of
     *             card (essentially, this is a syntax error).
     */
    public static List<Card> fromPath(final Path path) throws IOException,
            UnknownCard {
        return fromPath(path, Charset.defaultCharset());
    }

    /**
     * Reads an Analytical Engine program from the specified path and converts
     * it into a card chain.
     * 
     * @param path
     *            The path of the file containing the program.
     * @param charset
     *            The charset of the file.
     * @return A card chain, given as a {@link java.util.List} of
     *         {@link analyticalengine.cards.Card} instances.
     * @throws IOException
     *             if there is a problem reading the file.
     * @throws UnknownCard
     *             if a line in the file does not correspond to a known type of
     *             card (essentially, this is a syntax error).
     */
    // TODO create a syntax error and raise it instead of unknown card?
    public static List<Card> fromPath(final Path path, final Charset charset)
            throws IOException, UnknownCard {
        List<Card> cards = new ArrayList<Card>();
        try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
            String currentLine = reader.readLine();
            while (currentLine != null) {
                if (currentLine.isEmpty()) {
                    cards.add(Card.commentCard(""));
                } else {
                    cards.add(CardParser.toCard(currentLine));
                }
                currentLine = reader.readLine();
            }
        }
        return cards;
    }

    /**
     * Convenience method for {@link #fromString(String, String)}, where the
     * second argument is the system line separator.
     * 
     * @param input
     *            The string containing the program.
     * @return A card chain, given as a {@link java.util.List} of
     *         {@link analyticalengine.cards.Card} instances.
     * @throws UnknownCard
     *             if a line in the string does not correspond to a known type
     *             of card (essentially, this is a syntax error).
     */
    public static List<Card> fromString(final String input) throws UnknownCard {
        return fromString(input, System.lineSeparator());
    }

    /**
     * Reads an Analytical Engine program from the specified string and
     * converts it into a card chain.
     * 
     * @param input
     *            The string containing the program.
     * @param lineSeparator
     *            The string that separates lines in the given input string.
     * @return A card chain, given as a {@link java.util.List} of
     *         {@link analyticalengine.cards.Card} instances.
     * @throws UnknownCard
     *             if a line in the string does not correspond to a known type
     *             of card (essentially, this is a syntax error).
     */
    public static List<Card> fromString(final String input,
            final String lineSeparator) throws UnknownCard {
        List<Card> cards = new ArrayList<Card>();
        for (String line : input.split(lineSeparator)) {
            cards.add(CardParser.toCard(line));
        }
        return cards;
    }
}
