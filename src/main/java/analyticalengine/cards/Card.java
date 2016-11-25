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
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
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
     * Parses a card from the specified card string.
     * 
     * A card string is a single line of an Analytical Engine program.
     * 
     * @param cardString
     *            A string representation of a card.
     * @return The card specified by the given string.
     * @throws UnknownCard
     *             if the specified string does not correspond to a known card
     *             (essentially, this is a syntax error).
     */
    public static Card fromString(final String cardString) throws UnknownCard {
        if (cardString.isEmpty()) {
            return Card.commentCard("");
        }
        char firstChar = cardString.charAt(0);
        String untrimmedRest = cardString.substring(1);
        String rest = leftTrim(untrimmedRest.trim());
        // TODO need to account for possible inline comments after period
        switch (firstChar) {
        case '+':
            return new Card(CardType.ADD);
        case '*':
            return new Card(CardType.MULTIPLY);
        case '/':
            return new Card(CardType.DIVIDE);
        case '-':
            return new Card(CardType.SUBTRACT);
        case 'N':
            String[] args = rest.split("\\s+");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
            return new Card(CardType.NUMBER, args);
        case 'L':
            if (rest.endsWith("'")) {
                return new Card(CardType.LOADPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.LOAD, new String[] { rest });
        case 'Z':
            if (rest.endsWith("'")) {
                return new Card(CardType.ZLOADPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.ZLOAD, new String[] { rest });
        case 'S':
            if (rest.endsWith("'")) {
                return new Card(CardType.STOREPRIME,
                        new String[] { rest.substring(0, rest.length() - 1) });
            }
            return new Card(CardType.STORE, new String[] { rest });
        case '<':
            if (!rest.isEmpty()) {
                return new Card(CardType.LSHIFTN, new String[] { rest });
            }
            return new Card(CardType.LSHIFT);
        case '>':
            if (!rest.isEmpty()) {
                return new Card(CardType.RSHIFTN, new String[] { rest });
            }
            return new Card(CardType.RSHIFT);
        case 'C':
            return parseCombinatorialCard(cardString);
        case 'B':
            return new Card(CardType.BELL);
        case 'P':
            return new Card(CardType.PRINT);
        case 'H':
            return new Card(CardType.HALT);
        case 'D':
            if (rest.charAt(0) == '+') {
                return new Card(CardType.DRAW);
            } else if (rest.charAt(0) == '-') {
                return new Card(CardType.MOVE);
            } else if (rest.charAt(0) == 'X') {
                return new Card(CardType.SETX);
            } else if (rest.charAt(0) == 'Y') {
                return new Card(CardType.SETY);
            } else {
                throw new UnknownCard(
                        "Draw card must be one of DX, DY, D+, or D-, got: "
                                + cardString);
            }
        case 'A':
            if (rest.startsWith("include cards")) {
                return new Card(CardType.INCLUDE,
                        new String[] { rest.substring(14) });
            } else if (rest.startsWith("include from library cards for")) {
                return new Card(CardType.INCLUDELIB,
                        new String[] { rest.substring(31) });
            } else if (rest.startsWith("set decimal places to")) {
                return new Card(CardType.DECIMALEXPAND,
                        new String[] { rest.substring(22) });
            } else if (rest.startsWith("write numbers as")) {
                return new Card(CardType.WRITEPICTURE,
                        new String[] { untrimmedRest.substring(18) });
            } else if (rest.startsWith("write numbers with decimal point")) {
                return new Card(CardType.WRITEDECIMAL);
            } else if (rest.startsWith("write in rows")) {
                return new Card(CardType.WRITEROWS);
            } else if (rest.startsWith("write in columns")) {
                return new Card(CardType.WRITECOLUMNS);
            } else if (rest.startsWith("write new line")) {
                return new Card(CardType.NEWLINE);
            } else if (rest.startsWith("write annotation")) {
                return new Card(CardType.ANNOTATE,
                        new String[] { untrimmedRest.substring(18) });
            } else {
                throw new UnknownCard("Unknown attendant request: "
                        + cardString);
            }
        case '(':
            if (!rest.isEmpty() && rest.charAt(0) == '?') {
                return new Card(CardType.CBACKSTART);
            }
            return new Card(CardType.BACKSTART);
        case ')':
            return new Card(CardType.BACKEND);
        case '{':
            if (!rest.isEmpty() && rest.charAt(0) == '?') {
                return new Card(CardType.CFORWARDSTART);
            }
            return new Card(CardType.FORWARDSTART);
        case '}':
            if (!rest.isEmpty() && rest.charAt(0) == '{') {
                return new Card(CardType.ALTERNATION);
            }
            return new Card(CardType.FORWARDEND);
        case 'T':
            if (rest.charAt(0) == '1') {
                return new Card(CardType.TRACEON);
            } else if (rest.charAt(0) == '0') {
                return new Card(CardType.TRACEOFF);
            } else {
                throw new UnknownCard("Expected T0 or T1 but got: "
                        + cardString);
            }
        case ' ':
        case '.':
            return Card.commentCard(rest);
        default:
            throw new UnknownCard("Unable to parse: " + cardString);
        }
    }

    /**
     * Trim whitespace from the left of the specified string.
     * 
     * <a
     * href="//fromdev.com/2009/07/playing-with-java-string-trim-basics.html">
     * Source.</a>
     * 
     * @param value
     *            The string to trim.
     * @return The string with any whitespace on the left end trimmed.
     */
    public static String leftTrim(final String value) {
        int i = 0;
        while (i < value.length() && Character.isWhitespace(value.charAt(i))) {
            i++;
        }
        return value.substring(i);
    }

    /**
     * Returns the combinatorial card represented by the specified string.
     * 
     * @param cardString
     *            The string representing a combinatorial card.
     * @return The card parsed from the specified string.
     * @throws UnknownCard
     *             if there is a syntax error while parsing the given string.
     */
    private static Card parseCombinatorialCard(final String cardString)
            throws UnknownCard {
        String rest = cardString.substring(1);
        if (rest.charAt(0) == 'F') {
            if (rest.charAt(1) == '?') {
                return new Card(CardType.CFORWARD,
                        new String[] { rest.substring(2) });
            } else if (rest.charAt(1) == '+') {
                return new Card(CardType.FORWARD,
                        new String[] { rest.substring(2) });
            } else {
                throw new UnknownCard(
                        "Forward card must be either CF+ or CF?, got: "
                                + cardString);
            }
        } else if (rest.charAt(0) == 'B') {
            if (rest.charAt(1) == '?') {
                return new Card(CardType.CBACKWARD,
                        new String[] { rest.substring(2) });
            } else if (rest.charAt(1) == '+') {
                return new Card(CardType.BACKWARD,
                        new String[] { rest.substring(2) });
            } else {
                throw new UnknownCard(
                        "Backward card must be either CB+ or CB?, got: "
                                + cardString);
            }
        } else {
            throw new UnknownCard(
                    "Combinatorial card must be either CF or CB, got: "
                            + cardString);
        }
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
     * Creates a new card of the specified type with the specified comment, but
     * no arguments.
     * 
     * @param type
     *            The type of the card.
     * @param comment
     *            The comment on the card.
     */
    public Card(final CardType type, final String comment) {
        this(type, new String[] {}, comment);
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
     * Returns the string representation of the specified card (with any
     * terminal comments intact).
     * 
     * @param card
     *            The card to convert to a string.
     * @return The string representation of the specified card.
     * @throws UnknownCard
     *             if the specified card is of an unrecognized type.
     */
    public String toText() throws UnknownCard {
        // TODO account for comments after the instructions
        switch (this.type()) {
        case ADD:
            return "+";
        case ALTERNATION:
            return "}{";
        case ANNOTATE:
            return "A write annotation " + this.argument(0);
        case BACKEND:
            return ")";
        case BACKSTART:
            return "(";
        case BACKWARD:
            return "CB+" + this.argument(0);
        case BELL:
            return "B";
        case CBACKSTART:
            return "(?";
        case CBACKWARD:
            return "CB?" + this.argument(0);
        case CFORWARD:
            return "CF?" + this.argument(0);
        case CFORWARDSTART:
            return "{?";
        case COMMENT:
            return "  " + this.argument(0);
        case DECIMALEXPAND:
            return "A set decimal places to " + this.argument(0);
        case DIVIDE:
            return "/";
        case DRAW:
            return "D+";
        case FORWARD:
            return "CF+" + this.argument(0);
        case FORWARDEND:
            return "}";
        case FORWARDSTART:
            return "{";
        case HALT:
            return "H";
        case INCLUDE:
            return "A include cards " + this.argument(0);
        case INCLUDELIB:
            return "A include from library cards for " + this.argument(0);
        case LOAD:
            return "L" + this.argument(0);
        case LOADPRIME:
            return "L" + this.argument(0) + ";";
        case LSHIFT:
            return "<";
        case LSHIFTN:
            return "<" + this.argument(0);
        case MOVE:
            return "D-";
        case MULTIPLY:
            return "*";
        case NEWLINE:
            return "A write new line";
        case NUMBER:
            return "N" + this.argument(0) + " " + this.argument(1);
        case PRINT:
            return "P";
        case RSHIFT:
            return ">";
        case RSHIFTN:
            return ">" + this.argument(0);
        case SETX:
            return "DX";
        case SETY:
            return "DY";
        case STORE:
            return "S" + this.argument(0);
        case STOREPRIME:
            return "S" + this.argument(0) + "'";
        case SUBTRACT:
            return "-";
        case TRACEOFF:
            return "T0";
        case TRACEON:
            return "T1";
        case WRITECOLUMNS:
            return "A write in columns";
        case WRITEDECIMAL:
            return "A write numbers with decimal point";
        case WRITEPICTURE:
            return "A write numbers as " + this.argument(0);
        case WRITEROWS:
            return "A write in rows";
        case ZLOAD:
            return "Z" + this.argument(0);
        case ZLOADPRIME:
            return "Z" + this.argument(0) + "'";
        default:
            throw new UnknownCard("Unable to parse: " + this);
        }
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
