package pk.risiko.pojo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Completely random based AI
 *
 * @author Raphael Ludwig
 * @version 19.01.2016
 */
public class RandomAI extends PlayerAI {
    /**
     * The Random generator that is used to decide what should be done
     */
    private final Random rand = new Random();

    /**
     * @see Player
     */
    public RandomAI(String name, Color color, GameMap gm) {
        super(name, color, gm);
    }

    /**
     * This method does perforce a range-check so that the number only
     * can be between 0 and max (inclusively)
     * @param max max value
     * @return a random value between 0 and max
     */
    protected int throwDice(int max) {
        if( max < 1 ) return 0;
        return rand.nextInt(max);
    }

    /**
     * @see AI
     */
    @Override
    public Territory setUnitAction() {
        List<Territory> empties = this.getAllEmptyTerritories();
        assert !empties.isEmpty(): "Error: setUnitsToGameMap should not be called, there are no free territories!";

        return empties.get(throwDice(empties.size()-1));
    }

    /**
     * @see AI
     */
    @Override
    public List<Territory> reinforceUnitsAction() {
        List<Territory> my = this.getMyTerritories();
        List<Territory> border = this.getAllBorderTerritories();
        assert  !my.isEmpty() : "Error: i'm already defeated so what should i do, crawl back from the dead?";

        List<Territory> targets = new ArrayList<>(this.getReinforcements());
        //set halve of the troups into some random territory
        for(int i=0;i<getReinforcements()/2;i++) targets.add(my.get(throwDice(my.size()-1)));
        //the other halve will be set to any territory which as an enemy neighbour
        for(int i=0;i<getReinforcements()-getReinforcements()/2;i++) targets.add(border.get(throwDice(border.size()-1)));

        assert getReinforcements() == targets.size() : "I must set more reinforcements! ("+targets.size()+"/"+getReinforcements()+")";
        return targets;
    }

    /**
     * @see AI
     */
    @Override
    public List<Tripel<AiTroupState,Territory,Territory>> moveOrAttackAction() {
        List<Territory> my = this.getAllBorderTerritories();
        assert  !my.isEmpty() : "Error: i'm already defeated so what should i do, crawl back from the dead?";

        List<Tripel<AiTroupState,Territory,Territory>> movements = new ArrayList<>();
        my = my.stream().filter(c -> c.getStationedArmies()>1).collect(Collectors.toList());

        //no units left ... i lost
        if( my.isEmpty() ) return movements;

        my.forEach(c -> {
            if( throwDice(100) >= 50 ) {
                List<Territory> enemy = c.getNeighbours().stream().filter(e -> !e.getOwner().equals(this)).collect(Collectors.toList());

                movements.add(new Tripel<>(AiTroupState.ATTACK, c, enemy.get(throwDice(enemy.size()))));
                int maxMovements = throwDice(c.getStationedArmies());
                for (int i = 0; i < maxMovements; i++)
                    if (throwDice(100) > 50)
                        movements.add(new Tripel<>(AiTroupState.MOVE, c, enemy.get(throwDice(enemy.size()))));
            }
        });

/*
        Territory c = my.get( throwDice(my.size()) );
        List<Territory> enemy = c.getNeighbours().stream().filter(e -> !e.getOwner().equals(this)).collect(Collectors.toList());

        movements.add(new Tripel<>(AiTroupState.ATTACK,c,enemy.get(throwDice(enemy.size()))));
        int maxMovements = throwDice(c.getStationedArmies());
        for(int i=0;i<maxMovements;i++)
            if( throwDice(100) > 50 )
                movements.add(new Tripel<>(AiTroupState.MOVE,c,enemy.get(throwDice(enemy.size()))));
*/
        return movements;
    }
}
