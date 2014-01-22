package analyticalengine.components.gui;

import java.math.BigInteger;

import analyticalengine.components.CurvePrinter;

public class AWTCurvePrinter implements CurvePrinter {

    private BigInteger x;
    private BigInteger y;
    
    @Override
    public void setX(BigInteger x) {
        this.x = x;
    }

    @Override
    public void setY(BigInteger y) {
        this.y = y;
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub

    }

    @Override
    public void move() {
        // TODO Auto-generated method stub

    }

}
