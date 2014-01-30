/**
 * CardReader.java - device that maintains instructions for the engine
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
package analyticalengine;

import java.util.List;

import analyticalengine.cards.Card;

/**
 * A device through which a sequence of cards is provided to the Engine.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public interface CardReader {

    /**
     * Make the card {@code n} cards ahead of the current card to be the next
     * card read.
     * 
     * Calling code must ensure that {@code n} does not advance the current
     * card pointer beyond the last card in the chain.
     * 
     * @param n
     *            The number of cards by which to advance the index of the
     *            current card.
     */
    void advance(int n);

    /**
     * Make the specified card chain available to the card reader.
     * 
     * @param cardChain
     *            The sequence of cards to load.
     */
    void mountCards(List<Card> cardChain);

    /**
     * Reads the current card in the card chain and prepares the following card
     * to be read on the next invocation of this method.
     * 
     * @return The next card in the card chain.
     * @throws Halt
     *             if there are no more cards to read in the card chain.
     */
    Card readAndAdvance() throws Halt;

    /**
     * Make the card {@code n} cards behind the current card to be the next
     * card read.
     * 
     * Calling code must ensure that {@code n} does not rewind the current card
     * pointer beyond the last card in the chain.
     * 
     * @param n
     *            The number of cards by which to decrease the index of the
     *            current card.
     */
    void reverse(int n);

    /**
     * Removes the currently mounted card chain from the card reader.
     */
    void unmountCards();

}
