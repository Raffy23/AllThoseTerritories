package pk.risiko.pojo;

import jdk.nashorn.internal.runtime.Context;

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
    private String name;
    private int value;
    private List<Territory> territories = new ArrayList<Territory>();

    public Continent(String n, int v)
    {
        name=n;
        value=v;
    }

    public int getValue() {  return this.value; }
    public void addTerritory(Territory t)
    {
        territories.add(t);
    }
    public void addTerritory(Collection<Territory> t)
    {
        for (Iterator iterator = t.iterator(); iterator.hasNext();) {
            Territory territory = (Territory) iterator.next();
            territories.add(territory);
        }
    }

    public List<Territory> getTerritories() {
        return territories;
    }

    public boolean isOpenedByPlayer(Player p) {
        for(Territory t:this.territories)
            if(t.getOwner() != p) return false;

        return true;
    }
}
