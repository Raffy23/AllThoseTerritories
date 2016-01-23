package pk.risiko.util;

import java.util.ArrayList;

/**
 * This class represents a cyclic list
 *
 * @author Raphael Ludwig
 * @version 08.01.2016
 */
public class CyclicList<T> extends ArrayList<T> {
    /** current index which defines on which element we are currently looking **/
    private int currentCount = 0;

    /**
     * @return the next element in the list, if the current element is the last it returns the first
     */
    public T next() {
        if( ++currentCount >= size() ) currentCount = 0;
        return get(currentCount);
    }

    /**
     * @return the previous element in the list, if the current element is the first it returns the last
     */
    public T prev() {
        if( --currentCount < 0 ) currentCount = size()-1;
        return get(currentCount);
    }

    /**
     * @return the current element of the list, does not change anything
     */
    public T peek() {
        return  super.get(currentCount % super.size());
    }

    /**
     * @return true if the current element is at the beginning otherwise false
     */
    public boolean isAtBeginning() {
        return currentCount == 0;
    }

    /**
     * resets the current element to the first
     */
    public void reset() {
        currentCount = 0;
    }

    /**
     * @return true if the current element is at the end otherwise false
     */
    public boolean isAtEnd() {
        return currentCount == size()-1;
    }

}
