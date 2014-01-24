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