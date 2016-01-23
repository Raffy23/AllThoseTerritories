package pk.risiko.ui.elements;

import pk.risiko.ui.GameWindow;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * TODO: make menu
 * This calls represents the Graphical Menu in the game
 *
 * @author Raphael Ludwig
 * @version 14.01.2016
 */
public class InGameMenu extends UIElement {

    /** The default background color of the Menu **/
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    /** The default border color of the Menu **/
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    /** The default text color of the Menu **/
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);

    /** The width of the menu in pixel **/
    private static final int MENU_WIDTH  = 200;
    /** The height of the menu in pixel **/
    private static final int MENU_HEIGHT = 200;

    /** a status indicator which is true every time the menu is opend **/
    private boolean active = false;

    /**
     * Constructs a new Menu in the Game which is centered in the Window
     *
     * @see GameWindow
     */
    public InGameMenu() {
        super(new Rectangle(GameWindow.WINDOW_SIZE_WIDTH/2-MENU_WIDTH/2
                           ,GameWindow.WINDOW_SIZE_HEIGHT/2-MENU_HEIGHT/2
                           ,MENU_WIDTH
                           ,MENU_HEIGHT));
        //TODO: buttons
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(DEFAULT_BACKGROUND_COLOR);
        g.fill(this.getElementShape());

        //TODO: draw some Buttons
    }

    @Override
    public void mouseClicked() {
        /* TODO: do something to the buttons */
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Shows the Menu and enables all the Event listeners
     */
    public void show() {
        this.active = true;
    }

    /**
     * Hides the Menu and disables all the Event listeners
     */
    public void hide() {
        this.active = false;
    }
}
