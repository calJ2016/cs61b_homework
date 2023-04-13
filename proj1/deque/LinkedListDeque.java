package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private class DoubleNode {
        private DoubleNode prev;
        private T item;
        private DoubleNode next;

        DoubleNode(T value) {
            item = value;
        }
    }

    private DoubleNode senitel;
    private int size;

    // LinkedListDeque constructor
    public LinkedListDeque() {
        senitel = new DoubleNode(null);
        senitel.prev = senitel;
        senitel.next = senitel.prev;
        size = 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> temp = (Deque<T>) o;
        if (size() != temp.size()) {
            return false;
        }

        for (int i = 0; i < size(); i += 1) {
            Object obj = temp.get(i);
            if (!(this.get(i).equals(obj))) {
                return false;
            }

        }
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private DoubleNode current;

        public LinkedListDequeIterator() {
            current = senitel.next;
        }

        public boolean hasNext() {
            return current != senitel;
        }

        public T next() {
            T item = current.item;
            current = current.next;
            return item;
        }
    }

    /**
     * add first, put a new node between
     * senitel and original first node
     *
     * @param item not null
     */
    @Override
    public void addFirst(T item) {
        //let temp.next point to the original first node
        DoubleNode temp = new DoubleNode(item);
        //add first item and size = 0, need let the senitel.prev -> item
        if (size == 0) {
            senitel.prev = temp;
            temp.next = senitel;
        } else {
            // get the original node that the senitel.next point to
            //let the item.next point to original node
            //temp = senitel.next.prev;
            temp.next = senitel.next;
            senitel.next.prev = temp;
        }
        // no matter size equals 0 or not, should take below actions
        temp.prev = senitel;
        senitel.next = temp;
        size += 1;
    }

    /**
     * addLast, add a new node in the end of the list
     *
     * @param item not null
     */
    @Override
    public void addLast(T item) {
        if (size == 0) {
            addFirst(item);
        } else {
            //let the original last node.next point to new node
            DoubleNode temp = new DoubleNode(item);
            temp.prev = senitel.prev;
            senitel.prev.next = temp;
            temp.next = senitel;
            senitel.prev = temp;
            size += 1;
        }
    }

    /* removes the first node of the list */
    @Override
    public T removeFirst() {
        T item = null;
        if (size == 0) {
            return item;
        } else if (size == 1) {
            //1. delete the first node.prev point to senitel
            senitel.next.prev = null;
            item = senitel.next.item;
            //2. let the senitel.next point to self
            senitel.next = senitel;
            senitel.prev = senitel;
            size -= 1;
        } else {
            //1. delete the first node.prev point to senitel
            DoubleNode target = senitel.next;
            item = target.item;
            target.next.prev = senitel;
            senitel.next = target.next;
            target.prev = null;
            target.next = null;
            size -= 1;
        }
        return item;
    }

    /* removes the last node of the list. */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        } else if (size == 1) {
            return removeFirst();
        } else {
            DoubleNode last = senitel.prev;
            last.prev.next = senitel;
            senitel.prev = last.prev;
            last.prev = null;
            last.next = null;
            size -= 1;
            return last.item;
        }
    }

    /* get index node's item */
    @Override
    public T get(int index) {
        if (size() > 0 && index >= 0 && index < size) {
            DoubleNode temp = senitel.next;
            for (int i = 0; i < size(); i++) {
                if (index == i) {
                    return temp.item;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    public T getRecursive(int index) {
        if (index == 0) {
            return senitel.next.item;
        }
        if (index > size) {
            return null;
        }
        DoubleNode n = senitel.next;
        return getRecursiveHelper(index, n);
    }

    private T getRecursiveHelper(int count, DoubleNode node) {
        if (count == 0) {
            return node.item;
        }
        return getRecursiveHelper(count - 1, node.next);
    }


    /* return the size of the deque */
    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        if (senitel.next == senitel.prev) {
            return;
        }
        DoubleNode node = senitel.next;
        for (int i = 0; i < size; i += 1) {
            System.out.print(node.item + " ");
            node = node.next;
        }
        System.out.println();
    }

}
