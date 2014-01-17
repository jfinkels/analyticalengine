package analyticalengine.components.cards;

import java.util.Arrays;

public class Card {
    private CardType type;
    private String[] arguments;
    public Card(CardType type, String[] arguments) {
        this.type = type;
        this.arguments = arguments;
    }
    
    public String[] arguments() {
        return Arrays.copyOf(this.arguments, type.numArguments());
    }
    
    public CardType type() {
        return this.type;
    }
}
