package pk.risiko.pojo;

import pk.risiko.ui.elements.UIElement;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Territory in game
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Territory extends UIElement {

    private static final Color DEFAULT_COLOR = new Color(181, 132, 12);
    private static final Color HOVER_COLOR = DEFAULT_COLOR.brighter();

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

    private Color getDrawingColor() {
        if( this.getMouseState() == MouseState.NORMAL)
            return this.owner!=null?this.owner.getColor():DEFAULT_COLOR;
        else
            return this.owner!=null?this.owner.getColor().brighter():HOVER_COLOR;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(this.getDrawingColor());
        g.fillPolygon(this.land);

        g.setColor(this.getMouseState()==MouseState.CLICKED?Color.RED:Color.BLACK);
        g.drawPolygon(this.land);

        this.capital.paint(g);
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        return this.land.contains(x,y);
    }

    @Override
    public boolean mouseClicked() {
        //TODO: add units / attack / or something else

        return false;
    }

    @Override
    public boolean keyEntered(char key) { /** Not needed here **/ return false; }
}
