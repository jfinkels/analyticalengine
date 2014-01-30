/**
 * Card.java - an instruction for the engine
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
package analyticalengine.cards;

import java.util.Arrays;

/**
 * Provides an instruction for the Analytical Engine.
 * 
 * The specific instruction is specified by the
 * {@link analyticalengine.cards.CardType} parameter of the constructor.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class Card {
    /**
     * Creates and returns a new comment card with the specified comment.
     * 
     * @param comment
     *            The comment to place on the card.
     * @return A new comment card containing the specified message.
     */
    public static Card commentCard(final String comment) {
        return new Card(CardType.COMMENT, new String[] { comment });
    }

    /**
     * The arguments to the instruction, if any.
     * 
     * If the instruction does not require any arguments, this is an array of
     * length zero.
     */
    private final String[] arguments;

    /**
     * An additional comment provided on the card after the instruction and
     * arguments.
     */
    private final String comment;

    /** The specific instruction indicated by this card. */
    private final CardType type;

    /**
     * Creates a new card of the specified type with no arguments and no
     * additional comments.
     * 
     * @param type
     *            The type of the card.
     */
    public Card(final CardType type) {
        this(type, new String[] {});
    }

    /**
     * Creates a new card of the specified type with the specified arguments
     * but no additional comments.
     * 
     * @param type
     *            The type of the card.
     * @param arguments
     *            The arguments to the instruction that this card represents.
     */
    public Card(final CardType type, final String[] arguments) {
        this(type, arguments, "");
    }

    /**
     * Creates a new card of the specified type with the specified arguments
     * and specified additional comments.
     * 
     * @param type
     *            The type of the card.
     * @param arguments
     *            The arguments to the instruction that this card represents.
     * @param comment
     *            An additional comment on the card that doesn't affect its
     *            behavior.
     */
    public Card(final CardType type, final String[] arguments,
            final String comment) {
        this.type = type;
        this.arguments = arguments.clone();
        this.comment = comment;
    }

    /**
     * Returns the argument at the specified index.
     * 
     * Calling code must ensure that the index is less than the number of
     * arguments.
     * 
     * @param i
     *            The index of the argument to return in the array of all
     *            arguments.
     * @return The argument at index {@code i}
     */
    public String argument(final int i) {
        return this.arguments[i];
    }

    /**
     * Returns any additional comment provided when read from the input.
     * 
     * If no comment was provided, this method returns the empty string.
     * 
     * If the type of this card is
     * {@link analyticalengine.cards.CardType#COMMENT}, then this method
     * returns the empty string; on such a card, the comment is provided as the
     * first (and only) argument.
     * 
     * @return The additional comment, if any, provided on the card.
     */
    public String comment() {
        return this.comment;
    }

    /**
     * Returns the number of arguments of a card of this type.
     * 
     * @return The number of arguments of a card of this type.
     */
    public int numArguments() {
        return this.type.numArguments();
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (this.numArguments() > 0) {
            return this.type + Arrays.toString(this.arguments);
        }
        return this.type.toString();
    }

    /**
     * Returns the type of instruction this card represents.
     * 
     * @return The type of instruction this card represents.
     */
    public CardType type() {
        return this.type;
    }
}
