package analyticalengine.components;

import java.math.BigInteger;

public interface CurvePrinter {
    void setX(BigInteger x);

    void setY(BigInteger y);

    void draw();

    void move();
    
    void reset();
}
