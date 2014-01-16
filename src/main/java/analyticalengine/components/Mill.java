package analyticalengine.components;

import java.math.BigInteger;

public interface Mill {
    void transferIn(BigInteger value);
    BigInteger transferOut();
    boolean hasRunUp();
}
