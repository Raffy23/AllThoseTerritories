package pk.risiko.util;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.Territory;

import java.util.List;

/**
 * Created by
 *
 * @author Raphael
 * @version 12.01.2016
 */
public class RoundManager {

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

    public boolean isPlayerAlive(GameMap map) {
        for(Territory t:map.getTerritories())
            if( t.getOwner().equals(getCurrentPlayer()) ) return true;

        return false;
    }
}
