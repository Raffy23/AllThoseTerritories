package pk.risiko.pojo;

import java.awt.Color;

/**
 * This Class does represent the Player Entity
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class Player {
    private String name;
    private Color color;
    protected boolean human = true;
    private int reinforcements;

    public Player(String name,Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return this.color;
    }
    public int getReinforcements()
    {
        return reinforcements;
    }
    public void setReinforcements(int i)
    {
        reinforcements=i;
    }

    // returns true possible
    // false if no reinforcements left
    public boolean reinforcementPossible()
    {
        if (reinforcements<=0)
            return false;

        reinforcements--;
        return true;
    }

}
