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
    private final CyclicList<Player> players = new CyclicList<>();
    private int currentRound;

    public RoundManager(List<Player> players, GamePanel gp) {
        this.players.addAll(players);
        this.gamePanel = gp;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    public Player nextPlayer() {
        Player p = players.next();

        if (this.getCurrentPlayer() instanceof PlayerAI)
            manageActions();

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
    private void switchStates(Territory targetTerritory)
    {
        switch (gamePanel.getCurrentGameState())
        {
            case SET_UNIT:
                if (targetTerritory.getOwner() == null)
                {
                    //if(!(this.getCurrentPlayer() instanceof PlayerAI))
                    this.getCurrentPlayer().setUnit(targetTerritory);
                    this.nextPlayer();
                    if (!Player.decreaseFreeTerritories()) {
                        for (int i = 0; i < players.size(); i++) {
                            players.get(i).setReinforcements();
                        }
                        gamePanel.changeState(GameState.REINFORCE_UNITS);
                    }
                }
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
    }

    private void manageActions()
    {
        if (!(this.getCurrentPlayer() instanceof PlayerAI))
            return;
        PlayerAI playerAI = (PlayerAI) this.getCurrentPlayer();


        switch(gamePanel.getCurrentGameState()) {
            case SET_UNIT:
                switchStates(playerAI.chooseFreeTerritory());
            break;
            case REINFORCE_UNITS:
                for (int i = 0; i < playerAI.getReinforcements();) {
                    switchStates(playerAI.chooseOwnedTerriory());
                }

            break;
        }
    }
    public void manageActions(Territory targetTerritory)
    {
        switchStates(targetTerritory);
    }
}
