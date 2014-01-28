/**
 * BadCard.java - exception raised when attendant finds a bad instruction
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

/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
// this is essentially a syntax error
public class BadCard extends CardException {

    /**
     * 
     */
    private static final long serialVersionUID = -3311831632309378417L;

    /**
     * @param message
     * @param cause
     */
    public BadCard(String message, Card cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
