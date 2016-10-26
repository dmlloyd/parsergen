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

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
final class IndexHashMap<K, V> extends AbstractCollection<V> implements IndexMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 512;
    private static final int MAXIMUM_CAPACITY = 1 << 30;
    private static final float DEFAULT_LOAD_FACTOR = 0.60f;

    private final Indexer<? extends K, ? super V> indexer;
    private final Hasher<? super K> kh;
    private final Equaller<? super V> ve;

    private final float loadFactor;
    private final int initialCapacity;
    private V[][] array;
    private int threshold;
    private int size;

    @SuppressWarnings("unchecked")
    public IndexHashMap(final Indexer<? extends K, ? super V> indexer, final Hasher<? super K> kh, final Equaller<? super V> ve, final float loadFactor, int initialCapacity) {
        this.indexer = indexer;
        this.kh = kh;
        this.ve = ve;
        if (indexer == null) {
            throw new IllegalArgumentException("indexer is null");
        }
        if (kh == null) {
            throw new IllegalArgumentException("keyHasher is null");
        }
        if (ve == null) {
            throw new IllegalArgumentException("valueEqualler is null");
        }
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be > 0");
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }
        if (loadFactor <= 0.0 || Float.isNaN(loadFactor) || loadFactor >= 1.0) {
            throw new IllegalArgumentException("Load factor must be between 0.0f and 1.0f");
        }

        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }

        this.loadFactor = loadFactor;
        this.initialCapacity = capacity;

        array = (V[][]) new Object[capacity][];
        threshold = capacity == MAXIMUM_CAPACITY ? Integer.MAX_VALUE : (int)(capacity * loadFactor);
    }

    public IndexHashMap(final Indexer<? extends K, ? super V> indexer, final Hasher<? super K> kh, final Equaller<? super V> ve) {
        this(indexer, kh, ve, DEFAULT_LOAD_FACTOR, DEFAULT_INITIAL_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        final int capacity = threshold <<= 1;
        final int mask = capacity - 1;
        final V[][] newArray = (V[][]) new Object[capacity][];
        for (V[] row : array) {
            for (V item : row) {
                final K key = indexer.getKey(item);
                final int hc = kh.hashCode(key) & mask;
                final V[] oldRow = newArray[hc];
                if (oldRow == null) {
                    newArray[hc] = (V[]) new Object[] { item };
                } else {
                    final int oldLen = oldRow.length;
                    (newArray[hc] = Arrays.copyOf(oldRow, oldLen + 1))[oldLen] = item;
                }
            }
        }
        array = newArray;
    }

    private static <E> E[] addElement(E[] oldRow, E element) {
        if (oldRow == null) {
            return (E[]) new Object[] { element };
        } else {
            final int length = oldRow.length;
            final E[] newRow = Arrays.copyOf(oldRow, length + 1);
            newRow[length] = element;
            return newRow;
        }
    }

    private static <E> E[] removeElement(E[] row, int idx) {
        final int len = row.length;
        assert idx < len;
        if (len == 1) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E[] newRow = (E[]) new Object[len - 1];
        if (idx > 0) {
            System.arraycopy(row, 0, newRow, 0, idx);
        }
        if (idx < len - 1) {
            System.arraycopy(row, idx + 1, newRow, idx, len - 1 - idx);
        }
        return newRow;
    }

    public Iterator<V> iterator() {
        return new TableIterator<V>(array);
    }

    public int size() {
        return size;
    }

    public boolean containsKey(final K key) {
        final int hc = kh.hashCode(key);
        final V[][] array = this.array;
        final V[] row = array[hc & (array.length - 1)];
        if (row == null) {
            return false;
        }
        for (V v : row) {
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(final Object o) {
        if (! ve.accepts(o) || ! indexer.accepts(o)) {
            return false;
        }
        final V value = (V) o;
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key);
        final V[][] array = this.array;
        final V[] row = array[hc & (array.length - 1)];
        if (row == null) {
            return false;
        }
        for (V v : row) {
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                return ve.equals(v, value);
            }
        }
        return false;
    }

    public V get(final K key) {
        final int hc = kh.hashCode(key);
        final V[][] array = this.array;
        final V[] row = array[hc & (array.length - 1)];
        if (row == null) {
            return null;
        }
        for (V v : row) {
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                return v;
            }
        }
        return null;
    }

    public V removeKey(final K key) {
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row == null) {
            return null;
        }
        for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                array[hc] = removeElement(row, i);
                return v;
            }
        }
        return null;
    }

    public boolean remove(final Object o) {
        if (! ve.accepts(o) || ! indexer.accepts(o)) {
            return false;
        }
        final V value = (V) o;
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row == null) {
            return false;
        }
        for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                if (ve.equals(v, value)) {
                    array[hc] = removeElement(row, i);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean add(final V value) {
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                if (ve.equals(value, v)) {
                    return false;
                } else {
                    row[i] = value;
                    return true;
                }
            }
        }
        array[hc] = addElement(row, value);
        if (++size > threshold) {
            resize();
        }
        return true;
    }

    public V put(final V value) {
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row != null) for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                row[i] = value;
                return v;
            }
        }
        array[hc] = addElement(row, value);
        if (++size > threshold) {
            resize();
        }
        return null;
    }

    public V putIfAbsent(final V value) {
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row != null) for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                return v;
            }
        }
        array[hc] = addElement(row, value);
        if (++size > threshold) {
            resize();
        }
        return null;
    }

    public V replace(final V value) {
        final K key = indexer.getKey(value);
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row == null) {
            return null;
        }
        for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                row[i] = value;
                return v;
            }
        }
        return null;
    }

    public boolean replace(final V oldValue, final V newValue) {
        final K key = indexer.getKey(oldValue);
        if (! kh.equals(key, indexer.getKey(newValue))) {
            throw new IllegalArgumentException("Keys do not match");
        }
        final int hc = kh.hashCode(key) & array.length - 1;
        final V[][] array = this.array;
        final V[] row = array[hc];
        if (row == null) {
            return false;
        }
        for (int i = 0, length = row.length; i < length; i++) {
            final V v = row[i];
            final K rowKey = indexer.getKey(v);
            if (kh.equals(rowKey, key)) {
                if (ve.equals(v, oldValue)) {
                    row[i] = newValue;
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean accepts(final Object obj) {
        return indexer.accepts(obj) && kh.accepts(indexer.getKey((V) obj));
    }

    public K getKey(final V value) {
        return indexer.getKey(value);
    }

    public void clear() {
        int capacity = 1;

        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        array = (V[][]) new Object[capacity][];
        threshold = capacity == MAXIMUM_CAPACITY ? Integer.MAX_VALUE : (int)(capacity * loadFactor);
        size = 0;
    }

    static final class TableIterator<E> implements Iterator<E> {
        private final E[][] table;
        private int tableIdx;
        private int itemIdx;
        private E next;

        TableIterator(final E[][] table) {
            this.table = table;
        }

        public boolean hasNext() {
            while (next == null) {
                final E[][] array = table;
                if (array.length == tableIdx) {
                    return false;
                }
                final E[] items = array[tableIdx];
                if (items != null) {
                    final int len = items.length;
                    if (itemIdx < len) {
                        return true;
                    }
                }
                itemIdx = 0;
                tableIdx++;
            }
            return true;
        }

        public E next() {
            if (hasNext()) try {
                return next;
            } finally {
                next = null;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
