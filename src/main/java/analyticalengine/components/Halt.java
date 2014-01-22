package analyticalengine.components;

import analyticalengine.components.cards.Card;

public class Halt extends CardException {

    public Halt(String string) {
        super(string);
    }

    public Halt(String message, Exception cause) {
        super(message, cause);
    }

    public Halt(String message, Card cause) {
        super(message, cause);
    }

}
