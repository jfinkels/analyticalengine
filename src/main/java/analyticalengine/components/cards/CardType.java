package analyticalengine.components.cards;

public enum CardType {
    ADD(2), CFORWARD(1), FORWARD(1), ANNOTATE(0), BELL(0), COMMENT(1), DECIMALEXPAND(
            1), DIVIDE(2), SETX(0), SETY(0), DRAW(0), HALT(1), INCLUDE(1), INCLUDELIB(1), LOAD(
            1), LOADPRIME(1), LSHIFT(1), MOVE(0), MULTIPLY(2), NUMBER(1), PRINT(
            0), CBACKWARD(1), BACKWARD(1), RSHIFT(1), STORE(1), STOREPRIME(1), SUBTRACT(
            2), TRACEON(0), TRACEOFF(0), ZLOAD(1), ZLOADPRIME(1), BACKSTART(0), CBACKSTART(
            0), BACKEND(0), FORWARDSTART(0), CFORWARDSTART(0), FORWARDEND(0), ALTERNATION(
            0), WRITEDECIMAL(1), WRITEPICTURE(1), WRITEROWS(0), WRITECOLUMNS(0), NEWLINE(0);
    // TODO fill in the rest (combinatorial cards, eg.)

    private int numArguments;

    CardType(int numArguments) {
        this.numArguments = numArguments;
    }

    public int numArguments() {
        return this.numArguments;
    }
}
