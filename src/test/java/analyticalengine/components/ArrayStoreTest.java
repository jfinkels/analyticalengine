/**
 * ArrayStoreTest.java - tests for the ArrayStore class
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

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

/**
 * Tests for the ArrayStore class.
 * 
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class ArrayStoreTest {

    /**
     * Test for getting a memory address that has been set.
     */
    @Test
    public void testGetSetAddress() {
        Store store = new ArrayStore();
        store.put(0, BigInteger.ONE);
        assertEquals(BigInteger.ONE, store.get(0));
    }
    
    @Test
    public void testReset() {
        Store store = new ArrayStore();
        store.put(0, BigInteger.ONE);
        store.reset();
        assertEquals(BigInteger.ZERO, store.get(0));
    }
    
    /**
     * Tests that getting a memory address that has not yet been set returns
     * zero.
     */
    @Test
    public void testGetUnsetAddress() {
        Store store = new ArrayStore();
        assertEquals(BigInteger.ZERO, store.get(0));
    }

}
