package pk.risiko.ui.elements;

import pk.risiko.ui.GameWindow;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.screens.UserInterface;
import pk.risiko.util.FontLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * TODO: make menu
 * This calls represents the Graphical Menu in the game
 *
 * @author Raphael Ludwig
 * @version 14.01.2016
 */
public class InGameMenu extends UIElement {

    /** The default background color of the Menu **/
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.05f,0.05f,0.05f,0.25f);
    /** The default border color of the Menu **/
    private static final Color DEFAULT_PANE_COLOR = new Color(0.05f,0.05f,0.05f,0.85f);
    /** The default text color of the Menu **/
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);

    /** The width of the menu in pixel **/
    private static final int MENU_WIDTH  = 200;
    /** The height of the menu in pixel **/
    private static final int MENU_HEIGHT = 200;

    private static final int BUTTON_WIDTH = MENU_WIDTH-18;
    private static final int BUTTON_HEIGHT = 20;

    private static final String HEADLINE_FONT = "aniron/anirm___.ttf";
    /** The default font size of the headline **/
    private static final float HEADLINE_FONT_SIZE = 23f;

    /** a status indicator which is true every time the menu is opend **/
    private boolean active = false;

    private final Font headlineFont;

    private final GameButton exitToMenuBtn;
    private final GameButton saveGameBtn;
    private final GameButton backToGameBtn;

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


        this.headlineFont = FontLoader.loadFont(HEADLINE_FONT,HEADLINE_FONT_SIZE);

        final int menuX = this.getElementShape().getBounds().x;
        final int menuY = this.getElementShape().getBounds().y;

        this.exitToMenuBtn = new GameButton(new Rectangle2D.Double(menuX + MENU_WIDTH/2- BUTTON_WIDTH /2
                                                                  ,menuY + MENU_HEIGHT - (BUTTON_HEIGHT +15)
                                                                  ,BUTTON_WIDTH
                                                                  ,BUTTON_HEIGHT)
                                           ,"Exit to Menu");

        this.saveGameBtn = new GameButton(new Rectangle2D.Double(menuX + MENU_WIDTH/2- BUTTON_WIDTH /2
                                                                ,menuY + HEADLINE_FONT_SIZE*2 + 15 /* headline padding */
                                                                ,BUTTON_WIDTH
                                                                ,BUTTON_HEIGHT)
                                          ,"Save Game");

        this.backToGameBtn = new GameButton(new Rectangle2D.Double(menuX + MENU_WIDTH/2- BUTTON_WIDTH /2
                                                                  ,menuY + HEADLINE_FONT_SIZE*2 + BUTTON_HEIGHT + 15*2
                                                                  ,BUTTON_WIDTH
                                                                  ,BUTTON_HEIGHT)
                                            ,"Back to the Game");

        this.backToGameBtn.setListener((btn) -> this.hide());
    }

    @Override
    public void paint(Graphics2D g) {
        final Font oldFont = g.getFont();

        g.setColor(DEFAULT_BACKGROUND_COLOR);
        g.fillRect(0, UserInterface.BAR_HEIGHT,GameWindow.WINDOW_SIZE_WIDTH,GameWindow.WINDOW_SIZE_HEIGHT);

        g.setColor(DEFAULT_PANE_COLOR);
        g.fill(this.getElementShape());

        g.setFont(this.headlineFont);
        g.setColor(DEFAULT_TEXT_COLOR);

        final int fontHeight = g.getFontMetrics().getHeight()/2;
        final int fontWidth = g.getFontMetrics().stringWidth("MENU");
        g.drawString("MENU"
                ,(float)this.getElementShape().getBounds2D().getCenterX()-fontWidth/2
                ,(float)this.getElementShape().getBounds2D().getY() + fontHeight + 15);
        g.setFont(oldFont);

        this.exitToMenuBtn.paint(g);
        this.saveGameBtn.paint(g);
        this.backToGameBtn.paint(g);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if( !this.active ) return;

        this.exitToMenuBtn.mouseMoved(e);
        this.saveGameBtn.mouseMoved(e);
        this.backToGameBtn.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( !this.active ) return;

        this.exitToMenuBtn.mouseClicked(e);
        this.saveGameBtn.mouseClicked(e);
        this.backToGameBtn.mouseClicked(e);
    }

    @Override
    public void mouseClicked() {
        /* nothing to do ... just a panel */
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

    public void setExitGameListener(MouseClickedListener l) {
        this.exitToMenuBtn.setListener(l);
    }

    public void setSaveGameListener(MouseClickedListener l) {
        this.saveGameBtn.setListener(l);
    }
}
