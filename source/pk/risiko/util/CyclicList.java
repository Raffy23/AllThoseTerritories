package pk.risiko.util;

import java.util.ArrayList;

/**
 * @author Raphael
 * @version 08.01.2016
 */
public class CyclicList<T> extends ArrayList<T> {
    private int currentCount = 0;

    public T next() {
        return super.get(Math.abs(currentCount++ % super.size()));
    }
    public T peek() {
        return  super.get(Math.abs(currentCount % super.size()));
    }
}
