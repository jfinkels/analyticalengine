/**
 * ArrayListCardReader.java - ArrayList implementation of card reader
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.cards.Card;

/**
 * A card reader backed by an {@link java.util.ArrayList} of cards.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class ArrayListCardReader implements CardReader {

    /**
     * The logger for this class.
     */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(ArrayListCardReader.class);

    /**
     * The sequence of cards representing the instructions to the Engine.
     */
    private List<Card> cardChain = new ArrayList<Card>();

    /**
     * The index of the card before the next card to read in the card chain.
     */
    private int currentCard = -1;

    /**
     * {@inheritDoc}
     * 
     * @param n
     *            {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     * @see analyticalengine.CardReader#advance(int)
     */
    @Override
    public void advance(final int n) {
        if (this.currentCard + n >= this.cardChain.size()) {
            throw new IndexOutOfBoundsException("No more cards to read.");
        }
        this.currentCard += n;
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     * @see analyticalengine.CardReader#cards()
     */
    @Override
    public List<Card> cards() {
        return Collections.unmodifiableList(this.cardChain);
    }

    /**
     * {@inheritDoc}
     * 
     * @param cardChain
     *            {@inheritDoc}
     * @see analyticalengine.CardReader#mountCards(List)
     */
    @Override
    public void mountCards(final List<Card> cardChain) {
        LOG.debug("Mounting card chain {}", cardChain);
        this.cardChain = new ArrayList<Card>(cardChain);
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     * @throws Halt
     *             {@inheritDoc}
     * @see analyticalengine.CardReader#readAndAdvance()
     */
    @Override
    public Card readAndAdvance() throws Halt {
        if (this.currentCard + 1 == this.cardChain.size()) {
            throw new Halt("Reached end of card chain.");
        }
        this.currentCard += 1;
        return this.cardChain.get(this.currentCard);
    }

    /**
     * {@inheritDoc}
     * 
     * @param n
     *            {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     * @see analyticalengine.CardReader#advance(int)
     */
    @Override
    public void reverse(final int n) {
        if (this.currentCard - n < -1) {
            throw new IndexOutOfBoundsException(
                    "Cannot reverse beyond beginning.");
        }
        this.currentCard -= n;
    }

    /**
     * {@inheritDoc}
     * 
     * @see analyticalengine.CardReader#unmountCards()
     */
    @Override
    public void unmountCards() {
        this.currentCard = -1;
        this.cardChain.clear();
    }

}
