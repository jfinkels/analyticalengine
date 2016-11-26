/**
 * UnknownCard.java - exception raised when card cannot be parsed from string
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
package analyticalengine.cards;

/**
 * This exception is raised when an attempt to parse a string into a card
 * fails.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class UnknownCard extends Exception {

    /**
     * A default generated serial version UID.
     */
    private static final long serialVersionUID = -5748344376227284027L;

    /**
     * Creates a new exception with the specified error message.
     * 
     * @param string
     *            The error message.
     */
    public UnknownCard(final String string) {
        super(string);
    }

}
