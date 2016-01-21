package pk.risiko.pojo;

import pk.risiko.ui.elements.UIElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private final static Stroke ACTIVE_STROKE = new BasicStroke(4.0f
                                                                ,BasicStroke.CAP_SQUARE
                                                                ,BasicStroke.JOIN_MITER
                                                                ,2.0f
                                                                ,new float[] {2.0f}
                                                                ,0.0f);
    private static final int OVAL_SIZE = 24;

    private String name;
    private Capital capital=new Capital(0,0);
    private List<Territory> neighbours;

    private Player owner;
    private int currentArmyCount = 0;

    public enum ActiveState {
        OWN,HOSTILE,NONE
    }
    private ActiveState active = ActiveState.NONE;

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

        g.setColor(Color.BLACK);
        final Stroke oldStroke = g.getStroke();
        if( this.active == ActiveState.OWN ) g.setStroke(ACTIVE_STROKE);
        g.draw(this.getElementShape());

        g.setStroke(oldStroke);
    }

    public void paintTopComponents(Graphics2D g) {
        g.setColor(OVAL_COLOR);
        final int xOffset = g.getFontMetrics().stringWidth(String.valueOf(this.currentArmyCount));
        final int yOffset = g.getFontMetrics().getHeight();
        g.fillOval(this.capital.getCoords().x,this.capital.getCoords().y,OVAL_SIZE,OVAL_SIZE);

        if( this.active == ActiveState.HOSTILE ) {
            g.setColor(Color.RED);
            final Stroke oldStroke = g.getStroke();

            g.setStroke(ACTIVE_STROKE);
            g.drawOval(this.capital.getCoords().x,this.capital.getCoords().y,OVAL_SIZE,OVAL_SIZE);

            g.setStroke(oldStroke);
        }

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

    /**
     * Defending against a hostile force is done in this method, the territory must
     * be taken by any Player otherwise this method my produce faults!
     *
     * @param attacker the attacker territory
     * @return true if successfully defended against the troups otherwise false
     */
    public boolean defendAgainst(Territory attacker) {
        assert owner != attacker.owner : "Why should i attack my own territories? ("+owner.getName()+")";

        Random dice = new Random();
        int maxTroups = currentArmyCount > attacker.currentArmyCount ? currentArmyCount : attacker.currentArmyCount;
        if( maxTroups > 3 ) maxTroups = 3;

        for(int i=0;i<maxTroups;i++) {
            int attDice = dice.nextInt(6);
            int defDice  = dice.nextInt(6);

            if( attDice > defDice )
                if( --currentArmyCount == 0 ) return false;
            else if( --attacker.currentArmyCount == 1 ) return false;
        }

        return true;
    }

    public ActiveState getActiveState() {
        return active;
    }

    public void setActive(ActiveState active) {
        this.active = active;
    }
}
