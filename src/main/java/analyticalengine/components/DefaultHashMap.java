/**
 * DefaultHashMap.java - HashMap that provides default values for unset keys
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

import java.util.HashMap;
import java.util.function.Function;

/**
 * A HashMap that provides default values for keys that have not yet been set.
 * 
 * @param <K>
 *            The type of keys in the map.
 * @param <V>
 *            The type of values in the map.
 * @author Jeffrey Finkelstein &lt;jeffrey.finkelstein@gmail.com&gt;
 * @since 0.0.1
 */
public class DefaultHashMap<K, V> extends HashMap<K, V> {
    /** Default generated serial version UID. */
    private static final long serialVersionUID = 8778315633381430886L;

    /**
     * The object that generates values for keys that have not yet been set.
     */
    private transient Function<K, V> generator;

    /**
     * Gets the value associated with the specified key, or a default value if
     * the specified key is not in this map.
     * 
     * The default value is generated by the value generator specified
     * previously by {@link #setValueGenerator(Function)}. If the value
     * generator has not been set, returns whatever the superclass would have
     * returned.
     * 
     * @param key
     *            The key whose associated value will be returned.
     * @return The value associated with the specified key, or a default value
     *         if the specified key is not in this map.
     * @see java.util.HashMap#get(Object)
     */
    // HACK needed because superclass has param of type Object instead of V
    @SuppressWarnings("unchecked")
    @Override
    public V get(final Object key) {
        if (this.containsKey(key) || this.generator == null) {
            return super.get(key);
        }
        this.put((K) key, this.generator.apply((K) key));
        return this.get(key);
    }

    /**
     * Specifies the generator that produces values for keys that have not yet
     * been set.
     * 
     * @param generator
     *            The generator for default values in this map.
     */
    public void setValueGenerator(final Function<K, V> generator) {
        this.generator = generator;
    }
}
