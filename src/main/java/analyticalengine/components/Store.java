package analyticalengine.components;

import java.math.BigInteger;

/**
 * The memory of the Analytical Engine; stores values in random access memory.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 */
public interface Store {
    void reset();
    int maxAddress();
    /**
     * Stores {@code value} in the memory location specified by {@code address}
     * .
     * 
     * @param address
     *            A memory location.
     * @param value
     *            A value to store in {@code address}.
     */
    void put(long address, BigInteger value);

    /**
     * Retrieves and returns the value stored at {@code address}.
     * 
     * @param address
     *            A memory location.
     * @return The value stored at the memory location.
     */
    BigInteger get(long address);
}
