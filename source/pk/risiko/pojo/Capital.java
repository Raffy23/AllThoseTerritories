package pk.risiko.pojo;

import java.awt.Point;

/**
 * This class does store name and location of the
 * Captial of a #Territory
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Capital {
    private static final int SIZE = 8;
    private Point coords;

    public Capital(int x,int y) {
        this.coords = new Point(x,y);
    }

    public Point getCoords() {
        return coords;
    }
}
