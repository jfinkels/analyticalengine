package analyticalengine.components;

import java.math.BigInteger;

// the mill is the ALU of the engine
public interface Mill {
    boolean hasRunUp();

    void leftShift(int shift);

    BigInteger maxValue();
    BigInteger minValue();
    
    void rightShift(int shift);

    void setOperation(Operation operation);

    //void transferIn(BigInteger value);

    void transferIn(BigInteger value, boolean prime);

    BigInteger transferOut();

    BigInteger transferOut(boolean prime);

    BigInteger mostRecentValue();
}
