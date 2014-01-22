package analyticalengine.components;

import analyticalengine.components.cards.Card;

public class Error extends CardException {

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
