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
package analyticalengine.components.cards;

import java.util.Arrays;

public class Card {
    private CardType type;
    private String[] arguments;

    public static Card commentCard(String comment) {
        return new Card(CardType.COMMENT, new String[] { ". " + comment });
    }

    public Card(CardType type) {
        this(type, new String[] {});
    }

    public Card(CardType type, String[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    //public String[] arguments() {
    //    return Arrays.copyOf(this.arguments, type.numArguments());
    //}
    
    public int numArguments() {
        return this.type.numArguments();
    }

    public CardType type() {
        return this.type;
    }

    public String argument(int i) throws IndexOutOfBoundsException {
        return this.arguments[i];
    }
    
    @Override
    public String toString() {
        return this.type + Arrays.toString(this.arguments);
    }
}
