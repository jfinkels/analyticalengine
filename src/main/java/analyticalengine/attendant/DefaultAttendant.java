/**
 * DefaultAttendant.java - basic implementation of the operator of the engine
 * 
 * Copyright 2014-2016 Jeffrey Finkelstein.
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
package analyticalengine.attendant;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analyticalengine.cards.BadCard;
import analyticalengine.cards.Card;
import analyticalengine.cards.CardType;
import analyticalengine.cards.UnknownCard;
import analyticalengine.components.CardReader;

/**
 * A basic implementation of the Attendant interface.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultAttendant implements Attendant {

    /** The logger for this class. */
    private static final transient Logger LOG = LoggerFactory
            .getLogger(DefaultAttendant.class);

    /**
     * Returns {@code true} if and only if the cycle started by card of type
     * {@code start} matches the cycle end specified by card of type {code
     * end}.
     * 
     * @param start
     *            A card type representing the start of a cycle.
     * @param end
     *            A card type representing the end of a cycle.
     * @return {@code true} if and only if the cycle started by card with type
     *         {@code start} matches the cycle end specified by type {code
     *         end}.
     */
    private static boolean cyclesMatch(final CardType start,
            final CardType end) {
        switch (start) {
        case BACKSTART:
        case CBACKSTART:
            // A ( or (? must be closed by a ).
            switch (end) {
            case BACKEND:
                return true;
            default:
                return false;
            }
        case FORWARDSTART:
        case CFORWARDSTART:
            // A { or {? must be closed by a } or }{
            switch (end) {
            case FORWARDEND:
            case ALTERNATION:
                return true;
            default:
                return false;
            }
        default:
            throw new IllegalArgumentException(
                    "Cannot check cycle matching for cards " + start + " and "
                            + end);
        }
    }

    /**
     * Returns {@code true} if and only if the card type indicates the end of a
     * cycle block (including an alternation).
     * 
     * @param type
     *            The type of card.
     * @return {@code true} if and only if the card type indicates the end of a
     *         cycle block (including an alternation).
     */
    private static boolean isCycleEnd(final CardType type) {
        return isIn(type, CardType.BACKEND, CardType.ALTERNATION,
                CardType.FORWARDEND);
    }

    /**
     * Returns {@code true} if and only if the card type indicates the start of
     * a cycle block.
     * 
     * @param type
     *            The type of card.
     * @return {@code true} if and only if the card type indicates the start of
     *         a cycle block.
     */
    private static boolean isCycleStart(final CardType type) {
        return isIn(type, CardType.BACKSTART, CardType.CBACKSTART,
                CardType.FORWARDSTART, CardType.CFORWARDSTART);
    }

    /**
     * Returns {@code true} if and only if any of the elements of
     * {@code haystack} are equal to {@code needle}, according to the
     * {@link Object#equals(Object)} method.
     * 
     * @param needle
     *            The element to look for.
     * @param haystack
     *            The array in which to search for the element.
     * @return {@code true} if and only if the element is found somewhere in
     *         the given array.
     */
    private static boolean isIn(final Object needle,
            final Object... haystack) {
        for (Object element : haystack) {
            if (element.equals(needle)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a new {@link java.util.List} containing only those cards that
     * are not comments.
     * 
     * @param cards
     *            The list of cards to filter.
     * @return A new {@link java.util.List} containing only those cards that
     *         are not comments.
     */
    private static List<Card> strippedComments(final List<Card> cards) {
        List<Card> result = new ArrayList<Card>();
        for (Card card : cards) {
            if (card.type() != CardType.COMMENT) {
                result.add(card);
            }
        }
        return result;
    }

    /** The device in which the attendant loads the requested program. */
    private CardReader cardReader = null;

    /**
     * The format in which the attendant should write the output printed from
     * the Analytical Engine.
     */
    private String formatString = null;

    /** The library of built-in functions maintained by the attendant. */
    private Library library = null;

    /** The cumulative formatted output from the printer. */
    private String report = "";

    /**
     * Whether to remove comment cards from the program that will be loaded.
     */
    private boolean stripComments = false;

    /**
     * Whether to write the output from the Analytical Engine's printer in
     * rows.
     * 
     * If this is false, the output will be written in columns.
     */
    private boolean writeInRows = true;

    /**
     * {@inheritDoc}
     * 
     * @param message
     *            {@inheritDoc}
     */
    @Override
    public void annotate(final String message) {
        this.report += message;
        if (this.writeInRows) {
            this.report += System.lineSeparator();
        }
    }

    /**
     * Looks for cards that specify a global number of decimal places and
     * replaces any later cards that may be affected, like implicit shift or
     * number cards.
     * 
     * @param cards
     *            The list of cards to scan for decimal expansion.
     * @return A new list of cards representing the same program, but with the
     *         implicit decimals expanded into explicit decimals.
     * @throws BadCard
     *             if a card related to decimal expansion has invalid syntax.
     */
    private List<Card> expandDecimal(final List<Card> cards) throws BadCard {
        List<Card> result = new ArrayList<Card>();
        int decimalPlace = -1;
        for (Card card : cards) {
            // This variable shows up in more than one case, but we cannot
            // define it within each case because each case has the same
            // (switch-level) scope, even though it would be more readable that
            // way.
            Card replacement = null;
            switch (card.type()) {
            // A set decimal places to [+/-]n
            case DECIMALEXPAND:
                String dspec = card.argument(0);
                int relative = 0;

                if (dspec.charAt(0) == '+' || dspec.charAt(0) == '-') {
                    if (decimalPlace == -1) {
                        String msg = "I cannot accept a relative decimal place"
                                + "setting\nwithout a prior absolute setting.";
                        throw new BadCard(msg, card);
                    }
                    if (dspec.charAt(0) == '+') {
                        relative = 1;
                    } else {
                        relative = -1;
                    }
                    dspec = dspec.substring(1);
                }
                int d;
                try {
                    d = Integer.parseInt(dspec);
                } catch (NumberFormatException e) {
                    String msg = "I cannot find the number of decimal places"
                            + " you wish to use.";
                    throw new BadCard(msg, card, e);
                }
                if (relative != 0) {
                    d = decimalPlace + d * relative;
                }

                if (!this.stripComments) {
                    replacement = Card.commentCard(
                            "A set decimal places to " + card.argument(0));
                    result.add(replacement);
                }
                decimalPlace = d;
                break;
            // Convert "A write numbers with decimal point" to a picture
            case WRITEDECIMAL:
                if (decimalPlace < 0) {
                    String msg = "I cannot add the number of decimal places"
                            + " because\n you have not instructed me how many"
                            + " decimal\nplaces to use in a prior \"A set"
                            + " decimal places to\"\ninstruction.";
                    throw new BadCard(msg, card);
                }

                replacement = this.expandWriteDecimal(decimalPlace);
                result.add(replacement);
                break;
            /*
             * Replace number cards with decimal points with cards scaled to
             * the proper number of digits.
             */
            case NUMBER:
                String number = card.argument(1);
                int decimalIndex = number.indexOf('.');
                // If the number has no decimal, just add the card as is.
                if (decimalIndex < 0) {
                    result.add(card);
                    break;
                }
                if (decimalPlace < 0) {
                    String msg = "I cannot add the number of decimal places"
                            + " because\n you have not instructed me how many"
                            + " decimal\nplaces to use in a prior \"A set"
                            + " decimal places to\"\ninstruction.";
                    throw new BadCard(msg, card);
                }
                replacement = this.expandNumber(decimalPlace, card);
                result.add(replacement);
                break;
            // Add step up/down to "<" or ">" if not specified
            case LSHIFT:
            case RSHIFT:
                if (decimalPlace < 0) {
                    String msg = "I cannot add the number of decimal places"
                            + " because\n you have not instructed me how many"
                            + " decimal\nplaces to use in a prior \"A set"
                            + " decimal places to\"\ninstruction.";
                    throw new BadCard(msg, card);
                }

                replacement = this.expandShift(decimalPlace, card);
                result.add(replacement);
                break;
            default:
                // If no change needs to be made, just add the card as-is.
                result.add(card);
                break;
            }
        }
        return result;
    }

    /**
     * Returns a new number card that represents the same number, but trimmed
     * or expanded to have the specified number of digits after the decimal
     * point.
     * 
     * @param decimalPlace
     *            The number of digits the number should have after the decimal
     *            point.
     * @param card
     *            A card with type {@link CardType#NUMBER}.
     * @return A new card that represents the same number as on the original
     *         card, but with the specified number of digits after the decimal
     *         point.
     */
    private Card expandNumber(final int decimalPlace, final Card card) {
        // these two lines are repeated in the calling function, but we
        // repeat them here so that this method has only the two parameters
        String number = card.argument(1);
        int decimalIndex = number.indexOf('.');

        /*
         * Now adjust the decimal part to the given number of decimal places by
         * trimming excess digits and appending zeroes as required.
         */
        String dpart = number.substring(decimalIndex + 1);
        if (dpart.length() > decimalPlace) {
            /*
             * If we're trimming excess digits, round the remaining digits
             * based on the first digit of the portion trimmed.
             */
            if (Character.digit(dpart.charAt(decimalPlace), 10) >= 5) {
                dpart = new BigInteger(dpart.substring(0, decimalPlace), 10)
                        .add(BigInteger.ONE).toString();
            } else {
                dpart = dpart.substring(0, decimalPlace);
            }
        }

        StringBuilder afterDecimal = new StringBuilder(dpart);
        while (afterDecimal.length() < decimalPlace) {
            afterDecimal.append("0");
        }

        // Append the decimal part to fixed part from card
        String newNumber = number.substring(0, decimalIndex) + afterDecimal;
        if (this.stripComments) {
            return new Card(CardType.NUMBER,
                    new String[] { card.argument(0), newNumber });
        }
        return new Card(CardType.NUMBER,
                new String[] { card.argument(0), newNumber },
                "Decimal expansion by attendant");
    }

    /**
     * Replaces the given implicit left or right shift card with the
     * corresponding explicit shift card, with a shift of the specified number
     * of decimal places.
     * 
     * @param decimalPlace
     *            The number of digits by which to shift.
     * @param card
     *            An implicit left or right shift card.
     * @return An explicit left or right shift card with a shift argument of
     *         the specified number of decimal places.
     */
    private Card expandShift(final int decimalPlace, final Card card) {
        CardType newType = null;
        if (card.type() == CardType.LSHIFT) {
            newType = CardType.LSHIFTN;
        } else {
            newType = CardType.RSHIFTN;
        }

        String newArg = String.valueOf(decimalPlace);
        if (this.stripComments) {
            return new Card(newType, new String[] { newArg });
        }
        return new Card(newType, new String[] { newArg },
                "Step count added by attendant");
    }

    /**
     * Replaces a {@link CardType#WRITEDECIMAL} card with a
     * {@link CardType#WRITEPICTURE} card that instructs the attendant to
     * format the output with the specified number of decimal places.
     * 
     * @param decimalPlace
     *            The number of digits that the attendant should write after
     *            the decimal point.
     * @return A {@link CardType#WRITEPICTURE} card that instructs the
     *         attendant to format the output with the specified number of
     *         decimal places.
     */
    private Card expandWriteDecimal(final int decimalPlace) {
        int dpa;

        StringBuilder formatString = new StringBuilder("9.");
        for (dpa = 0; dpa < decimalPlace; dpa++) {
            formatString.append("9");
        }

        return new Card(CardType.WRITEPICTURE,
                new String[] { formatString.toString() });
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public String finalReport() {
        return this.report;
    }

    /**
     * Returns a formatted version of the specified number (given as a string)
     * according to the value specified previously by the
     * {@link #setFormat(String)} method.
     * 
     * @param input
     *            A number to format, given as a string.
     * @return The input number formatted according to the format specified by
     *         a previous invocation of {@link #setFormat(String)}.
     */
    private String formatted(final String input) {
        String s = input;
        boolean negative = input.charAt(0) == '-';
        boolean sign = false;

        if (this.formatString != null) {
            // s = v.abs().toString();
            if (negative) {
                s = input.substring(1);
            } else {
                s = input;
            }

            int i = this.formatString.length();
            String o = "";

            while (--i >= 0) {
                char c = this.formatString.charAt(i);

                switch (c) {
                case '9': // Digit, unconditionally
                    if (s.length() == 0) {
                        o = "0" + o;
                    } else {
                        o = s.substring(s.length() - 1, s.length()) + o;
                        s = s.substring(0, s.length() - 1);
                    }
                    break;

                case '#': // Digit, if number not exhausted
                    if (s.length() > 0) {
                        o = s.substring(s.length() - 1, s.length()) + o;
                        s = s.substring(0, s.length() - 1);
                    }
                    break;

                case ',': // Comma if digits remain to output
                    if (this.formatString.indexOf('9') >= 0
                            || s.length() > 0) {
                        o = c + o;
                    }
                    break;

                case '-': // Sign if negative
                    if (negative) {
                        o = '-' + o;
                        sign = true;
                    }
                    break;

                case '±': // Plus or minus sign
                    if (negative) {
                        o = '-' + o;
                    } else {
                        o = '+' + o;
                    }
                    sign = true;
                    break;

                case '+': // Sign if negative, space if positive
                    if (negative) {
                        o = '-' + o;
                    } else {
                        o = ' ' + o;
                    }
                    sign = true;
                    break;

                default: // Copy character to output
                    o = c + o;
                    break;
                }
            }
            /*
             * If there's any number "left over", write it to prevent
             * truncation without warning.
             */
            if (s.length() > 0) {
                o = s + o;
            }
            /*
             * If the number is negative and no sign has been output so far,
             * prefix it with a sign.
             */
            if (negative && !sign) {
                o = '-' + o;
            }
            s = o;
        }

        return s;
    }

    /**
     * {@inheritDoc}
     * 
     * @param cards
     *            {@inheritDoc}
     * @throws BadCard
     *             {@inheritDoc}
     * @throws IOException
     *             {@inheritDoc}
     * @throws UnknownCard
     *             {@inheritDoc}
     * @throws LibraryLookupException
     *             {@inheritDoc}
     */
    @Override
    public void loadProgram(final List<Card> cards)
            throws BadCard, IOException, UnknownCard, LibraryLookupException {
        LOG.debug("Examining cards before loading: " + cards);
        List<Card> result = cards;
        // Note: "A write numbers as ..." cards remain in the card chain.
        LOG.debug("Transcluding requested library functions");
        result = this.transcludeLibraryCards(result);
        if (this.stripComments) {
            LOG.debug("Remove comment cards");
            result = strippedComments(result);
        }
        LOG.debug("Making requested decimal expansions");
        result = this.expandDecimal(result);
        // TODO this method should return a new list like the others do
        LOG.debug("Translating shorthand combinatoric cards");
        translateCombinatorics(result);
        LOG.debug("Mounting cards in reader: " + result);
        this.cardReader.mountCards(result);
    }

    /**
     * Log that a bell has been rung.
     * 
     * Subclasses that wish to perform some action on a bell ring should
     * override and reimplement this method.
     * 
     * @param card
     *            {@inheritDoc}
     */
    @Override
    public void onBell(Card card) {
        LOG.info("Bell! Caused by card: %s", card);
    }

    /**
     * {@inheritDoc}
     * 
     * @param printed
     *            {@inheritDoc}
     */
    @Override
    public void receiveOutput(final String printed) {
        this.report += this.formatted(printed);
        if (this.writeInRows) {
            this.report += System.lineSeparator();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.report = "";
        this.cardReader.unmountCards();
        this.library.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @param reader
     *            {@inheritDoc}
     */
    @Override
    public void setCardReader(final CardReader reader) {
        this.cardReader = reader;
    }

    /**
     * {@inheritDoc}
     * 
     * @param formatString
     *            {@inheritDoc}
     */
    @Override
    public void setFormat(final String formatString) {
        this.formatString = formatString;
    }

    /**
     * {@inheritDoc}
     * 
     * @param library
     *            {@inheritDoc}
     */
    @Override
    public void setLibrary(final Library library) {
        this.library = library;
    }

    /**
     * {@inheritDoc}
     * 
     * @param stripComments
     *            {@inheritDoc}
     * @see analyticalengine.attendant.Attendant#setStripComments(boolean)
     */
    @Override
    public void setStripComments(final boolean stripComments) {
        this.stripComments = stripComments;
    }

    /**
     * Scans the list of cards for any inclusion requests and replaces those
     * inclusion cards with the cards of the requested function.
     * 
     * @param cards
     *            The list of cards to scan for include requests.
     * @return A new list of cards representing the same program, but with the
     *         include requests replaced with the cards necessary to execute
     *         the requested function.
     * @throws BadCard
     *             if the requested file could not be found.
     * @throws IOException
     *             if there was a problem reading the file containing the
     *             requested function.
     * @throws UnknownCard
     *             if there was a syntax error in the file containing the
     *             requested function.
     * @throws LibraryLookupException
     *             if there was a problem reading the requested library
     *             function.
     */
    private List<Card> transcludeLibraryCards(final List<Card> cards)
            throws BadCard, IOException, UnknownCard, LibraryLookupException {

        List<Card> result = new ArrayList<Card>();
        for (Card card : cards) {
            /*
             * We recognise a card which begins with an "@" or the more
             * Victorian " A include cards " as an instruction to the attendant
             * to locate the chain of cards bearing that label and interpolate
             * it into the chain, replacing the card requesting it. This allows
             * commonly used sequences of calculation to be called out without
             * having to physically copy them into the original card chain.
             */
            if (card.type() == CardType.INCLUDE) {
                Path path = Paths.get(card.argument(0));
                if (!Files.exists(path)) {
                    throw new BadCard("Could not find file: " + path, card);
                }

                result.add(Card.commentCard(
                        "Begin interpolation of " + card + " by attendant"));
                for (String line : Files.readAllLines(path)) {
                    result.add(Card.fromString(line));
                }
                result.add(Card.commentCard(
                        "Endinterpolation of " + card + " by attendant"));
                /*
                 * The analyst can request the inclusion of a set of cards from
                 * the machine's library with the request to the attendant:
                 * 
                 * A include from library cards for <whatever>
                 * 
                 * The attendant searches for a set of cards named <whatever>
                 * and, if found, they are strung into the chain, replacing the
                 * library request.
                 */
            } else if (card.type() == CardType.INCLUDELIB) {
                String filename = card.argument(0);
                List<Card> newCards;
                newCards = this.library.find(filename);

                result.add(Card.commentCard("Begin interpolation of "
                        + filename + " from library by attendant"));
                result.addAll(newCards);
                result.add(Card.commentCard("End interpolation of " + filename
                        + " from library by attendant"));
            } else {
                result.add(card);
            }
        }

        return result;
    }

    /**
     * Scans the list of cards for implicit combinatorial (control) cards and
     * replaces them (in-place) with the corresponding explicit forward and
     * backward jump cards.
     * 
     * @param cards
     *            The list of cards to scan for implicit loops.
     * @throws BadCard
     *             if there is a syntax error in one of the implicit
     *             combinatorial cards (for example, if a start card has no
     *             corresponding end card).
     */
    private void translateCombinatorics(final List<Card> cards)
            throws BadCard {
        for (int i = 0; i < cards.size(); i++) {
            if (isCycleStart(cards.get(i).type())) {
                LOG.debug("Translating cycle starting from " + cards.get(i)
                        + " at index " + i);
                this.translateCycle(cards, i);
            }
        }
    }

    /**
     * Replaces the implicit cycle (loop) starting at the specified index in
     * the given list of cards with explicit forward and/or backward jump
     * cards.
     * 
     * @param cards
     *            The list of cards containing the implicit loop to replace
     *            (in-place).
     * @param startIndex
     *            The index of the implicit loop to replace.
     * @throws BadCard
     *             if there is a syntax error in one of the implicit
     *             combinatorial cards (for example, if a start card has no
     *             corresponding end card).
     */
    private void translateCycle(final List<Card> cards, final int startIndex)
            throws BadCard {
        Card c = cards.get(startIndex);
        boolean depends = false;
        LOG.debug("Translating cycle starting at " + c);

        depends = c.type() == CardType.CBACKSTART
                || c.type() == CardType.CFORWARDSTART;
        int start = startIndex;
        if (this.stripComments) {
            cards.remove(start);
            start--;
        } else {
            cards.remove(start);
            cards.add(start, Card.commentCard(c + " Translated by attendant"));
        }

        /*
         * Search for the end of this cycle. If a sub-cycle is detected,
         * recurse to translate it.
         */

        for (int i = start + 1; i < cards.size(); i++) {
            Card u = cards.get(i);
            LOG.debug("looking at card " + u);
            if (isCycleStart(u.type())) {
                LOG.debug("Making a recursive call from new start index " + i);
                translateCycle(cards, i);
            }
            if (isCycleEnd(u.type())) {
                LOG.debug("  Found end of cycle starting with " + c);

                if (!cyclesMatch(c.type(), u.type())) {
                    throw new BadCard("End of cycle does not match " + c
                            + " beginning on card " + start, u);
                }
                cards.remove(i);
                if (!this.stripComments) {
                    // cards.remove(i);
                    cards.add(i,
                            Card.commentCard(u + " Translated by attendant"));
                }
                if (c.type().equals(CardType.BACKSTART)
                        || c.type().equals(CardType.CBACKSTART)) {
                    // It's a loop
                    CardType newType;
                    if (depends) {
                        newType = CardType.CBACKWARD;
                    } else {
                        newType = CardType.BACKWARD;
                    }
                    String[] newArgs = { Integer.toString(i - start) };
                    cards.add(i, new Card(newType, newArgs));
                    LOG.debug("  (replaced with " + cards.get(i) + ")");
                    return;
                }

                // If we've reached this point, it's a forward skip, possibly
                // with an else clause
                CardType newType;
                if (depends) {
                    newType = CardType.CFORWARD;
                } else {
                    newType = CardType.FORWARD;
                }

                boolean isElse = u.type().equals(CardType.ALTERNATION);
                int argument;
                if (isElse) {
                    if (this.stripComments) {
                        argument = 0;
                    } else {
                        argument = 1;
                    }
                } else {
                    if (this.stripComments) {
                        argument = -1;
                    } else {
                        argument = 0;
                    }
                }
                argument += Math.abs(i - start);
                String[] newArgs = { Integer.toString(argument) };
                cards.add(start + 1, new Card(newType, newArgs));

                // Translate else branch of conditional, if present
                if (isElse) {
                    int j;

                    for (j = i + 1; j < cards.size(); j++) {
                        u = cards.get(j);
                        CardType uType = u.type();
                        if (isCycleStart(uType)) {
                            translateCycle(cards, j);
                        }
                        if (isCycleEnd(uType)) {
                            if (!uType.equals(CardType.FORWARDEND)
                                    && !uType.equals(CardType.ALTERNATION)) {
                                throw new BadCard(
                                        "End of else cycle does not match " + c
                                                + " beginning on card " + i,
                                        u);
                            }
                            cards.remove(j);
                            if (!this.stripComments) {
                                cards.add(j, Card.commentCard(
                                        u + " Translated by attendant"));
                            }
                            CardType newType2 = CardType.FORWARD;
                            String[] newArgs2 = {
                                    Integer.toString(Math.abs(j - i) - 1) };
                            int location = i;
                            if (this.stripComments) {
                                location += 1;
                            } else {
                                location += 2;
                            }
                            cards.add(location, new Card(newType2, newArgs2));
                            return;
                        }
                    }
                }
                return;
            }
        }
        for (Card card : cards) {
            LOG.debug(card.toString());
        }
        throw new BadCard("No matching end of cycle.", c);
    }

    /**
     * {@inheritDoc}
     * 
     * @param direction
     *            {@inheritDoc}
     */
    @Override
    public void writeInDirection(final WriteDirection direction) {
        this.writeInRows = direction == WriteDirection.ROWS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeNewline() {
        this.report += System.getProperty("line.separator");
    }

}
