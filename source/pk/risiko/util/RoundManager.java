package pk.risiko.util;

import pk.risiko.pojo.AI;
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

        if( this.players.isAtBeginning() )
            if (this.currentGameState == GameState.REINFORCE_UNITS) {
                this.currentRound++;
            } else if(this.currentGameState == GameState.ATTACK_OR_MOVE_UNIT) {
                calculateReinforcements();
                currentGameState = GameState.REINFORCE_UNITS;
            }

        if( currentGameState != GameState.SET_UNIT )
            if( players.stream().filter(p1 -> p1.getReinforcements()>0).count() == 0) {
                currentGameState = GameState.ATTACK_OR_MOVE_UNIT;
            }


        if (this.getCurrentPlayer() instanceof PlayerAI)
            manageAIActions();

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

    private void calculateReinforcements() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).calculateReinforcements();
        }
    }

    // (rename if better name was found)
    private void doActionOn(Territory targetTerritory)
    {
        Player p = this.getCurrentPlayer();

        switch (this.currentGameState)
        {
            case SET_UNIT:
                if (targetTerritory.getOwner() == null)
                {
                    //if(!(this.getCurrentPlayer() instanceof PlayerAI))
                    this.getCurrentPlayer().conquerTerritory(targetTerritory);
                    if (!this.gameMap.hasFreeTerrotories()) { //lowering is done above
                        this.calculateReinforcements();
                        this.currentGameState = GameState.REINFORCE_UNITS;

                        //should not jup to the start of the player list?
                        this.players.reset();
                        this.players.prev();
                    }
                    this.nextPlayer();

                }
                break;
            case REINFORCE_UNITS:
                System.out.println(p.getName() + " reinforces territory " + targetTerritory.getName() + "(owern="+targetTerritory.getOwner().getName()+",posssible="+p.isReinforcementPossible()+",left="+p.getReinforcements()+")");

                if (targetTerritory.getOwner().equals(p))
                    if (p.reinforcementPossible())
                        targetTerritory.increaseArmy(1);
                if (p.getReinforcements()==0) {
                    System.out.println("NEXT_PLAYER");
                    this.nextPlayer();
                }
                break;
            case ATTACK_OR_MOVE_UNIT:
                if( p.getCurrentActiveTerritory() != null ) {

                    if( !targetTerritory.getOwner().equals(p.getCurrentActiveTerritory().getOwner())) {
                        if (!targetTerritory.defendAgainst(p.getCurrentActiveTerritory()))
                            p.conquerTerritory(targetTerritory);
                    } else {
                        if( p.getCurrentActiveTerritory().decreaseArmy(1) )
                            targetTerritory.increaseArmy(1);
                    }

                    p.setCurrentActiveTerritory(null);
                } else {
                    if( targetTerritory.getOwner().equals(p) ) {
                        p.setCurrentActiveTerritory(targetTerritory);
                        System.out.println("Info: Target Territory selected!");
                    }
                }

                if( p instanceof  AI ) nextPlayer();
                break;
            case NEXT_ROUND:
                break;
        }
    }

    private void manageAIActions()
    {
        if (!(this.getCurrentPlayer() instanceof PlayerAI))
            return;
        AI playerAI = (AI) this.getCurrentPlayer();

        switch(this.currentGameState) {
            case SET_UNIT: doActionOn(playerAI.setUnitAction()); break;
            case REINFORCE_UNITS:
                playerAI.reinforceUnitsAction().forEach(this::doActionOn);
                break;
            case ATTACK_OR_MOVE_UNIT:
                playerAI.moveOrAttackAction().forEach(action -> {
                    getCurrentPlayer().setCurrentActiveTerritory(action.y);
                    doActionOn(action.z);
                });
                break;
        }

    }
    public void manageActions(Territory targetTerritory)
    {
        doActionOn(targetTerritory);
    }

    public GameState getCurrentGameState() {
        return this.currentGameState;
    }
}
