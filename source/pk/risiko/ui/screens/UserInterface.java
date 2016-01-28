package pk.risiko.ui.screens;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.MouseEventHandler;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.elements.InGameMenu;
import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Class handles all the Interaction with non MapStuff in the Game
 *
 * @author Raphael Ludwig
 * @version 08.01.2016
 */
public class UserInterface extends MouseAdapter implements Drawable, MouseClickedListener {

    private static final Color BACKGROUND_COLOR = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color TEXT_COLOR = new Color(255,255,255);

    public static final int BAR_HEIGHT = 26;

    private final GamePanel master;

    private final GameButton nextButton, menuButton;
    private final InGameMenu menu;

    public UserInterface(GamePanel masterPanel) {
        this.master = masterPanel;

        this.nextButton = new GameButton(new Ellipse2D.Double(GameWindow.WINDOW_SIZE_WIDTH-75,GameWindow.WINDOW_SIZE_HEIGHT-100,65,65),">>");
        this.nextButton.setFontSize(24);
        this.menuButton = new GameButton(new Rectangle2D.Double(GameWindow.WINDOW_SIZE_WIDTH-75,2,65,20),"MENU");

        this.nextButton.setListener(this);
        this.menuButton.setListener(this);

        this.menu = new InGameMenu();
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0,0, GameWindow.WINDOW_SIZE_WIDTH,BAR_HEIGHT);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0,BAR_HEIGHT,GameWindow.WINDOW_SIZE_WIDTH,BAR_HEIGHT);

        this.menuButton.paint(g2d);
        this.nextButton.paint(g2d);

        final int headStatusLength = g2d.getFontMetrics().stringWidth("Player: ");
        g2d.fillRect(5,4,2,BAR_HEIGHT-8);
        g2d.drawString("Player: ",15,17);
        g2d.setColor(this.master.getCurrentPlayer().getColor());
        g2d.fillRect(headStatusLength+21,BAR_HEIGHT/2-6,11,11);
        g2d.setColor(TEXT_COLOR);
        g2d.drawRect(headStatusLength+21,BAR_HEIGHT/2-6,11,11);
        g2d.drawString(this.master.getCurrentPlayer().getName(),headStatusLength + 35,17);

        g2d.fillRect(145,4,2,BAR_HEIGHT-8);
        g2d.drawString("Round: " + this.master.getRounds() ,160,17);

        g2d.fillRect(225,4,2,BAR_HEIGHT-8);
        g2d.drawString("Reinforcements: " +
                       (this.master.getCurrentPlayer().getReinforcements()==0?
                               "none":
                               this.master.getCurrentPlayer().getReinforcements())
                       ,235,17);

        g2d.fillRect(365,4,2,BAR_HEIGHT-8);
        final int statusStringLength = g2d.getFontMetrics().stringWidth(this.getGameStateString());
        g2d.drawString(this.getGameStateString(),(375+545)/2-statusStringLength/2,17);
        g2d.fillRect(555,4,2,BAR_HEIGHT-8);
        g2d.drawString(this.master.getLastAction(),565,17);


        if( this.menu.isActive() )
            this.menu.paint(g2d);
    }

    private String getGameStateString() {
        switch( this.master.getGameState() ) {
            case ATTACK_OR_MOVE_UNIT: return "Attack or move Units";
            case REINFORCE_UNITS: return "Reinforce your units!";
            case SET_UNIT: return "Deploy your units to conquer";
        }

        return "<NONE>";
    }

    @Override
    public void mouseClicked(MouseEventHandler what) {
        if( what.equals(this.nextButton) ) { this.master.changeState(GameState.NEXT_ROUND); }
        else if( what.equals(this.menuButton) ) {
            if( !this.menu.isActive() ) { this.master.changeState(GameState.SHOW_MENU); this.menu.show(); }
            else { this.master.changeState(GameState.HIDE_MENU); this.menu.hide(); }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.menuButton.mouseMoved(e);
        this.nextButton.mouseMoved(e);
        this.menu.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.menuButton.mouseClicked(e);
        this.nextButton.mouseClicked(e);
        this.menu.mouseClicked(e);
    }

    public InGameMenu getMenu() {
        return this.menu;
    }
}
