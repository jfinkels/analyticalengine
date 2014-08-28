/**
 * HashMapStore.java - a RAM storing integers, backed by a HashMap
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
 * A memory store for the Analytical Engine backed by a
 * {@link java.util.HashMap}.
 * 
 * The {@link #MAX_VALUE} and {@link #MIN_VALUE} fields of this store should
 * agree with the {@link analyticalengine.DefaultMill#MAX} and
 * {@link analyticalengine.DefaultMill#MIN} fields of
 * {@link analyticalengine.DefaultMill}.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class HashMapStore implements Store {

    /** The maximum number of cells in the store. */
    public static final int MAX_ADDRESS = 1000;

    /** The width (number of digits) of an integer that can be stored. */
    public static final int WIDTH = 50;

    /** The maximum value of an integer that can be stored. */
    public static final BigInteger MAX_VALUE = BigInteger.TEN.pow(WIDTH)
            .subtract(BigInteger.ONE);

    /** The minimum value of an integer that can be stored. */
    public static final BigInteger MIN_VALUE = MAX_VALUE.negate();

    /** The hash map that provides the addressable, random-access storage. */
    private final DefaultHashMap<Long, BigInteger> rack = new DefaultHashMap<Long, BigInteger>();

    /**
     * An object which simply generates zero values (of type
     * {@link java.math.BigInteger} ).
     * 
     * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
     * @since 0.0.1
     */
    private static final class ZeroGenerator implements
            ValueGenerator<Long, BigInteger> {

        /** Default generated serial version UID. */
        private static final long serialVersionUID = 4997265095366224345L;

        /**
         * Always returns {@link java.math.BigInteger#ZERO}.
         * 
         * @param key
         *            This parameter is ignored.
         * @return Zero.
         */
        @Override
        public BigInteger generate(final Long key) {
            return BigInteger.ZERO;
        }
    };

    /**
     * Instantiates this object and initializes the underlying hash map.
     */
    public HashMapStore() {
        this.rack.setValueGenerator(new ZeroGenerator());
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
    public void put(final long address, final BigInteger value) {
        if (address < 0 || address > MAX_ADDRESS) {
            throw new IndexOutOfBoundsException("Address " + address
                    + " must be between " + 0 + " and " + MAX_ADDRESS);
        }
        if (value.compareTo(MIN_VALUE) < 0 || value.compareTo(MAX_VALUE) > 0) {
            throw new IllegalArgumentException("Value " + value
                    + " must be between " + MIN_VALUE + " and " + MAX_VALUE);
        }
        this.rack.put(address, value);
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
    public BigInteger get(final long address) {
        if (address < 0 || address > MAX_ADDRESS) {
            throw new IndexOutOfBoundsException("Bad address: " + address);
        }
        return this.rack.get(address);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        this.rack.clear();
    }

}
