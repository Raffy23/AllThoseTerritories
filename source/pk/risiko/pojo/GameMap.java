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
    private String mapName;
    private List<Territory> territories;
    private ArrayList<Continent> continents;

    public int getFreeTerritories() {
        return freeTerritories;
    }

    private int freeTerritories;

    public GameMap(String name, List<Territory> territories, ArrayList<Continent> continentList) {
        this.mapName = name;
        this.territories = territories;
        this.continents = continentList;
        freeTerritories = territories.size();
    }

    public String getMapName() {
        return this.mapName;
    }

    public List<Territory> getTerritories() {
        return this.territories;
    }

    // returns true if decrease was successful & another decrease is still possible
    // false if there are no freeTerritories left at the end oft the method
    public boolean decreaseFreeTerritories()
    {
        System.out.println("terr. leftover: "+ (freeTerritories-1));
        if (freeTerritories<=0)
            return false;

        freeTerritories--;
        return freeTerritories==0? false : true;
    }


    public ArrayList<Continent> getContinents() {
        return continents;
    }
}
