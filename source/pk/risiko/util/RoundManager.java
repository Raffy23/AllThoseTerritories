package pk.risiko.util;

import pk.risiko.pojo.*;
import pk.risiko.ui.screens.GamePanel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 17.01.2016
 */
public class RoundManager {

    private final GamePanel gamePanel;
    private final GameMap gameMap;
    private final CyclicList<Player> players = new CyclicList<>();
    private int currentRound;

    public RoundManager(List<Player> players, GamePanel gp, GameMap gm) {
        this.players.addAll(players);
        this.gamePanel = gp;
        this.gameMap = gm;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    public Player nextPlayer() {
        Player p = players.next();

        //  this part will be placed somewhere else later => GameState is only checked once
        if ( this.getCurrentPlayer() instanceof PlayerAI)
        {
            PlayerAI playerAI = (PlayerAI) this.getCurrentPlayer();
            if (gamePanel.getCurrentGameState().equals(GameState.SET_UNIT))
                {
                    Territory t = playerAI.chooseTerritory(gameMap.getContinents());
                    this.Player_SET_UNIT(t);
                }
        }

        if( this.players.isAtBeginning() )
            if (!this.gamePanel.getCurrentGameState().equals(GameState.SET_UNIT))
                this.currentRound++;

        return p;
    }

    public boolean isAtTheBeginning() {
        return this.players.isAtBeginning();
    }

    // returns true if current Player still ownes at least 1 territory
    public boolean isPlayerAlive(GameMap map) {
        for(Territory t:map.getTerritories())
            if( t.getOwner().equals(getCurrentPlayer()) ) return true;

        return false;
    }

    // (rename if better name was found)
    public void manageActions(Territory targetTerritory)
    {
        switch (gamePanel.getCurrentGameState())
        {
            case SET_UNIT:
                if (targetTerritory.getOwner() == null)
                    if(!(this.getCurrentPlayer() instanceof PlayerAI))
                        Player_SET_UNIT(targetTerritory);
                break;
            case REINFORCE_UNITS:
                Player p = this.getCurrentPlayer();
                if (targetTerritory.getOwner().equals(p))
                    if (p.reinforcementPossible())
                        targetTerritory.increaseArmy(1);
                    if (p.getReinforcements()==0)
                        this.nextPlayer();
                break;
            case ATTACK_OR_MOVE_UNIT:
                break;
            case NEXT_ROUND:
                break;
        }
        //System.out.println("GameState: " + gamePanel.getCurrentGameState());
    }
    private void setPlayerReinforcements()
    {
        /* TODO: replace reinforcement count for player*/
        //replace with correct numbers due to owned territories/continents
        players.get(0).setReinforcements(5);
        players.get(1).setReinforcements(5);
    }
    private void Player_SET_UNIT(Territory t)
    {
        t.setOwner(this.getCurrentPlayer());
        t.increaseArmy(1);
        this.nextPlayer();
        if (!gameMap.decreaseFreeTerritories()) {
            setPlayerReinforcements();
            gamePanel.changeState(GameState.REINFORCE_UNITS);
            //this.nextPlayer(); // set to Player , not Computer
        }
    }
}
