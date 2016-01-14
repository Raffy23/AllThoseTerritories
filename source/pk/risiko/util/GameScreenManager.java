package pk.risiko.util;

import pk.risiko.pojo.GameScreenType;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.GameScreen;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Map;

/**
 * Ugly first implementation of a Simple Screen State Machine
 * It can show Menu und Game screen only (also update the window in a SWING compatible way)
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class GameScreenManager {

    private GameWindow window;
    private GameScreenType activeScreen;
    private Map<GameScreenType,GameScreen> screens = new HashMap<>();

    public GameScreenManager(GameWindow window) {
        this.window = window;
    }

    public void addScreen(GameScreenType type,GameScreen screen) {
        this.screens.put(type,screen);
    }

    public void showScreen(GameScreenType type) {
        if( this.activeScreen != null ) { //On start there is no screen available!
            this.window.unregisterKeyAdapter(this.getActiveScreen().getKeyAdapter());
            this.window.removeMouseEventListener();
        }
        this.activeScreen = type;
        this.window.registerKeyAdapter(this.getActiveScreen().getKeyAdapter());
        this.window.setSwingEventDispatcher(this.getActiveScreen().getMouseEventDispatcher());
        this.repaintWindow();
    }

    public void showMenu() {
        this.showScreen(GameScreenType.START_MENU_SCREEN);
    }

    public void showGame(GamePanel gamePanel) {
        this.screens.put(GameScreenType.GAME_SCREEN,gamePanel);
        this.showScreen(GameScreenType.GAME_SCREEN);
    }

    public void repaintWindow() {
        if( EventQueue.isDispatchThread() ) this.window.updateGraphics();
        else EventQueue.invokeLater(this.window::updateGraphics);
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public GameScreen getActiveScreen() {
        return this.screens.get(this.activeScreen);
    }

}
