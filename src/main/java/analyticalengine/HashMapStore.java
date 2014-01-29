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
import java.util.Map;
import java.util.HashMap;

public class HashMapStore implements Store {

    public static final int MAX_ADDRESS = 1000;
    
    @Override
    public int maxAddress() {
        return MAX_ADDRESS;
    }
    
    private Map<Long, BigInteger> rack = new HashMap<Long, BigInteger>();
    
    @Override
    public void put(long address, BigInteger value) {
        this.rack.put(address, value);
    }

    @Override
    public BigInteger get(long address) {
        return this.rack.get(address);
    }

    @Override
    public void reset() {
        this.rack.clear();
    }

}
