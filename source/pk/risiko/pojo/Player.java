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
 * @author Raphael Ludwig, Wilma Weixelbaum
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
     * once was used to restrict attacks - not needed anymore
     */
    protected boolean attackAvailable=true;

    /**
     * This is the territory that units are moved to
     */
    private Territory receivingTerritory=null;

    /**
     * This is the territory that units are moved from
     */
    private Territory sendingTerritory=null;

    /**
     * This is the last territory that started an attack
     */
    private Territory fightingTerritory=null;

    /**
     * This is the last territory, that was conquered by the fightingTerritory
     */
    private Territory conqueredTerritory=null;

    /**
     * This holds the number of moved units from the sending to the receiving territory
     */
    private int moveCount=0;

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
            if( c.isOwnedByPlayer(this) ) {
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
    public void conquerTerritory(Territory t, int survivors)
    {
        this.gameMap.decreaseFreeTerritories();
        if (this.getCurrentActiveTerritory()!=null) {
            this.getCurrentActiveTerritory().decreaseArmy(survivors);
            conqueredTerritory=t;
        }
        t.setOwner(this);
        t.increaseArmy(survivors);
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

    /**
     * This method moves units from the selected Territory or attacks a given territory
     * @param targetTerritory either receives units or is attacked
     * @return false if targetTerritory was not conquered
     */
    public boolean attackOrMove(Territory targetTerritory) {
        if (targetTerritory.getMouseState() == MouseState.R_CLICKED) {
            // attack
            if (!targetTerritory.getOwner().equals(this.getCurrentActiveTerritory().getOwner())) {
                if (attackAvailable) {
                    //System.out.println(this.getName() + " attacks " + targetTerritory.getName());
                    // TODO: comment this line to attack more often per round
                    //attackAvailable = false;
                    this.fightingTerritory = this.getCurrentActiveTerritory();

                    int survivors=targetTerritory.defendAgainst(this.getCurrentActiveTerritory());
                    if (survivors>0) {
                        this.conquerTerritory(targetTerritory, survivors);
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                tryMove(targetTerritory);
                return true;
            }
        }
        // trail per left-click!
        else if (targetTerritory.getMouseState() == MouseState.L_CLICKED&&targetTerritory==conqueredTerritory&&this.currentActiveTerritory==fightingTerritory)
        {
            System.out.println("trail");
            if (this.getCurrentActiveTerritory().getStationedArmies()>1) {
                this.getCurrentActiveTerritory().decreaseArmy(1);
                targetTerritory.increaseArmy(1);
            }
        }
        return false;
    }

    /**
     * This is a helper method. Units are only moved to
     * a given territory, if a move action is still possible (one per round)
     * @param targetTerritory would be the receiving territory
     */
    private void tryMove(Territory targetTerritory)
    {
        if (this.getCurrentActiveTerritory().getStationedArmies()>1) {
            if (receivingTerritory == null && sendingTerritory == null) {
                receivingTerritory = targetTerritory;
                sendingTerritory = this.getCurrentActiveTerritory();
                moveCount++;
            } else if (receivingTerritory == targetTerritory && sendingTerritory == this.getCurrentActiveTerritory()) {
                moveCount++;
            } else if (receivingTerritory == this.getCurrentActiveTerritory() && sendingTerritory == targetTerritory) {
                if (moveCount == 1) {
                    receivingTerritory = null;
                    sendingTerritory = null;
                    moveCount = 0;
                } else if (moveCount > 1)
                    moveCount--;
            } else {
                return;
            }
            this.getCurrentActiveTerritory().decreaseArmy(1);
            targetTerritory.increaseArmy(1);
        }
    }

    /**
     * This mothod resets all the round-based restrictions
     */
    public void setNewRoundDefaults()
    {
        this.attackAvailable=true;
        receivingTerritory=null;
        sendingTerritory=null;
        fightingTerritory=null;
        conqueredTerritory=null;
        moveCount=0;
    }

    /** @return true if current Player still ownes at least 3 territoroies with at least 1 army
        or less than 3 territories with more armies than territories
     **/
    public boolean isAlive() {
        int count = 0;
        int armycount=0;
        for(Territory t:gameMap.getTerritories()) {
            if (this.equals(t.getOwner())) {
                count++;
                armycount+=t.getStationedArmies();
            }
        }

        return (count>=3||armycount>count);
    }

    /**
     * @param map is the new gameMap
     */
    public void setGameMap(GameMap map) {
        this.gameMap = map;
    }

    /**
     * @return the current moveCount between the 2 territories,
     * that are involved in the moving process
     */
    public int getMoveCount() {
        return moveCount;
    }
}
