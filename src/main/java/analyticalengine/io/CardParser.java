/**
 * CardParser.java - parses a string representation of a card
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

import analyticalengine.cards.Card;

/**
 * Parses a card from a string.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public final class CardParser {

    /**
     * Instantiation is disallowed.
     */
    private CardParser() {
        // intentionally unimplemented
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
    public static String toString(final Card card) throws UnknownCard {
        // TODO account for comments after the instructions
        switch (card.type()) {
        case ADD:
            return "+";
        case ALTERNATION:
            return "}{";
        case ANNOTATE:
            return "A write annotation " + card.argument(0);
        case BACKEND:
            return ")";
        case BACKSTART:
            return "(";
        case BACKWARD:
            return "CB+" + card.argument(0);
        case BELL:
            return "B";
        case CBACKSTART:
            return "(?";
        case CBACKWARD:
            return "CB?" + card.argument(0);
        case CFORWARD:
            return "CF?" + card.argument(0);
        case CFORWARDSTART:
            return "{?";
        case COMMENT:
            return "  " + card.argument(0);
        case DECIMALEXPAND:
            return "A set decimal places to " + card.argument(0);
        case DIVIDE:
            return "/";
        case DRAW:
            return "D+";
        case FORWARD:
            return "CF+" + card.argument(0);
        case FORWARDEND:
            return "}";
        case FORWARDSTART:
            return "{";
        case HALT:
            return "H";
        case INCLUDE:
            return "A include cards " + card.argument(0);
        case INCLUDELIB:
            return "A include from library cards for " + card.argument(0);
        case LOAD:
            return "L" + card.argument(0);
        case LOADPRIME:
            return "L" + card.argument(0) + ";";
        case LSHIFT:
            return "<";
        case LSHIFTN:
            return "<" + card.argument(0);
        case MOVE:
            return "D-";
        case MULTIPLY:
            return "*";
        case NEWLINE:
            return "A write new line";
        case NUMBER:
            return "N" + card.argument(0) + " " + card.argument(1);
        case PRINT:
            return "P";
        case RSHIFT:
            return ">";
        case RSHIFTN:
            return ">" + card.argument(0);
        case SETX:
            return "DX";
        case SETY:
            return "DY";
        case STORE:
            return "S" + card.argument(0);
        case STOREPRIME:
            return "S" + card.argument(0) + "'";
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
            return "A write numbers as " + card.argument(0);
        case WRITEROWS:
            return "A write in rows";
        case ZLOAD:
            return "Z" + card.argument(0);
        case ZLOADPRIME:
            return "Z" + card.argument(0) + "'";
        default:
            throw new UnknownCard("Unable to parse: " + card);
        }
    }
}
