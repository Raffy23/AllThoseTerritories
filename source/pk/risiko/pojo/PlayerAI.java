package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the AI system of the Game
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 08.01.2016
 */
public abstract class PlayerAI extends Player implements AI {

    public PlayerAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
        this.human = false;
    }

    //TODO: look into ContinentBasedAI -> i moved it there

    /* Helper Methods for various reasons: */
    protected List<Territory> getAllEmptyTerritories() {
        List<Territory> territories = new ArrayList<>();

        this.gameMap.getTerritories().forEach(t -> {
            if(t.getOwner() == null) territories.add(t);
        });

        return territories;
    }

    protected List<Territory> getMyTerritories() {
        List<Territory> territories = new ArrayList<>();

        this.gameMap.getTerritories().forEach(t -> {
            if(t.getOwner().equals(this)) territories.add(t);
        });

        return territories;
    }

    protected List<Territory> getAllBorderTerritories() {
        List<Territory> territories = new ArrayList<>();

        this.gameMap.getTerritories().forEach(t -> {
            if(t.getOwner().equals(this))
                for(Territory n:t.getNeighbours())
                    if(!n.getOwner().equals(this)) {
                        territories.add(t);
                        break;
                    }
        });

        return territories;
    }
}
