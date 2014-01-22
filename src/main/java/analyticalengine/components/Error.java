package analyticalengine.components;

import analyticalengine.components.cards.Card;

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
