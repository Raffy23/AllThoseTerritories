package pk.risiko.pojo;

/**
 * This Enumeration represents all types of different screens
 * which the GameScreenManager can handle
 *
 * @author Raphael Ludwig
 * @version 10.01.2016
 */
public enum GameScreenType {
    /**
     * The GameScreen, in which the game actually takes place
     */
    GAME_SCREEN,
    /**
     * This Screen represents the start menu which is displayed before and most times after
     * the game has ended
     */
    START_MENU_SCREEN,
    /**
     * This screen represents the menu that is shown if the user does want to start
     * a new game
     */
    NEW_GAME_SCREEN
}
