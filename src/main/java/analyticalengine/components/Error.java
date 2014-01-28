/**
 * Error.java - exception raised when engine encounters a bad instruction
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
package analyticalengine.components;

import analyticalengine.components.cards.Card;

// TODO remove String only constructor
public class Error extends CardException {

    /**
     * Default generated serial version UID.
     */
    private static final long serialVersionUID = -4000642169973972421L;

    public Error(String message) {
        super(message);
    }

    public Error(String message, Exception cause) {
        super(message, cause);
    }

    public Error(String message, Card cause) {
        super(message, cause);
    }

}
