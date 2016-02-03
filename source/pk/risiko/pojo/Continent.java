package pk.risiko.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the informations of a capital
 *
 * @author Wilma Weixelbaum
 * @version 15.01.2016.
 */
public class Continent {

    /** The name of the Continent **/
    private String name;

    /** value stores the amount of armies, the player gets, when he owns the continent **/
    private int value;

    /** All the territories that belong to the same continent*/
    private List<Territory> territories = new ArrayList<>();

    /**
     * For the creation of the continent, the name and the value must be known
     * @param n is the name of the continent
     * @param v is the value of the continent
     */
    public Continent(String n, int v)
    {
        name=n;
        value=v;
    }

    /**
     * @return the value
     */
    public int getValue() {  return this.value; }

    /**
     * This method adds a territory to the contient
     * @param t is the Territory which is then added to the list of territories
     */
    public void addTerritory(Territory t)
    {
        territories.add(t);
        t.setContinent(this);
    }

    /**
     * This method adds a Collection of territories to the continent
     * @param t represents a bunch of territories that are added to the list of territories
     */
    public void addTerritory(Collection<Territory> t)
    {
        for (Iterator iterator = t.iterator(); iterator.hasNext();) {
            Territory territory = (Territory) iterator.next();
            territories.add(territory);
            territory.setContinent(this);
        }
    }
    /**
     * @reurn the list of territories
     */
    public List<Territory> getTerritories() {
        return territories;
    }
    /**
     * Checks, if a given Player ownes the whole continent (all the territories in it)
     * @param p is the Player
     * @return true if the given Player ownes the whole continent
     */
    public boolean isOwnedByPlayer(Player p) {
        for(Territory t:this.territories)
            if(t.getOwner() != p) return false;

        return true;
    }
    /**
     * @return the name of the continent
     */
    public String getName() {
        return this.name;
    }
}
