/**
 * ValueGenerator.java - generates values based on a specified key
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

import java.io.Serializable;

/**
 * Generates values based on a specified key.
 * 
 * @param <K>
 *            The type of keys for which to generate associated values.
 * @param <V>
 *            The type of values to generate for associated keys.
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public interface ValueGenerator<K, V> extends Serializable {

    /**
     * Generates a value based on the specified key.
     * 
     * @param key
     *            The key for which to generate a value.
     * @return A value.
     */
    V generate(K key);
}
