package pk.risiko.util;

import pk.risiko.pojo.Drawable;
import pk.risiko.ui.GamePanel;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.MenuPanel;
import pk.risiko.ui.RootPane;

import java.awt.*;

/**
 * Ugly first implementation of a Simple Screen State Machine
 * It can show Menu und Game screen only (also update the window in a SWING compatible way)
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class GameStateMachine {

    private GameWindow window;
    private RootPane rootPane;
    private MenuPanel menuPanel;

    private GamePanel gamePanel;
    private Drawable activeScreen;

    public GameStateMachine(GameWindow window,MenuPanel menuPanel) {
        this.window = window;
        this.window.add(this.rootPane = new RootPane(this));
        this.menuPanel = menuPanel;
    }

    public void showMenu() {
        this.activeScreen = this.menuPanel;
        this.window.removeMouseListener(this.gamePanel.getMouseAdapter());
        this.window.removeMouseMotionListener(this.gamePanel.getMouseAdapter());

       this.repaintWindow();
    }

    public void showGame(GamePanel gamePanel) {
        this.activeScreen = this.gamePanel = gamePanel;
        this.window.addMouseListener(this.gamePanel.getMouseAdapter());
        this.window.addMouseMotionListener(this.gamePanel.getMouseAdapter());

        this.repaintWindow();
    }


    public void repaintWindow() {
        if( EventQueue.isDispatchThread() ) {
            doThreadUnsaveUpdate();
        } else {
            EventQueue.invokeLater(this::doThreadUnsaveUpdate);
        }
    }

    private void doThreadUnsaveUpdate() {
        this.rootPane.invalidate();
        this.window.invalidate();
        this.window.repaint();
    }

    public GameWindow getWindow() {
        return this.window;
    }

    public Drawable getActiveScreen() {
        return this.activeScreen;
    }
}
