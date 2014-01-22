package analyticalengine.components;

import java.math.BigInteger;

public class StringPrinter implements Printer {

    @Override
    public String print(BigInteger value) {
        return value.toString();
    }

}
