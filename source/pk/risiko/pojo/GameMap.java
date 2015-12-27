package pk.risiko.pojo;

import java.awt.*;
import java.util.List;

/**
 * This class represents a GameMap which is constructed by some
 * Territories
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameMap implements Drawable {
    private String mapName;
    private List<Territory> territories;

    public GameMap(String name,List<Territory> territories) {
        this.mapName = name;
        this.territories = territories;
    }

    public String getMapName() {
        return this.mapName;
    }

    @Override
    public void paint(Graphics g) {
        this.territories.forEach(territory -> territory.paint(g));
    }
}
