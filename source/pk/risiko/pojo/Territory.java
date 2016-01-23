package pk.risiko.pojo;

import pk.risiko.ui.elements.UIElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class represents a Territory in Game.
 * Besides holding all the Data it does extend the UIElement and is therefore
 * also responsible to draw the Territory to the Screen.
 *
 * @see UIElement
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Territory extends UIElement {

    /** The default color of the Oval which is drawn on the capital point **/
    private static final Color OVAL_COLOR = new Color(248, 248, 248);
    /** The default color which des Territory has if there is no Owner set **/
    private static final Color DEFAULT_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.6f);
    /** The default color which des Territory has if there is no Owner set and
     *  Territory is hovered with the mouse **/
    private static final Color HOVER_COLOR = DEFAULT_COLOR.brighter();
    /** The Stroke in which the Territory outline is drawn if it is set to active (OWN) **/
    private final static Stroke ACTIVE_STROKE = new BasicStroke(4.0f
                                                                ,BasicStroke.CAP_SQUARE
                                                                ,BasicStroke.JOIN_MITER
                                                                ,2.0f
                                                                ,new float[] {2.0f}
                                                                ,0.0f);
    /** The size of the oval which is drawn at the point of the capital **/
    private static final int OVAL_SIZE = 24;

    /** The name of the Territory **/
    private final String name;
    /** A List of all Neighbours of this Territory **/
    private final List<Territory> neighbours = new ArrayList<>();
    /** The Capital of the Territory, which is lazy initialized du the MapFormat **/
    private Capital capital=new Capital(0,0);

    /** The current owner of this Territory **/
    private Player owner;
    /** A counter which does represent how many armies are stationed **/
    private int currentArmyCount = 0;

    /**
     * This Enumeration does represent the current Active-State of the
     * Territory.
     */
    public enum ActiveState {
        /** The Territory was selected by a friendly entity **/ OWN,
        /** The Territory was selected by a hostile entity **/ HOSTILE,
        /** Territory was not selected by anything **/ NONE
    }

    /** the active state does change the way the territory is drawn **/
    private ActiveState active = ActiveState.NONE;

    /**
     * A Territory is more or less completely lazy initialized due the clustered MapFile Format
     * @param name The Name of the Territory
     * @param a a Area Object which does represent the Territory (must not be the complete Territory)
     */
    public Territory(String name, Area a) {
        super(a);
        this.name = name;
    }

    /**
     * @param player Sets the new Owner to the Territory
     */
    public void setOwner(Player player) {
        this.owner = player;
    }

    /**
     * @return the current owner of the territory
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * @return the name of the Territory
     */
    public String getName() {
        return name;
    }

    /**
     * @return The Capital of the Territory
     * @see Capital
     */
    public Capital getCapital(){return capital;}

    /**
     * @return a list of all currently known Neighbours of this Territory
     */
    public List<Territory> getNeighbours() {
        return neighbours;
    }

    /**
     * @return the number of how many armies are currently in the territory
     */
    public int getStationedArmies() {
        return this.currentArmyCount;
    }

    /**
     * Increases the army count in this Territory by the given delta
     * @param delta any positive number
     */
    public void increaseArmy(int delta) {
        assert delta > 0 : "Delta must be positive";
        this.currentArmyCount += delta;
    }

    /**
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

    /**
     * @return the current color of the solid parts of the Territory
     */
    private Color getDrawingColor() {
        if( this.getMouseState() == MouseState.NORMAL)
            return this.owner!=null?this.owner.getColor():DEFAULT_COLOR;
        else
            return this.owner!=null?this.owner.getColor().brighter():HOVER_COLOR;
    }

    /**
     * The paint method does only draw the bottom part of the territory, not the
     * Oval Element which can be overdrawn by another Territory. This is draw in the
     * #{paintTopComponents} Method
     *
     * @see UIElement
     */
    @Override
    public void paint(Graphics2D g) {
        final Stroke oldStroke = g.getStroke();

        //draw solid stuff
        g.setColor(this.getDrawingColor());
        g.fill(this.getElementShape());

        //draw the outlines:
        g.setColor(Color.BLACK);
        if( this.active == ActiveState.OWN ) g.setStroke(ACTIVE_STROKE);
        g.draw(this.getElementShape());

        g.setStroke(oldStroke);
    }

    /**
     * This Method draws the TOP Parts of the Terrory which should be drawn in another
     * layer to protect them from overdrawing
     *
     * @param g Graphics2D Element of the source
     */
    public void paintTopComponents(Graphics2D g) {
        g.setColor(OVAL_COLOR);
        final int xOffset = g.getFontMetrics().stringWidth(String.valueOf(this.currentArmyCount));
        final int yOffset = g.getFontMetrics().getHeight();
        g.fillOval(this.capital.getCoords().x,this.capital.getCoords().y,OVAL_SIZE,OVAL_SIZE);

        if( this.active == ActiveState.HOSTILE ) {
            g.setColor(Color.RED);
            final Stroke oldStroke = g.getStroke(); //save stroke for restoring

            g.setStroke(ACTIVE_STROKE);
            g.drawOval(this.capital.getCoords().x,this.capital.getCoords().y,OVAL_SIZE,OVAL_SIZE);

            //restore stroke
            g.setStroke(oldStroke);
        }

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(this.currentArmyCount)
                ,this.capital.getCoords().x+OVAL_SIZE/2-xOffset/2
                ,this.capital.getCoords().y+yOffset);
    }

    /**
     * This Method does nothing, all clicks should be handled by any Clicklisterner outside of this Class
     * @see UIElement
     */
    @Override
    public void mouseClicked() {
        /* do nothing */
    }

    /**
     * @return the area object which does represent the current Area of this Territory. It can be changed.
     */
    public Area getArea() {
        return (Area)this.getElementShape();
    }

    /**
     * @param capital a Captial object to set the capital of this Territory
     */
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
    public int defendAgainst(Territory attacker) {
        assert owner != attacker.owner : "Why should i attack my own territories? ("+owner.getName()+")";

        Random dice = new Random();

        int defendingTroups = currentArmyCount >= 2? 2 : 1; // 1 is minimum
        int numberOfAttacks = 0;
        for (; numberOfAttacks < attacker.currentArmyCount-1; numberOfAttacks++) { // -1 because 1 army MUST be left behind
            if (numberOfAttacks ==3)
                break;
        }
        List<Integer> attackRolls = randomIntList(dice, numberOfAttacks);
        List<Integer> defendRolls = randomIntList(dice, defendingTroups);

        // TODO: remove eventually
        System.out.print("Battle!\nAttacking Troups: " +numberOfAttacks+ " [");
        for (int i:attackRolls) {
            System.out.print(i+";");
        }
        System.out.print("]\nDefending Troups: "+defendingTroups+ " [");
        for (int i:defendRolls) {
            System.out.print(i+";");
        }
        System.out.println("]");


        // iterate through attacks
        for (int i = 0; i < attackRolls.size();i++) {
            if (attackRolls.get(i)> (defendRolls.size()>=i ? defendRolls.get(i):0)) {
                // attack successful
                System.out.println("attack "+ (i+1) + " successful!");
                --defendingTroups;
                --this.currentArmyCount;
                if (currentArmyCount == 0)
                    return numberOfAttacks; // Territory conquered
                if (defendingTroups==0)
                    return -1;
            }
            else
            {
                // defense successful
                --numberOfAttacks;
                --attacker.currentArmyCount;
                System.out.println("attack "+ (i+1) + " failed!");

                defendRolls.add(dice.nextInt(6));

                // TODO: remove eventually
                System.out.println("new defens roll: "+defendRolls.get(defendRolls.size()-1));
            }
        }
        return -1;
    }

    /**
     * This function does return a sorted List of random generated numbers
     * which is used by the battle calculations
     *
     * @param dice a Randomgenerator which is used to fill the array
     * @param count how many elements the array should hold
     * @return a sorted array of randomly generated numbers
     */
    private List<Integer> randomIntList(Random dice, int count)
    {
        ArrayList<Integer> randoms = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            randoms.add(dice.nextInt(6));
        }
        Collections.sort(randoms);
        Collections.reverse(randoms);
        return randoms;
    }

    /**
     * @return the current active state of the Territory
     */
    public ActiveState getActiveState() {
        return active;
    }

    /**
     * @see ActiveState
     * @param active sets the current active state of the Territory
     */
    public void setActive(ActiveState active) {
        this.active = active;
    }
}
