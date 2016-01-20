package pk.risiko.ui.screens;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.Drawable;
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
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterface extends MouseAdapter implements Drawable, MouseClickedListener {

    private static final Color UO_COLOR = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color TEXT_COLOR = new Color(255,255,255);

    public static final int BAR_HEIGHT = 26;

    private final GamePanel master;

    private int width,height;
    private final GameButton nextButton, menuButton;
    private final InGameMenu menu;

    public UserInterface(GamePanel masterPanel,int width,int height) {
        this.master = masterPanel;
        this.width = width;
        this.height = height;

        this.nextButton = new GameButton(new Ellipse2D.Double(width-75,height-100,65,65),">>");
        this.nextButton.setFontSize(24);
        this.menuButton = new GameButton(new Rectangle2D.Double(width-75,2,65,20),"MENU");

        this.nextButton.setListener(this);
        this.menuButton.setListener(this);

        this.menu = new InGameMenu(width,BAR_HEIGHT);
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(UO_COLOR);
        g2d.fillRect(0,0,width,BAR_HEIGHT);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0,BAR_HEIGHT,width,BAR_HEIGHT);

        this.menuButton.paint(g2d);
        this.nextButton.paint(g2d);

        g2d.setColor(TEXT_COLOR);
        g2d.drawString("Player: " + this.master.getCurrentPlayer().getName() +
                       "  Round: " + this.master.getRounds() +
                       "     Available Reinforcements: " + this.master.getCurrentPlayer().getReinforcements() +
                       " Current GameState: " + this.master.getGameState().name(),5,17);

        if( this.menu.isActive() )
            this.menu.paint(g2d);
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
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.menuButton.mouseClicked(e);
        this.nextButton.mouseClicked(e);
    }
}
