package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * TODO: Placeholder for the AI of this game ...
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class PlayerAI extends Player {

    public PlayerAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
        this.human = false;

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
}
