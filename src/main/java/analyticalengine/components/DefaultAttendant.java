package analyticalengine.components;

import java.math.BigInteger;
import java.util.StringTokenizer;

import analyticalengine.components.cards.Card;
import analyticalengine.components.cards.CardChain;
import analyticalengine.components.cards.CardType;
import analyticalengine.newio.ArrayListCardChain;

public class DefaultAttendant implements Attendant {

    private CardReader cardReader = null;
    private String report = "";
    private boolean writeInRows = true;

    @Override
    public void reset() {
        this.report = "";
        this.cardReader.unmountCards();
    }

    private static CardChain strippedComments(CardChain cards) {
        CardChain result = new ArrayListCardChain();
        for (Card card : cards) {
            if (card.type() != CardType.COMMENT) {
                result.add(card);
            }
        }
        return result;
    }

    private CardChain expandDecimal(CardChain cards) throws BadCard {
        CardChain result = new ArrayListCardChain();
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
                            Card replacement = new Card(CardType.COMMENT,
                                    new String[] { "A set decimal places to "
                                            + card.argument(0) });
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
                StringTokenizer stok = new StringTokenizer(card.argument(0)
                        .substring(1));
                if (stok.countTokens() == 2) {
                    String cn, nspec;

                    cn = stok.nextToken();
                    nspec = stok.nextToken();
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
                             * Now adjust the decimal part to the given number
                             * of decimal places by trimming excess digits and
                             * appending zeroes as required.
                             */

                            if (dpart.length() > decimalPlace) {
                                /*
                                 * If we're trimming excess digits, round the
                                 * remaining digits based on the first digit of
                                 * the portion trimmed.
                                 */
                                if ((decimalPlace > 0)
                                        && (Character
                                                .digit(dpart
                                                        .charAt(decimalPlace),
                                                        10) >= 5)) {
                                    dpart = new BigInteger(dpart.substring(0,
                                            decimalPlace), 10).add(
                                            BigInteger.ONE).toString();
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
                    }
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

    private static CardChain translateCombinatorics(CardChain cards) {
        return cards;
    }

    @Override
    public void loadProgram(CardChain cards) throws BadCard {
        // Note: "A write numbers as ..." cards remain in the card chain.
        cards = strippedComments(cards);
        cards = this.expandDecimal(cards);
        cards = translateCombinatorics(cards);
        this.cardReader.mountCards(cards);
    }

    private String formatString = null;

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
    public void receiveOutput(String printed) {
        String toWrite = this.formatted(printed);
        if (this.writeInRows) {
            this.report += toWrite;
        } else {
            // TODO do something different
        }
    }

    @Override
    public void annotate(String message) {
        if (this.writeInRows) {
            this.report += message;
        } else {
            // TODO do something different
        }
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
    public String finalReport() {
        return this.report;
    }

    @Override
    public void writeNewline() {
        this.report += System.getProperty("line.separator");
    }

    @Override
    public void setCardReader(CardReader reader) {
        this.cardReader = reader;
    }

    private boolean stripComments = false;

    /*
     * (non-Javadoc)
     * 
     * @see analyticalengine.components.Attendant#setStripComments(boolean)
     */
    @Override
    public void setStripComments(boolean stripComments) {
        this.stripComments = stripComments;
    }

}
