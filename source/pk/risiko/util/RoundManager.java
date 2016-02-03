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

    /** All players are stored in this list **/
    private final CyclicList<Player> players = new CyclicList<>();
    /** The actual GameMap is stored here **/
    private final GameMap gameMap;
    /** The current Round counter is stored here **/
    private int currentRound;
    /** currentGameState is the current GameState **/
    private GameState currentGameState;
    /** The last action String is the String which is displayed
     *  in the UserInterface so the Player can see what the AI did
     *  **/
    private String lastAction;

    /** This Dispatcher does handly all AI action in an separate Thread **/
    private final AsyncAIActionDispatcher aiDispatcher;

    /**
     * After construction the RoundManager it does not start into Round 0 automatically,
     * you must call #{startGame} manually to start it!
     *
     * @param players a list of players which are in the game
     * @param map the current gameMap
     * @param aiDispatcher the dispatcher which handles ai interactions
     * @param loaded true if the game was loaded or false if it's a new game
     */
    public RoundManager(List<Player> players,GameMap map,AsyncAIActionDispatcher aiDispatcher,boolean loaded) {
        this.players.addAll(players);
        this.gameMap = map;
        this.currentGameState = loaded?GameState.REINFORCE_UNITS:GameState.SET_UNIT;
        this.aiDispatcher = aiDispatcher;
        lastAction = "Game hast started ... ";
    }

    /**
     * Starts the Game, if the first Player is AI
     * Dispatching does start here otherwise it
     * is waited for the Player to do some input
     */
    public void startGame() {
        //fixes round 0 problem
        if( players.get(0) instanceof AI ) {
            manageAIActions();
            this.aiDispatcher.startDispatching();
        }
    }

    /**
     * @return the current number of played rounds
     */
    public int getCurrentRound() {
        return this.currentRound;
    }

    /**
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    /**
     * This method does switch automatically to the next player and handles
     * all the GameStates.
     *
     * @return the player which is currently playing
     */
    public Player nextPlayer() {
        //look at next player
        Player p = players.peek();

        //Block next action if player has still reinforcements
        if( currentGameState == GameState.REINFORCE_UNITS &&
            p.getReinforcements() > 0  && !(p instanceof AI) ) return p;
        else //if not choose next player
            p = players.next();

        //if we are at the beginning of the list we do want to handle
        //some stuff like GameState switching
        if( this.players.isAtBeginning() )
            if (this.currentGameState == GameState.REINFORCE_UNITS) {
                this.currentRound++;
            } else if(this.currentGameState == GameState.ATTACK_OR_MOVE_UNIT) {
                setNewRoundDefaults();
                calculateReinforcements();
                currentGameState = GameState.REINFORCE_UNITS;
            }

        //determine if all players have already set the rreinforcements
        if( currentGameState != GameState.SET_UNIT )
            if( players.stream().filter(p1 -> p1.getReinforcements()>0).count() == 0) {
                currentGameState = GameState.ATTACK_OR_MOVE_UNIT;
            }

        //if the player is a AI start dispatching their actions
        if( p instanceof PlayerAI ) {
            manageAIActions();
            this.aiDispatcher.startDispatching();
            this.lastAction = null;
        }

        //return current player
        return this.getCurrentPlayer();
    }

    /**
     * This Method calls for each Player the #{Player.calculateReinforcements}
     * Method so every Player gets their reinforcements
     */
    private void calculateReinforcements() {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).calculateReinforcements();
        }
    }

    /**
     * This Method calls for each Player the #{Player.setNewRoundDefaults}
     * Method and resets therefore all selections of the players
     */
    private void setNewRoundDefaults()
    {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setNewRoundDefaults();
        }
    }

    /**
     * Do a certain action which depends on the internal state of the Player, the GameState and the
     * targetTerritory
     *
     * @param targetTerritory the target Territory
     */
    private void doActionOn(Territory targetTerritory)
    {
        //get current player
        Player p = this.getCurrentPlayer();


        //Switch GameState
        switch (this.currentGameState)
        {
            //In Set Unit we want to set the troups to the territory
            case SET_UNIT:
                if (targetTerritory.getOwner() == null) //But only if we can
                {
                    //inform player about the action
                    this.lastAction = p.getName() + " conquered " + targetTerritory.getName();
                    //now it's the players territory
                    this.getCurrentPlayer().conquerTerritory(targetTerritory,1);

                    //If there are no free Territories we switch to the next GameState
                    if (!this.gameMap.hasFreeTerrotories()) {
                        this.calculateReinforcements();
                        this.currentGameState = GameState.REINFORCE_UNITS;

                        //jump to the first player
                        this.players.reset(); //reset to player 0
                        this.players.prev(); //go one back, cuz next() is called
                    }

                    //If the Player is a AI we have to switch to the next one, that's what the AI is excepting
                    if( !(p instanceof AI) ) this.nextPlayer();
                }
                break;

            //In here we want to increase the army count of the players territory
            case REINFORCE_UNITS:
                //inform the player about the action
                this.lastAction = p.getName() + " reinforces " + targetTerritory.getName();

                //Check if the player can perform this action
                if (targetTerritory.getOwner().equals(p))
                    if (p.reinforcementPossible())
                        targetTerritory.increaseArmy(1);

                //If there are no reinforcements left -> and the AI has set all, we have to switch to the next player
                if (p.getReinforcements()==0) {
                    if( !(p instanceof AI) ) this.nextPlayer();
                }
                break;

            //Here we allow the Player to attack any Territory or move his units somewhere
            case ATTACK_OR_MOVE_UNIT:
                //Player or AI must have selected a territory
                if( p.getCurrentActiveTerritory() != null) {
                    if (p.getCurrentActiveTerritory().getNeighbours().contains(targetTerritory)) {

                        //Inform the Player about the action
                        if( p.getCurrentActiveTerritory().getOwner().equals(targetTerritory.getOwner()) && p.getMoveCount()==0 )
                            lastAction = p.getName() + " moves units to " + targetTerritory.getName();
                        else //if (p.getAttackAvailable())
                            lastAction = p.getName() + " attacks " + targetTerritory.getName();

                        //Let the Player do his action
                        p.attackOrMove(targetTerritory);
                    }

                    //reset the selected Territory
                    p.setCurrentActiveTerritory(null);
                } else {
                    //Otherwise if the territory is owned by the player it is selected
                    if( targetTerritory.getOwner().equals(p) ) {
                        p.setCurrentActiveTerritory(targetTerritory);
                    }
                }
                break;
        }
    }

    /**
     * This Methodes does check if a Player has Won or Lost.
     * All Players which can not do anything (are dead) are removed from the List
     *
     * @return null if the Game still has more than one Player alive, othweise the lst player staning alive
     */
    public Player checkWinLose() {
        //Check if it's not the first Gamestate
        if (players==null||(currentGameState!=GameState.ATTACK_OR_MOVE_UNIT&&currentGameState!=GameState.REINFORCE_UNITS))
            return null;

        //Search & remove all dead Players
        for (int i = 0; i < players.size(); i++) {
            if (!players.get(i).isAlive()) {
               this.lastAction = "Player "+ players.get(i).getName()+" is defeated.";
                players.remove(i); // remove player - since he has already lost
            }
        }

        //If there is only one Player left, this one has won
        if (players.size()==1) {
           this.lastAction = "Player "+players.get(0).getName()+" wins!";

            return players.get(0);
        }

        //still more than one player alive
        return null;
    }

    /**
     * @return if true only one player is alive
     */
    public boolean isOnePlayerLeft() {
        return this.players.size() == 1;
    }

    /**
     * This Method does Handle the AI Action Requests and queues them
     * into the AI Dispatcher
     */
    private void manageAIActions()
    {
        //If not AI we break free from here
        if (!(this.getCurrentPlayer() instanceof PlayerAI)) return;
        PlayerAI playerAI = (PlayerAI) this.getCurrentPlayer();

        //Switch the GameState -> AI does not know what to do
        switch(this.currentGameState) {
            //Asks the AI to set a Unit somewhere & conquer a Territory
            case SET_UNIT: doActionOn(playerAI.setUnitAction()); break;

            //Ask the AI on which Territory it does wnat to reinforce
            case REINFORCE_UNITS:
                //is dead already:
                if( !playerAI.isAlive() ) this.nextPlayer();

                //Get reinforcements from the AI
                List<Territory> reinforcements = playerAI.reinforceUnitsAction();
                reinforcements.forEach(this::doActionOn);

                //if can't set Player as fewer than 3 Territories
                if( reinforcements.isEmpty() ) this.nextPlayer();
                break;

            //Ask the AI for his Attack/Movement actions, these are queued
            case ATTACK_OR_MOVE_UNIT:
                this.aiDispatcher.queueActions(playerAI.moveOrAttackAction());
                break;
        }

    }

    /**
     * Do a certain action which depends on the internal state of the Player, the GameState and the
     * targetTerritory.
     * This method does call #{doActionOn} with the targetTerritory
     *
     * @param targetTerritory the target Territory
     */
    public void manageActions(Territory targetTerritory)
    {
        doActionOn(targetTerritory);
    }

    /**
     * @return the current gameState
     */
    public GameState getCurrentGameState() {
        return this.currentGameState;
    }

    /**
     * @return the last action from a AI or a Player
     */
    public String getLastAction() {
        return (getCurrentPlayer() instanceof  AI)?this.aiDispatcher.getNextAction():
               (this.lastAction!=null)?this.lastAction:this.aiDispatcher.getNextAction();
    }
}
