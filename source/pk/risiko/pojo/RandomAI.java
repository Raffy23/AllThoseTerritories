package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Completely random based AI
 *
 * @author Raphael Ludwig
 * @version 19.01.2016
 */
public class RandomAI extends PlayerAI {

    private final Random rand = new Random();

    public RandomAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
    }

    protected int throwDice(int max) {
        if( max < 1 ) return 0;
        return rand.nextInt(max);
    }

    @Override
    public Territory setUnitAction() {
        List<Territory> empties = this.getAllEmptyTerritories();
        assert empties.size() != 0 : "Error: setUnitsToGameMap should not be called, there are no free territories!";

        return empties.get(throwDice(empties.size()-1));
    }

    @Override
    public List<Territory> reinforceUnitsAction() {
        List<Territory> my = this.getMyTerritories();
        assert  my.size() != 0 : "Error: i'm already defeated so what should i do, crawl back from the dead?";

        List<Territory> targets = new ArrayList<>(this.getReinforcements());
        for(int i=0;i<getReinforcements();i++)
            targets.add(my.get(throwDice(my.size()-1)));

        return targets;
    }

    @Override
    public List<Tripel<AiTroupState,Territory,Territory>> moveOrAttackAction() {
        List<Territory> my = this.getAllBorderTerritories();
        assert  my.size() != 0 : "Error: i'm already defeated so what should i do, crawl back from the dead?";

        Territory mine   = my.get(throwDice(my.size()-1));
        Territory target = mine.getNeighbours().get(throwDice(mine.getNeighbours().size()-1));

        target.defendAgainst(mine);

        return null;
    }
}
