package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comparator = c;

    }

    public T max() {
        return max(comparator);
    }

    public T max(Comparator<T> c) {
        int dequeSize = this.size();
        if (dequeSize > 0) {
            T maxItem = this.get(0);
            for (int i = 1; i < dequeSize; i += 1) {
                T temp = get(i);
                if (c.compare(maxItem, temp) < 0) {
                    maxItem = temp;
                }
            }
            return maxItem;
        }
        return null;
    }

}
