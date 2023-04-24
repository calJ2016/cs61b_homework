package bstmap;

import edu.princeton.cs.algs4.Queue;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private BSTNode root;
    private Set<K> keySet;

    private class BSTNode {
        private K key;                  //sorted by key
        private V value;               // associated data
        private BSTNode left, right;  //left and right subtrees
        private int size;            // number of BSTNodes in subtree

        //constructor,
        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.value = val;
            this.size = size;
        }
    }

    //Initializes an empty table
    public BSTMap() {
    }

    /**
     * Removes all of the mappings from this map.
     */
    @Override
    public void clear() {
        if (root == null) {
            return;
        }
        clearValues(root);
    }

    private void clearValues(BSTNode bn) {
        if (bn == null) {
            return;
        } else {
            bn.value = null;
            bn.size = 0;
        }
        clearValues(bn.left);
        clearValues(bn.right);
    }

    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey(K key) is null");
        }
        if (containsValue(key)) {
            return true;
        } else if (!containsValue(key) && size() > 0) {
            return true;
        }
        return false;
    }

    private boolean containsValue(K key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to containsKey(K key) is null");
        }
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(root, key);
    }

    private V get(BSTNode bn, K key) {
        if (key == null) {
            throw new IllegalArgumentException("call get(BSTNode, K) is null");
        }
        if (bn == null) {
            return null;
        }
        int cmp = key.compareTo(bn.key);
        if (cmp < 0) {
            return get(bn.left, key);
        } else if (cmp > 0) {
            return get(bn.right, key);
        } else {
            return bn.value;
        }
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(BSTNode bn) {
        if (bn == null) {
            return 0;
        }
        return bn.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Calls put(K,V) with a null key");
        }
        if (value == null && size() > 0) {
            remove(key);
        }
        root = put(root, key, value);

    }

    private BSTNode put(BSTNode bn, K key, V value) {
        //First time to put
        if (bn == null) {
            //put the key into keySet
            //keySet.add(key);
            return new BSTNode(key, value, 1);
        }
        int cmp = key.compareTo(bn.key);
        if (cmp < 0) {
            bn.left = put(bn.left, key, value);
        } else if (cmp > 0) {
            bn.right = put(bn.right, key, value);
        } else {
            bn.value = value;
        }
        bn.size = 1 + size(bn.left) + size(bn.right);
        return bn;

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    private Set<K> getKeySet() {
        return keySet;
    }

    @Override
    public V remove(K key) {
        //throw new UnsupportedOperationException();
        if (key == null) {
            throw new IllegalArgumentException("Calls remove(K) with a null key");
        }
        V value = remove(root, key);
        return value;
    }

    private V remove(BSTNode bn, K key) {
        if (bn == null) {
            return null;
        }
        V res;
        int cmp = key.compareTo(bn.key);
        if (cmp < 0) {
            res = remove(bn.left, key);
        } else if (cmp > 0) {
            res = remove(bn.right, key);
        } else {
            res = bn.value;
            bn.value = null;
        }
        return res;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        K minKey = minKey();
        K maxKey = maxKey();
        Iterable<K> queueKey = allKeys(minKey, maxKey);
        for (K key : queueKey) {
            V value = get(key);
            System.out.println("Key: " + key + " Value: " + value);
        }
    }

    private K minKey() {
        BSTNode bn = root;
        while (bn.left != null) {
            bn = bn.left;
        }
        return bn.key;
    }

    private K maxKey() {
        BSTNode bn = root;
        while (bn.right != null) {
            bn = bn.right;
        }
        return bn.key;
    }

    private Iterable<K> allKeys(K low, K high) {
        if (low == null) throw new IllegalArgumentException("First argument is null.");
        if (high == null) throw new IllegalArgumentException("Second argument is null.");
        Queue<K> queue = new Queue<>();
        keys(root, queue, low, high);
        return queue;
    }

    private void keys(BSTNode bn, Queue<K> q, K low, K high) {
        if (bn == null) {
            return;
        }
        int cmpLow = low.compareTo(bn.key);
        int cmpHigh = high.compareTo(bn.key);
        if (cmpLow < 0) {
            keys(bn.left, q, low, high);
        }
        if (cmpLow <= 0 && cmpHigh >= 0) {
            q.enqueue(bn.key);
        }
        if (cmpHigh > 0) {
            keys(bn.right, q, low, high);
        }
    }
}
