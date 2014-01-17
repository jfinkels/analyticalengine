package analyticalengine.components.cards;

public enum CardType {
    ADD(2), ADVANCE(1), ANNOTATE(2), BELL(0), COMMENT(1), DECIMALEXPAND(1), DIVIDE(
            2), DRAWSETX(1), DRAWSETY(1), DRAWTO(0), HALT(1), INCLUDE(1), LOAD(
            1), LSHIFT(1), MOVETO(0), MULTIPLY(2), NUMBER(1), PRINT(0), REVERSE(
            1), RSHIFT(1), STORE(1), SUBTRACT(2), TRACE(1), ZLOAD(1);
    // TODO fill in the rest (combinatorial cards, eg.)

    private int numArguments;

    CardType(int numArguments) {
        this.numArguments = numArguments;
    }

    public int numArguments() {
        return this.numArguments;
    }
}
