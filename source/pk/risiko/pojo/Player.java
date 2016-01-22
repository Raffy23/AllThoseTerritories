package pk.risiko.pojo;

import java.awt.Color;

/**
 * This Class does represent the Player Entity. This Entity can be
 * either a Human Player or a AI. If this Entity is a AI than a
 * subclass must implement the AI interface.
 *
 * Besides that this class does store the name and the reinforcements
 * which the player can set.
 *
 * @author Raphael Ludwig
 * @version 08.01.2016
 */
public class Player {
    /**
     * The Name of the Player
     */
    private String name;
    /**
     * The Color of the Player, all territories are marked with
     * that color
     */
    private Color color;
    /**
     * The reinforcements the player can currently set to his own
     * countries.
     */
    private int reinforcements;
    /**
     * The current GameMap on which the Player does play
     */
    protected GameMap gameMap;
    /**
     * The current active (selected) territory from which
     * the player does perform actions in the ATTACK_OR_MOVE GameState
     */
    private Territory currentActiveTerritory;


    /**
     *
     * @param name The Name of the Player
     * @param color The color, in which the territories should be colored
     * @param gm The current GameMap, on which the Player does want to play
     */
    public Player(String name, Color color, GameMap gm) {
        this.name = name;
        this.color = color;
        this.gameMap = gm;
    }

    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * @return the color of the player
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * @return the number of reinforcements the player currently has
     */
    public int getReinforcements()
    {
        return reinforcements;
    }

    /**
     * This Method does calculate the Number of reinforcements the
     * Player gets based on his currently owned territories
     */
    public void calculateReinforcements() {
        //reset the reinforcements
        this.reinforcements = 0;

        //reinforcements by held continents:
        this.gameMap.getContinents().forEach(c -> {
            if( c.isOpenedByPlayer(this) ) {
                this.reinforcements += c.getValue();
            }
        });

        //reinforcements by 3 held territories
        int heldTerritories = 0;
        for(Territory t:this.gameMap.getTerritories())
            if(t.getOwner() == this) heldTerritories++;

        this.reinforcements += heldTerritories/3;
    }

    /**
     * This Method does also decrease the number of reinforcements of the player.
     * (It is assumed that the player does try to set any reinforcement anywhere)
     *
     * @return true if possible, false if no reinforcements left
     */
    public boolean reinforcementPossible()
    {
        if (reinforcements<=0) //should not < 0 be a error?
            return false;

        reinforcements--; //nice should be documented somehow
        return true;
    }

    /**
     * @return true if the player as more than 0 reinforcements and can therefore place any
     */
    public boolean isReinforcementPossible() {
        return reinforcements>=0;
    }

    /**
     * This method does set the owner of the given territory and increases the army count to 1
     * @param t any free or held territory with 0 stationed units
     */
    public void conquerTerritory(Territory t)
    {
        this.gameMap.decreaseFreeTerritories();
        t.setOwner(this);
        t.increaseArmy(1);
    }

    /**
     * @return the currently active (selected) territory
     */
    public Territory getCurrentActiveTerritory() {
        return currentActiveTerritory;
    }

    /**
     * Setting the active Territory to a Player does invoke a fairly simple process in marking
     * all other countries which are connected with this through the border as "attackable"
     * @param currentActiveTerritory any territory owned by the player or null
     */
    public void setCurrentActiveTerritory(Territory currentActiveTerritory) {
        //Null check
        if( this.currentActiveTerritory != null ) {
            this.currentActiveTerritory.setActive(Territory.ActiveState.NONE);      //release mark from old territory
            this.currentActiveTerritory.getNeighbours().forEach(n -> {              //release mark from all borders
                if(n.getActiveState() == Territory.ActiveState.HOSTILE)
                    n.setActive(Territory.ActiveState.NONE);
            });
        }

        //set new active territory
        this.currentActiveTerritory = currentActiveTerritory;

        //Null check
        if( this.currentActiveTerritory != null ) {
            this.currentActiveTerritory.setActive(Territory.ActiveState.OWN);   //mark new active territory
            this.currentActiveTerritory.getNeighbours().forEach(n -> {          //mark all border territories
                if(n.getActiveState() == Territory.ActiveState.NONE)
                    n.setActive(Territory.ActiveState.HOSTILE);
            });
        }
    }
}
