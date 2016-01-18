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
    protected static GameMap gameMap;

    public Player(String name, Color color, GameMap gm) {
        this.name = name;
        this.color = color;
        this.gameMap = gm;
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
    public void setReinforcements()
    {
        //replace with calculated number
        reinforcements=5;
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
    public void setUnit(Territory t)
    {
        t.setOwner(this);
        t.increaseArmy(1);
    }

    public static boolean decreaseFreeTerritories() {
        return gameMap.decreaseFreeTerritories();
    }
}
