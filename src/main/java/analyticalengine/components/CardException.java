package analyticalengine.components;

import analyticalengine.components.cards.Card;

public class CardException extends Exception {
    /**
     * Default generated serial version UID.
     */
    private static final long serialVersionUID = -590810062640328181L;
    private Card cause = null;

    public CardException(String message, Card cause) {
        super(message);
        this.cause = cause;
    }

    public CardException(String message) {
        super(message);
    }

    public CardException(String message, Exception cause) {
        super(message, cause);
    }
}
