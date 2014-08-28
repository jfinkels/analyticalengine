/**
 * Halt.java - exception raised when engine indicates halt (or no more cards)
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

import analyticalengine.cards.Card;

/**
 * This exception is raised when the Analytical Engine stops executing its
 * program, either because the program includes a "halt" instruction, or the
 * card reader has read every available card.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class Halt extends CardException {

    /**
     * Default generated serial version UID.
     */
    private static final long serialVersionUID = -8026821124810632777L;

    /**
     * Instantiates this exception with the specified error message.
     * 
     * @param message
     *            The error message.
     */
    public Halt(final String message) {
        super(message);
    }

    /**
     * Instantiates this exception with the specified error message and the
     * specified exception that caused the exception.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The throwable that caused this exception.
     */
    public Halt(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates this exception with the specified error message and the
     * specified card that caused the exception.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The card that caused this exception.
     */
    public Halt(final String message, final Card cause) {
        super(message, cause);
    }

}
