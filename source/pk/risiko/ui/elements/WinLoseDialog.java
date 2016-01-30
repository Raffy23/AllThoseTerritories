package pk.risiko.ui.elements;

import pk.risiko.pojo.Player;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.screens.UserInterface;
import pk.risiko.util.FontLoader;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by Wilma on 28.01.2016.
 */
public class WinLoseDialog extends UIElement {

    /** The width of the dialog in pixel **/
    private static final int DIALOG_WIDTH  = DefaultDesigns.DEFAULT_WIDTH;
    /** The height of the dialog in pixel **/
    private static final int DIALOG_HEIGHT = DefaultDesigns.DEFAULT_HEIGHT;

    private static final int BUTTON_WIDTH = DefaultDesigns.BUTTON_WIDTH;
    private static final int BUTTON_HEIGHT = DefaultDesigns.BUTTON_HEIGHT;


    /** a status indicator which is true every time the menu is opend **/
    private boolean active = false;

    private final Font headlineFont;

    private final GameButton exitToMenuBtn;

    private Player winner;


    /**
     * For the creation of the component the shape must be known
     *
     * @see GameWindow
     */
    public WinLoseDialog() {
        super(new Rectangle(GameWindow.WINDOW_WIDTH /2-DIALOG_WIDTH/2
                ,GameWindow.WINDOW_HEIGHT /2-DIALOG_HEIGHT/2
                ,DIALOG_WIDTH
                ,DIALOG_HEIGHT));


        this.headlineFont = FontLoader.loadFont(DefaultDesigns.HEADLINE_FONT,DefaultDesigns.HEADLINE_FONT_SIZE);

        final int dialogX = this.getElementShape().getBounds().x;
        final int dialogY = this.getElementShape().getBounds().y;

        this.exitToMenuBtn = new GameButton(new Rectangle2D.Double(dialogX + DIALOG_WIDTH/2- BUTTON_WIDTH /2
                ,dialogY+DefaultDesigns.HEADLINE_FONT_SIZE*2+15 + (DIALOG_HEIGHT/5)
                ,BUTTON_WIDTH
                ,BUTTON_HEIGHT)
                ,"Main Menu");
    }


    @Override
    public void paint(Graphics2D g) {
        final Font oldFont = g.getFont();

        g.setColor(DefaultDesigns.LIGHT_BACKGROUND_COLOR);
        g.fillRect(0, UserInterface.BAR_HEIGHT,GameWindow.WINDOW_WIDTH,GameWindow.WINDOW_HEIGHT);

        g.setColor(DefaultDesigns.PANE_COLOR);
        g.fill(this.getElementShape());

        g.setFont(this.headlineFont);
        g.setColor(DefaultDesigns.TEXT_COLOR);

        final int fontHeight = g.getFontMetrics().getHeight()/2;
        final int fontWidth = g.getFontMetrics().stringWidth("Player \""+winner.getName()+"\" wins!");

        if (winner!=null)
            g.drawString("Player \"" + winner.getName()+ "\" wins!"
                ,(float)this.getElementShape().getBounds2D().getCenterX()-fontWidth/2
                ,(float)this.getElementShape().getBounds2D().getY() + fontHeight + 15);
        g.setFont(oldFont);

        this.exitToMenuBtn.paint(g);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if( !this.active ) return;

        this.exitToMenuBtn.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( !this.active ) return;

        this.exitToMenuBtn.mouseClicked(e);
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
    public void show(Player p) {

        this.active = true;
        this.winner=p;
    }

    /**
     * Hides the Menu and disables all the Event listeners
     */
    public void hide() {
        this.active = false;
    }

    public void setExitToMenuListener(MouseClickedListener l) {
        this.exitToMenuBtn.setListener(l);
    }
}
