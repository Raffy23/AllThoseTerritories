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
    protected /*static*/ GameMap gameMap; //should not be static! seems strange

    private Territory currentActiveTerritory;

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
    public void calculateReinforcements() {
        this.reinforcements = 0;

        //reinforcements by held continents:
        this.gameMap.getContinents().forEach(c -> {
            if( c.isOpenedByPlayer(this) ) {
                this.reinforcements += c.getValue();
                System.out.println(name + " holds continent " + c.getValue());
            }
        });

        //reinforcements by 3 held territories
        int heldTerritories = 0;
        for(Territory t:this.gameMap.getTerritories())
            if(t.getOwner() == this) heldTerritories++;

        System.out.println(name + " holds " + heldTerritories + " territories");
        this.reinforcements += heldTerritories/3;
    }

    // returns true possible
    // false if no reinforcements left
    public boolean reinforcementPossible()
    {
        if( !this.human ) System.out.println(reinforcements<=0);
        if (reinforcements<=0) //should not < 0 be a error?
            return false;

        reinforcements--; //nice should be documented somehow
        return true;
    }

    public boolean isReinforcementPossible() {
        return reinforcements>=0;
    }

    public void conquerTerritory(Territory t)
    {
        this.gameMap.decreaseFreeTerritories();
        t.setOwner(this);
        t.increaseArmy(1);
    }

    public Territory getCurrentActiveTerritory() {
        return currentActiveTerritory;
    }

    public void setCurrentActiveTerritory(Territory currentActiveTerritory) {
        this.currentActiveTerritory = currentActiveTerritory;
    }


    //why does player have a static method to a local in game map?
    /*public static boolean decreaseFreeTerritories() {
        return gameMap.decreaseFreeTerritories();
    }*/

}
