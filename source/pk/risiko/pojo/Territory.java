package pk.risiko.pojo;

import pk.risiko.ui.elements.UIElement;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Territory in game
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Territory extends UIElement {

    private static final Color OVAL_COLOR = new Color(248, 248, 248);
    private static final Color DEFAULT_COLOR = new Color(181, 132, 12);
    private static final Color HOVER_COLOR = DEFAULT_COLOR.brighter();

    private static final int OVAL_SIZE = 24;

    private String name;
    private Capital capital;
    private List<Territory> neighbours;

    private Player owner;
    private int currentArmyCount = 0;

    //neighbours might not be known at constructing time!
    public Territory(String name,Capital capital,Polygon land) {
        super(land);

        this.name = name;
        this.capital = capital;
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

    public int getStationedArmies() {
        return this.currentArmyCount;
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
        g.fillPolygon((Polygon) this.getElementShape());

        g.setColor(this.getMouseState()==MouseState.CLICKED?Color.RED:Color.BLACK);
        g.drawPolygon((Polygon) this.getElementShape());

        g.setColor(OVAL_COLOR);
        final int xOffset = g.getFontMetrics().stringWidth(String.valueOf(this.currentArmyCount))+1;
        final int yOffset = g.getFontMetrics().getHeight();
        g.fillOval(this.capital.getCoords().x-xOffset,this.capital.getCoords().y-yOffset,OVAL_SIZE,OVAL_SIZE);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.currentArmyCount),this.capital.getCoords().x,this.capital.getCoords().y);
    }


}
