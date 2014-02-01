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
import java.util.HashMap;
import java.util.Map;

/**
 * A memory store for the Analytical Engine backed by a
 * {@link java.util.HashMap}.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @since 0.0.1
 */
public class HashMapStore implements Store {

    /** The maximum number of cells in the store. */
    public static final int MAX_ADDRESS = 1000;

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public int maxAddress() {
        return MAX_ADDRESS;
    }

    /** The hash map that provides the addressable, random-access storage. */
    private Map<Long, BigInteger> rack = new HashMap<Long, BigInteger>();

    /**
     * {@inheritDoc}
     * 
     * @param address
     *            {@inheritDoc}
     * @param value
     *            {@inheritDoc}
     */
    @Override
    public void put(final long address, final BigInteger value) {
        this.rack.put(address, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @param address
     *            {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public BigInteger get(final long address) {
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
