/**
 * DefaultAttendant.java - basic implementation of the operator of the engine
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

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import analyticalengine.io.UnknownCard;

public class DefaultAttendant implements Attendant {

    private static final boolean isCycleEnd(CardType type) {
        return type == CardType.BACKEND || type == CardType.ALTERNATION
                || type == CardType.FORWARDEND;
    }

    private static final boolean isCycleStart(CardType type) {
        return type == CardType.BACKSTART || type == CardType.CBACKSTART
                || type == CardType.FORWARDSTART
                || type == CardType.CFORWARDSTART;
    }

    private static List<Card> strippedComments(List<Card> cards) {
        List<Card> result = new ArrayList<Card>();
        for (Card card : cards) {
            if (card.type() != CardType.COMMENT) {
                result.add(card);
            }
        }
        return result;
    }

    private CardReader cardReader = null;

    private String formatString = null;

    private List<Path> libraryPaths = Collections.emptyList();

    private String report = "";

    private boolean stripComments = false;

    private boolean writeInRows = true;

    private boolean writeWithDecimal;

    @Override
    public void annotate(String message) {
        if (this.writeInRows) {
            this.report += message;
        } else {
            // TODO do something different
        }
    }

    private List<Card> expandDecimal(List<Card> cards) throws BadCard {
        List<Card> result = new ArrayList<Card>();
        int decimalPlace = -1;
        for (Card card : cards) {
            switch (card.type()) {
            // A set decimal places to [+/-]n
            case DECIMALEXPAND:
                String dspec = card.argument(0);
                int relative = 0;
                boolean dspok = true;

                if (dspec.charAt(0) == '+' || dspec.charAt(0) == '-') {
                    if (decimalPlace == -1) {
                        dspok = false;
                        throw new BadCard(
                                "I cannot accept a relative decimal place setting\nwithout a prior absolute setting.",
                                card);
                    } else {
                        relative = (dspec.charAt(0) == '+')
                                                           ? 1
                                                               : -1;
                        dspec = dspec.substring(1);
                    }
                }
                if (dspok) {
                    try {
                        int d = Integer.parseInt(dspec);
                        if (relative != 0) {
                            d = decimalPlace + (d * relative);
                        }
                        if (d < 0 || d > 50) {
                            throw new BadCard(
                                    "I can only set the decimal place between 0 and 50 digits.",
                                    card);
                        }

                        if (!this.stripComments) {
                            Card replacement = Card
                                    .commentCard("A set decimal places to "
                                            + card.argument(0));
                            result.add(replacement);
                        }
                        decimalPlace = d;
                    } catch (NumberFormatException ne) {
                        throw new BadCard(
                                "I cannot find the number of decimal places you wish to use.",
                                card);
                    }
                }
                break;
            // Convert "A write numbers with decimal point" to a picture
            case WRITEDECIMAL: {
                int dpa;

                if (decimalPlace < 0) {
                    throw new BadCard(
                            "I cannot add the number of decimal places because\n"
                                    + "you have not instructed me how many decimal\n"
                                    + "places to use in a prior \"A set decimal places to\"\ninstruction.",
                            card);
                }

                StringBuilder formatString = new StringBuilder("9.");
                for (dpa = 0; dpa < decimalPlace; dpa++) {
                    formatString.append("9");
                }

                Card replacement = new Card(CardType.WRITEPICTURE,
                        new String[] { formatString.toString() });
                result.add(replacement);

                break;
            }
            /*
             * Replace number cards with decimal points with cards scaled to
             * the proper number of digits.
             */
            case NUMBER: {
                String cn = card.argument(0);
                String nspec = card.argument(1);
                int dp = nspec.indexOf(".");
                if (dp >= 0) {
                    if (decimalPlace < 0) {
                        throw new BadCard(
                                "I cannot add the number of decimal places because\n"
                                        + "you have not instructed me how many decimal\n"
                                        + "places to use in a prior \"A set decimal places\"\ninstruction.",
                                card);
                    } else {
                        String dpart = nspec.substring(dp + 1);
                        // dpnew = "";
                        // int j, places = 0;
                        // char ch;
                        //
                        // for (j = 0; j < dpart.length(); j++) {
                        // if (Character.isDigit(ch = dpart.charAt(j)))
                        // {
                        // dpnew += ch;
                        // places++;
                        // }
                        // }

                        /*
                         * Now adjust the decimal part to the given number of
                         * decimal places by trimming excess digits and
                         * appending zeroes as required.
                         */

                        if (dpart.length() > decimalPlace) {
                            /*
                             * If we're trimming excess digits, round the
                             * remaining digits based on the first digit of the
                             * portion trimmed.
                             */
                            if ((decimalPlace > 0)
                                    && (Character.digit(
                                            dpart.charAt(decimalPlace), 10) >= 5)) {
                                dpart = new BigInteger(dpart.substring(0,
                                        decimalPlace), 10).add(BigInteger.ONE)
                                        .toString();
                            } else {
                                dpart = dpart.substring(0, decimalPlace);
                            }
                        }
                        while (dpart.length() < decimalPlace) {
                            dpart += "0";
                        }

                        // Append the decimal part to fixed part from
                        // card
                        String newNumber = "N" + cn + " "
                                + nspec.substring(0, dp) + dpart;
                        if (!this.stripComments) {
                            newNumber += " . Decimal expansion by attendant";
                        }

                        Card replacement = new Card(CardType.NUMBER,
                                new String[] { newNumber });
                        result.add(replacement);
                    }
                } else {
                    // Just add the card as it is
                    result.add(card);
                }

                break;
            }
            // Add step up/down to "<" or ">" if not specified
            case LSHIFT:
            case RSHIFT:
                if (decimalPlace < 0) {
                    throw new BadCard(
                            "I cannot add the number of decimal places because\n"
                                    + "you have not instructed me how many decimal\n"
                                    + "places to use in a prior \"A set decimal places\"\ninstruction.",
                            card);
                }

                String newArg = null;
                CardType newType = null;
                if (card.type() == CardType.LSHIFT) {
                    newArg = "<";
                    newType = CardType.LSHIFTN;
                } else {
                    newArg = ">";
                    newType = CardType.RSHIFTN;
                }

                newArg += String.valueOf(decimalPlace);
                if (!this.stripComments) {
                    newArg += " . Step count added by attendant";
                }
                Card replacement = new Card(newType, new String[] { newArg });
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

    @Override
    public String finalReport() {
        return this.report;
    }

    private String formatted(String input) {
        String s = input;
        boolean negative = (input.charAt(0) == '-');
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
                    if ((this.formatString.indexOf('9') >= 0)
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
                    o = (negative
                                 ? '-'
                                     : '+') + o;
                    sign = true;
                    break;

                case '+': // Sign if negative, space if positive
                    o = (negative
                                 ? '-'
                                     : ' ') + o;
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

    @Override
    public void loadProgram(List<Card> cards) throws BadCard, IOException,
            UnknownCard {
        // Note: "A write numbers as ..." cards remain in the card chain.
        cards = this.transcludeLibraryCards(cards);
        cards = strippedComments(cards);
        cards = this.expandDecimal(cards);
        // TODO this method should return a new list like the others do
        translateCombinatorics(cards);
        this.cardReader.mountCards(cards);
    }

    @Override
    public void receiveOutput(String printed) {
        String toWrite = this.formatted(printed);
        if (this.writeInRows) {
            this.report += toWrite;
        } else {
            // TODO do something different
        }
    }

    @Override
    public void reset() {
        this.report = "";
        this.cardReader.unmountCards();
    }

    @Override
    public void setCardReader(CardReader reader) {
        this.cardReader = reader;
    }

    @Override
    public void setLibraryPaths(List<Path> paths) {
        Collections.copy(this.libraryPaths, paths);
    }

    /*
     * (non-Javadoc)
     * 
     * @see analyticalengine.components.Attendant#setStripComments(boolean)
     */
    @Override
    public void setStripComments(boolean stripComments) {
        this.stripComments = stripComments;
    }

    private List<Card> transcludeLibraryCards(List<Card> cards)
            throws BadCard, IOException, UnknownCard {
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

                result.add(Card.commentCard("Begin interpolation of " + card
                        + " by attendant"));
                result.addAll(analyticalengine.io.CardReader.fromPath(path));
                result.add(Card.commentCard("Endinterpolation of " + card
                        + " by attendant"));
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
                // we assume library files have the .ae file extension
                String filename = card.argument(0) + ".ae";

                // look for the file in the known paths
                Path libraryFile = null;
                for (Path path : this.libraryPaths) {
                    libraryFile = path.resolve(filename);
                    if (Files.exists(libraryFile)) {
                        break;
                    }
                    libraryFile = null;
                }

                // raise an exception if the requested library file could not
                // be found in any of the known paths
                if (libraryFile == null) {
                    throw new BadCard("Could not find library file: "
                            + filename, card);
                }

                result.add(Card.commentCard("Begin interpolation of "
                        + libraryFile + " from library by attendant"));
                result.addAll(analyticalengine.io.CardReader
                        .fromPath(libraryFile));
                result.add(Card.commentCard("End interpolation of "
                        + libraryFile + " from library by attendant"));
            } else {
                result.add(card);
            }
        }

        return result;
    }

    private void translateCombinatorics(List<Card> cards) throws BadCard {
        for (int i = 0; i < cards.size(); i++) {
            if (isCycleStart(cards.get(i).type())) {
                this.translateCycle(cards, i);
            }
        }
    }

    private void translateCycle(List<Card> cards, int start) throws BadCard {
        Card c = cards.get(start);
        boolean depends = false;
        int i;

        depends = (c.type() == CardType.CBACKSTART || c.type() == CardType.CFORWARDSTART);
        if (this.stripComments) {
            cards.remove(start);
            start--;
        } else {
            cards.remove(start);
            cards.add(start,
                    Card.commentCard(". " + c + " Translated by attendant"));
        }

        /*
         * Search for the end of this cycle. If a sub-cycle is detected,
         * recurse to translate it.
         */

        for (i = start + 1; i < cards.size(); i++) {
            Card u = cards.get(i);

            if (isCycleStart(u.type())) {
                translateCycle(cards, i);
            }
            if (isCycleEnd(u.type())) {
                boolean isElse = u.type() == CardType.ALTERNATION;

                if (((c.type() == CardType.CBACKSTART || c.type() == CardType.BACKSTART) && u
                        .type() != CardType.BACKEND)
                        || ((c.type() == CardType.CFORWARDSTART || c.type() == CardType.FORWARDSTART) && (u
                                .type() != CardType.FORWARDEND || u.type() != CardType.ALTERNATION))) {
                    throw new BadCard("End of cycle does not match " + c
                            + " beginning on card " + start, u);
                }
                cards.remove(i);
                if (!this.stripComments) {
                    cards.remove(i);
                    cards.add(
                            start,
                            Card.commentCard(". " + u
                                    + " Translated by attendant"));
                }
                if (c.type() == CardType.BACKSTART) {
                    // It's a loop
                    CardType newType = depends
                                              ? CardType.CBACKWARD
                                                  : CardType.BACKWARD;
                    String[] newArgs = { Integer.toString(i - start) };
                    cards.add(i, new Card(newType, newArgs));
                } else {
                    // It's a forward skip, possibly with an else clause
                    CardType newType = depends
                                              ? CardType.CFORWARD
                                                  : CardType.FORWARD;
                    String[] newArgs = { Integer
                            .toString(((isElse
                                              ? (this.stripComments
                                                                   ? 0
                                                                       : 1)
                                                  : (this.stripComments
                                                                       ? -1
                                                                           : 0)) + Math
                                    .abs(i - start))) };
                    cards.add(start + 1, new Card(newType, newArgs));

                    // Translate else branch of conditional, if present
                    if (isElse) {
                        int j;

                        for (j = i + 1; j < cards.size(); j++) {
                            u = cards.get(j);

                            if (isCycleStart(u.type())) {
                                translateCycle(cards, j);
                            }
                            if (isCycleEnd(u.type())) {
                                if (u.type() != CardType.FORWARDEND
                                        || u.type() != CardType.ALTERNATION) {
                                    throw new BadCard(
                                            "End of else cycle does not match "
                                                    + c
                                                    + " beginning on card "
                                                    + i, u);
                                }
                                cards.remove(j);
                                if (!this.stripComments) {
                                    cards.add(j, Card.commentCard(u
                                            + " Translated by attendant"));
                                }
                                CardType newType2 = CardType.FORWARD;
                                String[] newArgs2 = { Integer.toString(Math
                                        .abs(j - i)
                                        - (this.stripComments
                                                             ? 1
                                                                 : 1)) };
                                cards.add(i + (this.stripComments
                                                                 ? 1
                                                                     : 2),
                                        new Card(newType2, newArgs2));
                                return;
                            }
                        }
                    }
                }
            }
        }
        throw new BadCard("No matching end of cycle.", c);
    }

    @Override
    public void writeInColumns() {
        this.writeInRows = false;
    }

    @Override
    public void writeInRows() {
        this.writeInRows = true;
    }

    @Override
    public void writeNewline() {
        this.report += System.getProperty("line.separator");
    }

    @Override
    public void writeWithDecimal() {
        this.writeWithDecimal = true;
    }

    @Override
    public void setFormat(String formatString) {
        this.formatString = formatString;
    }

}
