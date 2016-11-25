/**
 * ProgramWriter.java - writes a list of cards to an output stream
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

import java.io.PrintStream;
import java.util.List;

import analyticalengine.cards.Card;

/**
 * Writes a list of cards to an output stream.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public final class ProgramWriter {

    /**
     * Writes the specified list of cards to {@code System.out}.
     * 
     * One card string will be printed per line.
     * 
     * Uses {@link analyticalengine.io.CardParser} to convert the card into its
     * string representation.
     * 
     * @param cards
     *            The list of cards to write.
     * @throws UnknownCard
     *             if the list of cards contains an unrecognized card.
     */
    public static void write(final List<Card> cards) throws UnknownCard {
        write(cards, System.out);
    }

    /**
     * Writes the specified list of cards to the specified print stream.
     * 
     * One card string will be printed per line.
     * 
     * Uses {@link analyticalengine.io.CardParser} to convert the card into its
     * string representation.
     * 
     * @param cards
     *            The list of cards to write.
     * @param stream
     *            The print stream to which to write the string representation
     *            of the cards.
     * @throws UnknownCard
     *             if the list of cards contains an unrecognized card.
     */
    public static void write(final List<Card> cards, final PrintStream stream)
            throws UnknownCard {
        for (Card card : cards) {
            stream.println(card.toText());
        }
    }

    /** Instantiation disallowed. */
    private ProgramWriter() {
        // intentionally unimplemented
    }

}
