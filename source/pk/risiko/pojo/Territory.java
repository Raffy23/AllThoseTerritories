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

    private static final Color DEFAULT_COLOR = new Color(181, 132, 12);

    private String name;
    private Capital capital;
    private List<Territory> neighbours;
    private Polygon land;

    private Player owner;
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

    public void setOwner(Player player) {
        this.owner = player;
    }

    public Player getOwner() {
        return this.owner;
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
        g.setColor(this.owner!=null?this.owner.getColor():DEFAULT_COLOR);
        g.fillPolygon(this.land);
        g.setColor(Color.BLACK);
        g.drawPolygon(this.land);
        this.capital.paint(g);
    }
}
