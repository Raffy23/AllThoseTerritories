package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This AI's is currently theoretically not in use and was planned to work
 * based on other criteria than random.
 *
 * @author Wilma Weixelbaum, Raphael Ludwig
 * @version 19.01.2016
 */
public class ContinentBasedAI extends PlayerAI {

    /**
     * For the creation of the name, color and gamemap must be known
     * @param name is the player-name
     * @param color is the representing color of the player
     * @param gm is the GameMap of the Game
     */
    public ContinentBasedAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
    }

    // should choose a territory based on any criteria
    /**
     * This method chooses a territory to own that has no owner yet, by random
     * @return Territory, that the AI wants to own
     */
    public Territory chooseFreeTerritory()
    {
        ArrayList<Territory> freeTerritories = new ArrayList<Territory>();
        for (int i = 0; i < this.gameMap.getContinents().size(); i++) {
            for (int k = 0; k < this.gameMap.getContinents().get(i).getTerritories().size(); k++) {
                if (this.gameMap.getContinents().get(i).getTerritories().get(k).getOwner()==null)
                    freeTerritories.add(this.gameMap.getContinents().get(i).getTerritories().get(k));
            }
        }
        return chooseRandom(freeTerritories);
    }

    /**
     * This method chooses a Territory, that the AI already owns
     * @return a random Territory, owned by the AI
     */
    public Territory chooseOwnedTerriory() {
        ArrayList<Territory> ownedTerritories = new ArrayList<Territory>();
        for (int i = 0; i < this.gameMap.getContinents().size(); i++) {
            for (int k = 0; k < this.gameMap.getContinents().get(i).getTerritories().size(); k++) {
                if (this.gameMap.getContinents().get(i).getTerritories().get(k).getOwner()==this)
                    ownedTerritories.add(this.gameMap.getContinents().get(i).getTerritories().get(k));
            }
        }

        Random rand = new Random();
        int randomNum = rand.nextInt(ownedTerritories.size());

        return chooseRandom(ownedTerritories);
    }

    /**
     * Chooses a random territory from a given list
     * @param territories holds the territories to choose from
     * @return a territory
     */
    private Territory chooseRandom(ArrayList<Territory> territories)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(territories.size());

        return territories.get(randomNum);
    }

    /**
     * If the AI should set a unit, it will choose a free territory
     * @return Territory, chosen by the AI's methods
     */
    @Override
    public Territory setUnitAction() {
        return this.chooseFreeTerritory();
    }

    /**
     * This method is executed if the AI should reinforce its units
     *
     * @return a list of the territories to reinforce
     */
    @Override
    public List<Territory> reinforceUnitsAction() {
        List<Territory> targets = new ArrayList<>(this.getReinforcements());

        for (int i = 0; i < this.getReinforcements();i++) {
            if (this.isReinforcementPossible())
                targets.add(this.chooseOwnedTerriory());
        }
        return targets;
    }

    /**
     * Overridden from the AI Interface
     *
     * not in use
     *
     * @see AI
     */
    @Override
    public List<Tripel<AiTroupState,Territory,Territory>> moveOrAttackAction() {
        return new ArrayList<>();
    }
}
