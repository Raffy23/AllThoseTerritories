package pk.risiko.pojo;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * TODO: Placeholder for the AI of this game ...
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class PlayerAI extends Player {

    public PlayerAI(String name, Color color) {
        super(name, color);
        this.human = false;

    }

    // should choose a territory based on any criteria
    public Territory chooseTerritory(ArrayList<Continent> continentList)
    {
        ArrayList<Territory> freeTerritories = new ArrayList<Territory>();
        for (int i = 0; i < continentList.size(); i++) {
            for (int k = 0; k < continentList.get(i).getTerritories().size(); k++) {
                if (continentList.get(i).getTerritories().get(k).getOwner()==null)
                    freeTerritories.add(continentList.get(i).getTerritories().get(k));
            }
        }

        Random rand = new Random();
        int randomNum = rand.nextInt(freeTerritories.size());
        return freeTerritories.get(randomNum);
    }

}
