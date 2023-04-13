package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c){
        super();
        comparator = c;

    }

    public T max(){
        return max(comparator);
    }

    public T max(Comparator<T> c){
        int dequeSize = size();
        if (dequeSize > 0 ){
            T maxItem = get(0);
            for (int i = 0; i< dequeSize; i += 1){
                T temp = get(i);
                if (comparator.compare(maxItem, temp) == 0){
                    maxItem = temp;
                }
            }
            return maxItem;
        }
        return null;
    }
}
