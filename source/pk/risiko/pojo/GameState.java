package pk.risiko.pojo;

/**
 * Type does specify all possible game states in the game
 *
 * @author Raphael Ludwig
 * @version 08.01.2016
 */
public enum GameState {
    /**
     * This is the first possible GameState,
     * in this state each player can set it's
     * units to any free territory
     */
    SET_UNIT,
    /**
     * This GameState should allow the user to set
     * newly created armies to any owned territory
     */
    REINFORCE_UNITS,
    /**
     * Here the User should be allowed to move his
     * units from one of his territories to another,
     * either to attack ot to strengthen the army there
     */
    ATTACK_OR_MOVE_UNIT,
    /**
     * This GameState is a Meta state and does indicate that
     * a Human Player wants to enter the next round and give
     * another player control over the world
     */
    NEXT_ROUND,
    /**
     * This GameState is a Meta state and does get triggered
     * if a Human Player requests to show the menu. The game
     * should be paused while the menu is opened
     */
    SHOW_MENU,
    /**
     * This GameState is a Meta state and does get triggered
     * after a Human Player closes the Menu. The game should be
     * un-paused at this moment.
     */
    HIDE_MENU,
    /**
     * This GameState is a Meta state and does get triggered
     * after a Player won the Game.
     */
    SHOW_WINLOSE,
    /**
     * This GameState is a Meta state and does get triggered
     * after a Human Player exits the running game, after
     * it was won by one of the players.
     */
    HIDE_WINLOSE
}
