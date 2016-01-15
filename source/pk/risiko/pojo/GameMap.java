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
    private int freeterritories;

    public GameMap(String name,List<Territory> territories) {
        this.mapName = name;
        this.territories = territories;
    }

    public String getMapName() {
        return this.mapName;
    }

    public List<Territory> getTerritories() {
        return this.territories;
    }


}
