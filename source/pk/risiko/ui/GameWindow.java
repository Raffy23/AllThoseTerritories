package pk.risiko.ui;

import javax.swing.*;
import java.awt.*;

/**
 * GameWindow represents the Game Window of the Game.
 * It is responsible to draw all changes which occur in
 * the models or other UI Elements.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameWindow extends JFrame {

    //TODO: Should be max of game window + ui
    private static final int WINDOW_SIZE_X = 600;
    private static final int WINDOW_SIZE_Y = 480;

    /**
     * Constructs a Window with a certain size.
     * (Size is fixed by the Game) <br/>
     * #setVisible(true); must be called explicitly to show the window!
     */
    public GameWindow() {
        super("Risiko");
        this.setSize(GameWindow.WINDOW_SIZE_X,GameWindow.WINDOW_SIZE_Y);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Debug Code:
        this.add(new JLabel("Risiko Spiel"));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); //If this line is omitted no controls are drawn
        //TODO: Draw GameMap (own object) as List of Polygons?
    }
}
