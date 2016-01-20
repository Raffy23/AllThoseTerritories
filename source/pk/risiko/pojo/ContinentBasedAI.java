package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * TODO: I moved your AI stuff here, and used the PlayerAI as Base Class
 *
 * @author Wilma Weixelbaum, Raphael Ludwig
 * @version 19.01.2016
 */
public class ContinentBasedAI extends PlayerAI {

    public ContinentBasedAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
    }

    // should choose a territory based on any criteria
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
    private Territory chooseRandom(ArrayList<Territory> territories)
    {
        Random rand = new Random();
        int randomNum = rand.nextInt(territories.size());

        return territories.get(randomNum);
    }


    @Override
    public Territory setUnitAction() {
        return this.chooseFreeTerritory();
    }

    @Override
    public List<Territory> reinforceUnitsAction() {
        List<Territory> targets = new ArrayList<>(this.getReinforcements());

        for (int i = 0; i < this.getReinforcements();i++) {
            if (this.isReinforcementPossible())
                targets.add(this.chooseOwnedTerriory());
        }

        return targets;
    }

    @Override
    public List<Tripel<AiTroupState,Territory,Territory>> moveOrAttackAction() {
        return new ArrayList<>();
    }
}
