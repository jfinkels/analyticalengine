package analyticalengine.components;

import java.math.BigInteger;
import java.util.Map;
import java.util.HashMap;

public class HashMapStore implements Store {

    private Map<Long, BigInteger> rack = new HashMap<Long, BigInteger>();
    
    @Override
    public void set(long address, BigInteger value) {
        this.rack.put(address, value);
    }

    @Override
    public BigInteger get(long address) {
        return this.rack.get(address);
    }

}
