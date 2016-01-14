package pk.risiko.ui.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This calls represents the Graphical Menu in the game
 *
 * @author Raphael
 * @version 14.01.2016
 */
public class InGameMenu extends UIElement {

    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);

    private static final int MENU_WIDTH  = 200;
    private static final int MENU_HEIGHT = 200;

    private boolean active = false;

    public InGameMenu(int windowWidth, int topBarHeight) {
        super(new Rectangle(windowWidth-MENU_WIDTH,topBarHeight,windowWidth,MENU_HEIGHT));
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

    public void show() {
        this.active = true;
    }

    public void hide() {
        this.active = false;
    }
}
