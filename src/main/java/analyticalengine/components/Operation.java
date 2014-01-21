package analyticalengine.components;

public enum Operation {
    ADD(2), SUBTRACT(2), MULTIPLY(2), DIVIDE(2), NOOP(0);

    private final int numArguments;
    
    private Operation(int numArguments) {
        this.numArguments = numArguments;
    }
    
    public int numArguments() {
        return 0;
    }
}
