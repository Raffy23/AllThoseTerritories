package pk.risiko.util;

import pk.risiko.pojo.GameScreenType;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.GameScreen;

import java.util.HashMap;
import java.util.Map;

/**
 * Ugly first implementation of a Simple Screen State Machine
 * It can show Menu und Game screen only (also update the window in a SWING compatible way)
 *
 * @author Raphael Ludwig
 * @version 08.01.2016
 */
public class GameScreenManager {

    /** GameScreenManager holds GameWindow and vice-versa! **/
    private GameWindow window;
    /** the active screen which is currently displayed is stored here **/
    private GameScreenType activeScreen;
    /** all screens are stored with their identifier **/
    private Map<GameScreenType,GameScreen> screens = new HashMap<>();


    public GameScreenManager(GameWindow window) {
        this.window = window;
    }

    /**
     * Adds a screen to the internal map
     * @param type the identifier of the screen
     * @param screen the screen itself
     */
    public void addScreen(GameScreenType type,GameScreen screen) {
        this.screens.put(type,screen);
    }

    /**
     * Shows a screen, if the screen does not exist the behavior is undefined
     * ( lot's of NullPointers )
     * @param type identifier of the screen
     */
    public void showScreen(GameScreenType type) {
        if( this.activeScreen != null ) { //On start there is no screen available!
            //this.window.unregisterKeyAdapter(this.getActiveScreen().getKeyAdapter());
            this.window.removeMouseEventListener();
        }
        this.activeScreen = type;
        this.getActiveScreen().shown();
        //this.window.registerKeyAdapter(this.getActiveScreen().getKeyAdapter());
        this.window.setSwingEventDispatcher(this.getActiveScreen().getMouseEventDispatcher());
    }

    /**
     * Displays the Screen which does have the identifier START_MENU_SCREEN
     * @see GameScreenType
     */
    public void showMenu() {
        this.showScreen(GameScreenType.START_MENU_SCREEN);
    }

    /**
     * Added the GamePanel to the List of the Screens and displays it
     * @param gamePanel the gamePanel which should be displayed
     */
    public void showGame(GamePanel gamePanel) {
        this.screens.put(GameScreenType.GAME_SCREEN,gamePanel);
        this.showScreen(GameScreenType.GAME_SCREEN);
    }

    /**
     * @return the active screen
     */
    public GameScreen getActiveScreen() {
        return this.screens.get(this.activeScreen);
    }

    /**
     * @param type identifier of the stored screen
     * @return the screen which is stored or null if it does not exist
     */
    public GameScreen getScreen(GameScreenType type) {
        return screens.get(type);
    }

}
