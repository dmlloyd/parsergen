/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.flicc.processor;

import java.util.Collection;

/**
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
interface IndexMap<K, V> extends Collection<V>, Indexer<K, V> {

    /**
     * Determine whether the given key is contained in the map.
     *
     * @param key the key
     * @return {@code true} if it is contained in the map
     */
    boolean containsKey(K key);

    /**
     * Get the value for the given key.
     *
     * @param key the key
     * @return the corresponding value
     */
    V get(K key);

    /**
     * Remove and return a value for the given key.
     *
     * @param key the key
     * @return the value
     */
    V removeKey(K key);

    /**
     * Put a value into the map, replacing and returning any existing mapping.
     *
     * @param value the value to add
     * @return the old value, or {@code null} if the old value was {@code null} or was not present
     */
    V put(V value);

    /**
     * Put a value into the map if there is not already an existing mapping for it.
     *
     * @param value the value to add
     * @return the existing value, if any, or {@code null} if the existing value was {@code null} or the value was added successfully
     */
    V putIfAbsent(V value);

    /**
     * Put a value into the map only if there is an existing mapping for it.
     *
     * @param value the value to store
     * @return the previous value (may be {@code null}) or {@code null} if there was no mapping to replace
     */
    V replace(V value);

    /**
     * Replace an old value with a new value.
     *
     * @param oldValue the value to replace
     * @param newValue the value to replace with
     * @return {@code true} if the replacement succeeded, or {@code false} if the old value was not present in the map
     */
    boolean replace(V oldValue, V newValue);
}
