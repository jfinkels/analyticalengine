package analyticalengine.components;

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
