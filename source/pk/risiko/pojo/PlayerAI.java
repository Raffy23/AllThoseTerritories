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
    public Territory chooseTerritory(ArrayList<Territory> territories)
    {
        ArrayList<Territory> freeTerritories = new ArrayList<Territory>();
        int j=0;
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getOwner()==null)
                freeTerritories.add(j++,territories.get(i));
        }

        Random rand = new Random();

        int randomNum = rand.nextInt(freeTerritories.size());

        return freeTerritories.get(randomNum);
    }

}
