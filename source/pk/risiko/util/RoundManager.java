package pk.risiko.util;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.Territory;

import java.util.List;

/**
 *
 *
 * @author Raphael Ludwig
 * @version 12.01.2016
 */
public class RoundManager {
    // still needs information about: current GameState (GameMap)
    // all the territories & continents (to be delegated to PlayerAI)

    private final CyclicList<Player> players = new CyclicList<>();
    private int currentRound;

    public RoundManager(List<Player> players) {
        this.players.addAll(players);
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    public Player nextPlayer() {
        Player p = players.next();
        if( this.players.isAtBeginning() ) this.currentRound++;

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
}
