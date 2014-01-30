/**
 * CardException.java - exception that stores the card that caused it
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
 * Base class for exceptions that are caused by cards.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public abstract class CardException extends Exception {
    /**
     * Default generated serial version UID.
     */
    private static final long serialVersionUID = -590810062640328181L;

    /** The card that caused this exception. */
    private Card card = null;

    /**
     * Instantiates this exception with the specified error message.
     * 
     * @param message
     *            The error message.
     */
    public CardException(final String message) {
        super(message);
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
    public CardException(final String message, final Card cause) {
        super(message);
        this.card = cause;
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
    public CardException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Gets the card that caused this exception.
     * 
     * @return The card that caused this exception.
     */
    public Card card() {
        return this.card;
    }
}
