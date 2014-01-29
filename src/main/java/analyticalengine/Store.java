/**
 * Store.java - random-access memory for the engine
 * 
 * Copyright 2014 Jeffrey Finkelstein.
 * 
 * This file is part of analyticalengine.
 * 
 * analyticalengine is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 * 
 * analyticalengine is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * analyticalengine. If not, see <http://www.gnu.org/licenses/>.
 */
package analyticalengine;

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
