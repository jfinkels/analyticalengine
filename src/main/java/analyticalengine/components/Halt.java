package analyticalengine.components;

import analyticalengine.components.cards.Card;

public class Halt extends CardException {

    /**
     * Default generated serial version UID.  
     */
    private static final long serialVersionUID = -8026821124810632777L;

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
