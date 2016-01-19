package pk.risiko.util;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.PlayerAI;
import pk.risiko.pojo.Territory;

import java.util.List;

/**
 * The RoundManager is responsible for the Roundmanagement and thus
 * handles the all Events (next player, set unit, reinforce unit)
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 17.01.2016
 */
public class RoundManager {

    private final CyclicList<Player> players = new CyclicList<>();
    private final GameMap gameMap;
    private int currentRound;
    private GameState currentGameState;

    public RoundManager(List<Player> players,GameMap map) {
        this.players.addAll(players);
        this.gameMap = map;
        this.currentGameState = GameState.SET_UNIT;
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
            if (this.currentGameState != GameState.SET_UNIT)
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
        switch (this.currentGameState)
        {
            case SET_UNIT:
                if (targetTerritory.getOwner() == null)
                {
                    //if(!(this.getCurrentPlayer() instanceof PlayerAI))
                    this.getCurrentPlayer().setUnit(targetTerritory);
                    this.nextPlayer();
                    if (!this.gameMap.decreaseFreeTerritories()) {
                        for (int i = 0; i < players.size(); i++) {
                            players.get(i).setReinforcements();
                        }
                        this.currentGameState = GameState.REINFORCE_UNITS;
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


        switch(this.currentGameState) {
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
