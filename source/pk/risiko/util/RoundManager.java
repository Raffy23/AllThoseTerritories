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
 * @version 22.01.2016
 */
public class RoundManager {

    private final CyclicList<Player> players = new CyclicList<>();
    private final GameMap gameMap;
    private int currentRound;
    private GameState currentGameState;
    private String lastAction;

    private final AsyncAIActionDispatcher aiDispatcher;

    public RoundManager(List<Player> players,GameMap map,AsyncAIActionDispatcher aiDispatcher) {
        this.players.addAll(players);
        this.gameMap = map;
        this.currentGameState = GameState.SET_UNIT;
        this.aiDispatcher = aiDispatcher;
        lastAction = "Game hast started ... ";
    }

    public void startGame() {
        //fixes round 0 problem
        if( players.get(0) instanceof AI ) {
            manageAIActions();
            this.aiDispatcher.startDispatching();
        }
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    public Player nextPlayer() {
        Player p = players.peek();

        //Block next action if player has still reinforcements
        if( currentGameState == GameState.REINFORCE_UNITS &&
            p.getReinforcements() > 0  && !(p instanceof AI) ) return p;
        else p = players.next();


        if( this.players.isAtBeginning() )
            if (this.currentGameState == GameState.REINFORCE_UNITS) {
                this.currentRound++;
            } else if(this.currentGameState == GameState.ATTACK_OR_MOVE_UNIT) {

                //TODO: switch to GameState new round? + win/lose
                setNewRoundDefaults();
                calculateReinforcements();
                currentGameState = GameState.REINFORCE_UNITS;
            }

        if( currentGameState != GameState.SET_UNIT )
            if( players.stream().filter(p1 -> p1.getReinforcements()>0).count() == 0) {
                currentGameState = GameState.ATTACK_OR_MOVE_UNIT;
            }

        if( p instanceof PlayerAI ) {
            manageAIActions();
            this.aiDispatcher.startDispatching();
            this.lastAction = null;
        }

        return this.getCurrentPlayer();
    }

    public boolean isAtTheBeginning() {
        return this.players.isAtBeginning();
    }

    private void calculateReinforcements() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).calculateReinforcements();
        }
    }
    private void setNewRoundDefaults()
    {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setNewRoundDefaults();
        }
    }

    private void doActionOn(Territory targetTerritory)
    {
        Player p = this.getCurrentPlayer();

        switch (this.currentGameState)
        {
            case SET_UNIT:
                if (targetTerritory.getOwner() == null)
                {
                    this.lastAction = p.getName() + " conquered " + targetTerritory.getName();
                    //if(!(this.getCurrentPlayer() instanceof PlayerAI))
                    this.getCurrentPlayer().conquerTerritory(targetTerritory,1);
                    if (!this.gameMap.hasFreeTerrotories()) { //lowering is done above

                        this.calculateReinforcements();
                        this.currentGameState = GameState.REINFORCE_UNITS;

                        //should not jump to the start of the player list?
                        this.players.reset();
                        this.players.prev();
                    }

                    if( !(p instanceof AI) ) this.nextPlayer();
                }
                break;
            case REINFORCE_UNITS:
                this.lastAction = p.getName() + " reinforces " + targetTerritory.getName();
                //System.out.println(p.getName() + " reinforces territory " + targetTerritory.getName() + "(owern="+targetTerritory.getOwner().getName()+",posssible="+p.isReinforcementPossible()+",left="+p.getReinforcements()+")");

                if (targetTerritory.getOwner().equals(p))
                    if (p.reinforcementPossible())
                        targetTerritory.increaseArmy(1);

                if (p.getReinforcements()==0) {
                    if( !(p instanceof AI) ) this.nextPlayer();
                }
                break;
            case ATTACK_OR_MOVE_UNIT:
                if( p.getCurrentActiveTerritory() != null) {
                    if (p.getCurrentActiveTerritory().getNeighbours().contains(targetTerritory)) {

                        if( p.getCurrentActiveTerritory().getOwner().equals(targetTerritory.getOwner()) && p.getMoveCount()==0 )
                            lastAction = p.getName() + " moves units to " + targetTerritory.getName();
                        else //if (p.getAttackAvailable())
                            lastAction = p.getName() + " attacks " + targetTerritory.getName();

                        p.attackOrMove(targetTerritory);
                    }
                    p.setCurrentActiveTerritory(null);
                } else {
                    if( targetTerritory.getOwner().equals(p) ) {
                        p.setCurrentActiveTerritory(targetTerritory);
                        System.out.println("Info: Target Territory selected!");
                    }
                }
                break;
        }
    }

    public Player checkWinLose() {
        if (players==null||(currentGameState!=GameState.ATTACK_OR_MOVE_UNIT&&currentGameState!=GameState.REINFORCE_UNITS))
            return null;

        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).isAlive()) {
                System.out.println("Player "+ players.get(i).getName()+" is defeated.");
                players.remove(i); // remove player - since he has already lost
            }
        }
        if (players.size()==1) {
            System.out.println("Player "+players.get(0).getName()+" wins!");

            return players.get(0);
        }

        return null;
    }

    public boolean isOnePlayerLeft() {
        return this.players.size() == 1;
    }

    private void manageAIActions()
    {
        if (!(this.getCurrentPlayer() instanceof PlayerAI))
            return;
        PlayerAI playerAI = (PlayerAI) this.getCurrentPlayer();

        switch(this.currentGameState) {
            case SET_UNIT: doActionOn(playerAI.setUnitAction()); break;
            case REINFORCE_UNITS:
                //is dead allready:
                if( !playerAI.isAlive() ) this.nextPlayer();

                List<Territory> reinforcements = playerAI.reinforceUnitsAction();
                reinforcements.forEach(this::doActionOn);

                //if can't set Player as fewer than 3 Territories
                if( reinforcements.isEmpty() ) this.nextPlayer();
                break;
            case ATTACK_OR_MOVE_UNIT:
                this.aiDispatcher.queueActions(playerAI.moveOrAttackAction());
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

    public String getLastAction() {
        return (getCurrentPlayer() instanceof  AI)?this.aiDispatcher.getNextAction():
               (this.lastAction!=null)?this.lastAction:this.aiDispatcher.getNextAction();
    }
}
