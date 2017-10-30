/**
 * ArrayStore.java - a RAM storing integers, backed by an Array
 * 
 * Copyright 2014-2016 Jeffrey Finkelstein.
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
package analyticalengine.components;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * A memory store for the Analytical Engine backed by an array.
 * 
 * The {@link #MAX_VALUE} and {@link #MIN_VALUE} fields of this store should
 * agree with the {@link analyticalengine.components.DefaultMill#MAX} and
 * {@link analyticalengine.components.DefaultMill#MIN} fields of
 * {@link analyticalengine.components.DefaultMill}.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class ArrayStore implements Store {

    /** The maximum number of cells in the store. */
    public static final int MAX_ADDRESS = 1000;

    /** The width (number of digits) of an integer that can be stored. */
    public static final int WIDTH = 50;

    /** The maximum value of an integer that can be stored. */
    public static final BigInteger MAX_VALUE = BigInteger.TEN.pow(WIDTH)
            .subtract(BigInteger.ONE);

    /** The minimum value of an integer that can be stored. */
    public static final BigInteger MIN_VALUE = MAX_VALUE.negate();

    /** The array that provides the addressable, random-access storage. */
    private final BigInteger[] rack = new BigInteger[MAX_ADDRESS];

    /**
     * Instantiates this object and initializes the underlying array list.
     */
    public ArrayStore() {
        this.reset();
    }

    /**
     * {@inheritDoc}
     * 
     * @param address
     *            {@inheritDoc}
     * @param value
     *            {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     */
    @Override
    public void put(final int address, final BigInteger value) {
        if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Value " + value
                    + " must be between " + MIN_VALUE + " and " + MAX_VALUE);
        }
        this.rack[address] = value;
    }

    /**
     * {@inheritDoc}
     * 
     * @param address
     *            {@inheritDoc}
     * @return {@inheritDoc}
     * @throws IndexOutOfBoundsException
     *             {@inheritDoc}
     */
    @Override
    public BigInteger get(final int address) {
        return this.rack[address];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        Arrays.fill(this.rack, BigInteger.ZERO);
    }

}
