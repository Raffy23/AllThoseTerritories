package pk.risiko.ui.screens;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.Interactable;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.listener.MouseEventListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Class handles all the Interaction with non MapStuff in the Game
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterface implements Drawable, MouseClickedListener, MouseEventListener {

    private static final Color UO_COLOR = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color textColor = new Color(255,255,255);

    private final GamePanel master;

    private int width,height;
    private GameButton next,menu;

    public UserInterface(GamePanel masterPanel,int width,int height) {
        this.master = masterPanel;
        this.width = width;
        this.height = height;

        this.next = new GameButton(new Ellipse2D.Double(width-75,height-100,65,65),">>");
        this.next.setFontSize(24);
        this.menu = new GameButton(new Rectangle2D.Double(width-75,2,65,20),"MENU");

        this.next.setListener(this);
        this.menu.setListener(this);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(UO_COLOR);
        g2d.fillRect(0,0,width,25);
        g.setColor(Color.WHITE);
        g2d.drawLine(0,26,width,26);

        this.menu.paint(g);
        this.next.paint(g);

        g2d.setColor(textColor);
        g2d.drawString("Player: " + this.master.getCurrentPlayer().getName() +
                       "  Round: " + this.master.getRounds(),5,17);
    }

    @Override
    public boolean mouseclicked(Interactable what) {
        if( what.equals(this.next) ) { this.master.changeState(GameState.NEXT_ROUND); return true;}
        else if( what.equals(this.menu) ) { this.master.changeState(GameState.SHOW_MENU); return true; }

        return false;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        return this.menu.mouseMoved(e) || this.next.mouseMoved(e);
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        return this.menu.mouseClicked(e) || this.next.mouseClicked(e);
    }
}
