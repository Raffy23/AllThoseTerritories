package pk.risiko.pojo;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Territory in game
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Territory implements Drawable {

    private String name;
    private Capital capital;
    private List<Territory> neighbours;
    private Polygon land;

    /* TODO: Implement some nice things we might need the later:
    * private Player holdBy
    * private int currentArmyCount
    * */

    //neighbours might not be known at constructing time!
    public Territory(String name,Capital capital,Polygon land) {
        this.name = name;
        this.capital = capital;
        this.land = land;
        this.neighbours = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCapitalName() {
        return capital.getName();
    }

    public List<Territory> getNeighbours() {
        return neighbours;
    }

    @Override
    public void paint(Graphics g) {
        g.drawPolygon(this.land);
        this.capital.paint(g);
    }
}
