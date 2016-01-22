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
    /**
     * coords does save the exact coordinates of the capital
     */
    private Point coords;

    /**
     * To construct the Object the exact position of the capatial must be
     * known, since the capital has no name (jet?) we simply don't care
     * @param x x position on the screen
     * @param y y position on the screen
     */
    public Capital(int x,int y) {
        this.coords = new Point(x,y);
    }

    /**
     * @return the coordinates where the capital is n form of a Point
     */
    public Point getCoords() {
        return coords;
    }
}
