package pk.risiko.util;

import java.util.ArrayList;

/**
 * @author Raphael
 * @version 08.01.2016
 */
public class CyclicList<T> extends ArrayList<T> {
    private int currentCount = 0;

    public T next() {
        if( ++currentCount >= size() ) currentCount = 0;
        return get(currentCount);
    }
    public T prev() {
        if( --currentCount < 0 ) currentCount = size()-1;
        return get(currentCount);
    }
    public T peek() {
        return  super.get(currentCount % super.size());
    }
    public boolean isAtBeginning() {
        return currentCount == 0;
    }
}
