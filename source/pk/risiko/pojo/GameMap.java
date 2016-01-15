package pk.risiko.pojo;

import java.util.List;

/**
 * This class represents a GameMap which is constructed by some
 * Territories
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameMap {
    private String mapName;
    private List<Territory> territories;
    private int freeTerritories;

    public GameMap(String name,List<Territory> territories) {
        this.mapName = name;
        this.territories = territories;
        freeTerritories = territories.size();
    }

    public String getMapName() {
        return this.mapName;
    }

    public List<Territory> getTerritories() {
        return this.territories;
    }

    // returns true if possible
    // false if there are no freeTerritories
    public boolean decreaseFreeTerritories()
    {
        System.out.println("lefttover "+ (freeTerritories-1));
        if (freeTerritories<=0)
            return false;

        freeTerritories--;
        return freeTerritories==0? false : true;
    }


}
