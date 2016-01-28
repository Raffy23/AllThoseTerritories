package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the base AI of the Game.
 * It does have no logic implemented which should be
 * done in any of the AI Method. It does contain useful
 * functions for the AI to be able to perform the same
 * actions as the player does (by looking at the screen)
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 08.01.2016
 */
public abstract class PlayerAI extends Player implements AI {

    /**
     * @see Player
     */
    public PlayerAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
    }

    /* Below there are Helper Methods for various reasons: */

    /**
     * @return all empty territories in the current GameMap
     */
    protected List<Territory> getAllEmptyTerritories() {
        return gameMap.getTerritories().stream().filter(t -> t.getOwner()==null).collect(Collectors.toList());
    }

    /**
     * @return all territories which are owned by this player
     */
    protected List<Territory> getMyTerritories() {
        return gameMap.getTerritories().stream().filter(t -> t.getOwner() != null && t.getOwner().equals(this))
                                                .collect(Collectors.toList());
    }

    /**
     * @return all territories which have a neighbour which is not owned by this player
     */
    protected List<Territory> getAllBorderTerritories() {
        List<Territory> territories = new ArrayList<>();

        this.getMyTerritories().forEach(t -> {
            for (Territory n : t.getNeighbours())
                if (!n.getOwner().equals(this)) {
                    territories.add(t);
                    break;
                }
        });

        return territories;
    }

    @Override
    public void attackOrMove(Territory targetTerritory) {
        targetTerritory.setMouseState(MouseState.R_CLICKED);

        super.attackOrMove(targetTerritory);

        targetTerritory.setMouseState(MouseState.NORMAL);
    }
}
