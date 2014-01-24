package analyticalengine.components.cards;

public enum CardType {
    ADD(0), ALTERNATION(0), ANNOTATE(1), BACKEND(0), BACKSTART(0), BACKWARD(1), BELL(
            0), CBACKSTART(0), CBACKWARD(1), CFORWARD(0), CFORWARDSTART(0), COMMENT(
            1), DECIMALEXPAND(1), DIVIDE(0), DRAW(0), FORWARD(0), FORWARDEND(0), FORWARDSTART(
            0), HALT(0), INCLUDE(1), INCLUDELIB(1), LOAD(1), LOADPRIME(1), LSHIFT(
            0), LSHIFTN(1), MOVE(0), MULTIPLY(0), NEWLINE(0), NUMBER(2), PRINT(
            0), RSHIFT(0), RSHIFTN(1), SETX(0), SETY(0), STORE(1), STOREPRIME(
            1), SUBTRACT(0), TRACEOFF(0), TRACEON(0), WRITECOLUMNS(0), WRITEDECIMAL(
            0), WRITEPICTURE(1), WRITEROWS(0), ZLOAD(1), ZLOADPRIME(1);

    private int numArguments;

    CardType(int numArguments) {
        this.numArguments = numArguments;
    }

    public int numArguments() {
        return this.numArguments;
    }
}
