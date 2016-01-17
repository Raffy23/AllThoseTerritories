package pk.risiko.pojo;

import pk.risiko.ui.elements.UIElement;

import java.awt.Color;
import java.awt.Graphics2D;
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
    private static final Color DEFAULT_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.6f);
    private static final Color HOVER_COLOR = DEFAULT_COLOR.brighter();

    private static final int OVAL_SIZE = 24;

    private String name;
    private Capital capital=new Capital("",0,0);
    private List<Territory> neighbours;

    private Player owner;
    private int currentArmyCount = 0;

    //neighbours might not be known at constructing time!
    public Territory(String name, Area a) {
        super(a);

        this.name = name;
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

    public Capital getCapital(){return capital;}

    public List<Territory> getNeighbours() {
        return neighbours;
    }

    public int getStationedArmies() {
        return this.currentArmyCount;
    }

    public void increaseArmy(int delta) {
        this.currentArmyCount += delta;
    }

    /**
     *
     * @param delta delta value of which the units should be decreased (must be positive)
     * @return true if the land was take by another player
     */
    public boolean decreaseArmy(int delta) {
        assert delta > 0 : "Delta value must be positive!";

        this.currentArmyCount -= delta;
        if( this.currentArmyCount <= 0 ) {
            this.currentArmyCount = 0;
            return true;
        }

        return false;
    }

    private Color getDrawingColor() {
        if( this.getMouseState() == MouseState.NORMAL)
            return this.owner!=null?this.owner.getColor():DEFAULT_COLOR;
        else
            return this.owner!=null?this.owner.getColor().brighter():HOVER_COLOR;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(this.getDrawingColor());
        g.fill(this.getElementShape());

        g.setColor(this.getMouseState()==MouseState.CLICKED?Color.RED:Color.BLACK);
        g.draw(this.getElementShape());

        g.setColor(OVAL_COLOR);
        final int xOffset = g.getFontMetrics().stringWidth(String.valueOf(this.currentArmyCount));
        final int yOffset = g.getFontMetrics().getHeight();
        g.fillOval(this.capital.getCoords().x,this.capital.getCoords().y,OVAL_SIZE,OVAL_SIZE);

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.currentArmyCount)
                    ,this.capital.getCoords().x+OVAL_SIZE/2-xOffset/2
                    ,this.capital.getCoords().y+yOffset);
    }

    @Override
    public void mouseClicked() {
        /* do nothing */
    }

    public Area getArea() {
        return (Area)this.getElementShape();
    }

    public void setCapital(Capital capital) {
        this.capital = capital;
    }
}
