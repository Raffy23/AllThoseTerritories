package pk.risiko.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the GameMap which is constructed by the Continents that include
 * Territories
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 17.01.2016
 */
public class GameMap {
    /**
     * The name of the map, can be anything that identifies the
     * map (mostly the filename of the mapfile)
     */
    private final String mapName;

    /**
     * All territories are stored here too for easy access
     */
    private final List<Territory> territories;

    /**
     * All continents which are present in the map are stored here,
     * they must contain at least all territories which are stored
     * in territories
     */
    private final ArrayList<Continent> continents;

    /**
     * A simple counter that keeps track of all free territories
     */
    private int freeTerritories;

    /**
     * To construct a GameMap, the name, territories and continents must be known.
     * The continents must at least contain all the territories in the list!
     *
     * @param name Name of the GameMap which does identify it
     * @param territories List of all territories in the map
     * @param continentList list of all continents
     */
    public GameMap(String name, List<Territory> territories, ArrayList<Continent> continentList) {
        this.mapName = name;
        this.territories = territories;
        this.continents = continentList;
        freeTerritories = territories.size();
    }

    /**
     * @return a per GameMap unique name which can be set in the constructor
     */
    public String getMapName() {
        return this.mapName;
    }

    /**
     * @return a list of territories which are present in this GameMap Object
     */
    public List<Territory> getTerritories() {
        return this.territories;
    }

    /**
     * returns true if decrease was successful and another decrease is still possible
     * false if there are no freeTerritories left at the end oft the method
     *
     * @return true if the GameMap has any free Territories
     */
    public boolean decreaseFreeTerritories()
    {
        if (freeTerritories<=0)
            return false;

        freeTerritories--;
        return freeTerritories==0? false : true;
    }

    /**
     * This function does not change the internal state of the GameMap!
     *
     * @return true if the GameMap has any free Territories
     */
    public boolean hasFreeTerrotories() {
        return freeTerritories != 0;
    }

    /**
     * @return a list of continents which are present in the GameMap Object
     */
    public ArrayList<Continent> getContinents() {
        return continents;
    }
}
