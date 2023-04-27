package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int tableSize; //hash table size
    private int pairSize; //key value size
    private final static int initialSize = 16;
    private double loadFactor = 0.75;
    private Set<K> keySet;

    /**
     * Constructors
     */
    public MyHashMap() {
        this(initialSize);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad     maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        if (initialSize < 1 || maxLoad <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = maxLoad;
        this.tableSize = initialSize;
        this.pairSize = 0;
        this.buckets = createTable(initialSize);
        this.keySet = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        LinkedList<Node> lln = new LinkedList<>();
        return lln;
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        if (tableSize < 1 || this.loadFactor <= 0.0) {
            throw new IllegalArgumentException();
        }
        this.buckets = new Collection[tableSize];
        for (int i = 0; i < tableSize; i += 1) {
            buckets[i] = createBucket();
        }
        return buckets;
    }


    @Override
    public void clear() {
        if (this.pairSize == 0) {
            return;
        }
        for (int i = 0; i < this.tableSize; i += 1) {
            this.buckets[i] = null;
        }
        this.pairSize = 0;
        this.keySet.clear();
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey() is null");
        }
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to get() is null");
        }
        int i = hash(key);
        Collection<Node> lln = this.buckets[i];
        if (lln != null) {
            for (Node n : lln) {
                if (n.key.equals(key)) {
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.pairSize;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("First argument to put() is null");
        }
        if (value == null) {
            return;
        }
        if (pairSize >= 0.75 * tableSize) {
            resize(tableSize * 2);
        }
        int tempHash = hash(key);
        if (!containsKey(key)) {
            this.buckets[tempHash].add(new Node(key, value));
            this.keySet.add(key);
            this.pairSize += 1;
        }
        if (this.buckets[tempHash] != null) {
            for (Node n : this.buckets[tempHash]) {
                if (n.key == key) {
                    n.value = value;
                    return;
                }
            }
        } else {
            this.buckets[tempHash].add(new Node(key, value));
            this.keySet.add(key);
            this.pairSize += 1;
        }

    }

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        Set<K> sk = keySet();
        return sk.iterator();
    }

    private int hash(K key) {
        int h = key.hashCode();
        h ^= (h >>> 20) ^ (h >>> 12) ^ (h >>> 7) ^ (h >>> 4);
        return h & (this.tableSize - 1);
    }

    private void resize(int tableSize) {
        MyHashMap<K, V> mhm = new MyHashMap<>(tableSize);
        for (int i = 0; i < this.tableSize; i += 1) {
            Collection<Node> cn = this.buckets[i];
            for (Node n : cn) {
                mhm.put(n.key, n.value);
            }
        }
        this.tableSize = mhm.tableSize;
        this.pairSize = mhm.pairSize;
        this.buckets = mhm.buckets;
    }
}
