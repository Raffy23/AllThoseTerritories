package pk.risiko.ui;

import javax.swing.*;

/**
 * GameWindow represents the Game Window of the Game.
 * It is responsible to draw all changes which occur in
 * the models or other UI Elements.
 * <br/>
 * <b>TODO: draw stuff in other class JPanel because we can draw into the Taskbar ... lol </b>
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameWindow extends JFrame {

    //TODO: Should be max of game window + ui
    public static final int WINDOW_SIZE_X = 1250;
    public static final int WINDOW_SIZE_Y = 650;

    /**
     * Constructs a Window with a certain size.
     * (Size is fixed by the Game) <br/>
     * #setVisible(true); must be called explicitly to show the window!
     */
    public GameWindow() {
        super("AllThoseTerritories - Risiko");
        this.setSize(GameWindow.WINDOW_SIZE_X,GameWindow.WINDOW_SIZE_Y);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
    }


}
