package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private static int FIXED_SIZE = 8;

    /* constructor of ArrayDeque */
    public ArrayDeque() {
        items = (T[]) new Object[FIXED_SIZE];
        size = 0;
        nextFirst = 2;
        nextLast = 3;

    }

    /* implements the Iterator of ArrayDeque */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int pointer;

        public ArrayDequeIterator() {
            pointer = addOne(nextFirst);
        }

        @Override
        public boolean hasNext() {
            return pointer != nextLast;
        }

        @Override
        public T next() {
            T item = items[pointer];
            pointer = addOne(pointer);
            return item;
        }
    }

    private int  addOne(int index){
        return (index + 1) % items.length;
    }


    public boolean equals(Object o) {
        if (this.equals(o)) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> temp = (Deque<T>) o;
        if (this.size() != temp.size()) {
            return false;
        }
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * resizes the underlying array to the target capacity.
     * 0- all items of original array copy to new larger capacity array.
     * 1- all items of original array copy to new smaller capacity array.
     * To improve usage factor, if usage factor < 0.25, resizeType = 1
     */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];

        int newNextFirst = (int) Math.round(capacity * 0.25);
        System.arraycopy(items, nextFirst + 1, temp, newNextFirst + 1, size);

        items = temp;
        nextFirst = newNextFirst;
        nextLast = nextFirst + size + 1;
    }

    private void usageCheck() {
        if (nextFirst == -1) {
            //reach front, resize arrayDeque length to 2*x
            resize(items.length * 2);
        } else if (nextLast == items.length) {
            //reach end, resize arrayDeque length
            resize(items.length * 2);
        } else if (items.length > 16 && size() < (items.length) / 4) {
            resize(items.length / 2);

        }
    }

    /**
     * add first item, put new item in the first index
     * size = 0, just add item to the index 0  position.
     * size > 0, just add item to the index nextFirst.
     * size = length, need to resize a larger array.
     * nextFirst = 0 -- > add item to the back of Array.
     *
     * @param item not null
     */
    @Override
    public void addFirst(T item) {
        items[nextFirst] = item;
        size += 1;
        nextFirst -= 1;
        usageCheck();
    }

    /**
     * add last item, put new item in the last index
     * size = 0, just add item to the index 0 position.
     * size > 0, just add item to the index nextLast.
     * size = length, need to resize a larger array.
     * nextLast = length - 1, need to let nextLast point to index 0.
     *
     * @param item not null
     */
    @Override
    public void addLast(T item) {
        items[nextLast] = item;
        size += 1;
        nextLast += 1;
        usageCheck();
    }

    /* Removes the first item of the ArrayDeque */
    @Override
    public T removeFirst() {
        if (!isEmpty()) {
            T removeItem = items[nextFirst + 1];
            items[nextFirst + 1] = null;
            size -= 1;
            nextFirst += 1;
            usageCheck();
            return removeItem;
        }
        return null;
    }

    /* Removes the last item of the ArrayDeque */
    @Override
    public T removeLast() {
        if (!isEmpty()) {
            T removeItem = items[nextLast - 1];
            items[nextLast - 1] = null;
            size -= 1;
            nextLast -= 1;
            usageCheck();
            return removeItem;
        }
        return null;
    }

    /* Uses the index to get the item in items */
    @Override
    public T get(int index) {
        if (index >= 0 && index < items.length) {
            int realIndex = (nextFirst + index + 1) % items.length;
            return items[realIndex];
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public void printDeque() {
        if (size == 0) {
            return;
        }
        for (T item : items) {
            System.out.print(item + " ");
        }

        System.out.println();
    }

}
