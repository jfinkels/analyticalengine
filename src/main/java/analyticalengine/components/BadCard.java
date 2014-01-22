/**
 * BadCard.java - 
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
