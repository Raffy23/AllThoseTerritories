package pk.risiko.pojo;

import pk.risiko.ui.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/**
 * This class does store name and location of the
 * Captial of a #Territory
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Capital implements Drawable {
    private static final int SIZE = 8;

    private String name;
    private Point coords;

    public Capital(String name,int x,int y) {
        this.name = name;
        this.coords = new Point(x,y);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(coords.x,coords.y,SIZE,SIZE);
        g.drawString(name,coords.x,coords.y); //Eyecandy: show name on hoofer only
    }

    public Point getCoords() {
        return coords;
    }
}
