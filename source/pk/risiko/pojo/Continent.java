package pk.risiko.pojo;

import java.util.ArrayList;
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

    public List<Territory> getTerritories() {
        return territories;
    }
}
