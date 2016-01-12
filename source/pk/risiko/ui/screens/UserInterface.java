package pk.risiko.ui.screens;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.Interactable;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Class handles all the Interaction with non MapStuff in the Game
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterface implements Drawable, MouseMotionListener, MouseListener, MouseClickedListener {

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
    public void paint(Graphics2D g2d) {
        g2d.setColor(UO_COLOR);
        g2d.fillRect(0,0,width,25);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0,26,width,26);

        this.menu.paint(g2d);
        this.next.paint(g2d);

        g2d.setColor(textColor);
        g2d.drawString("Player: " + this.master.getCurrentPlayer().getName() +
                       "  Round: " + this.master.getRounds(),5,17);
    }

    @Override
    public void mouseclicked(Interactable what) {
        if( what.equals(this.next) ) { this.master.changeState(GameState.NEXT_ROUND); }
        else if( what.equals(this.menu) ) { this.master.changeState(GameState.SHOW_MENU); }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        this.menu.mouseMoved(e);
        this.next.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.menu.mouseClicked(e);
        this.next.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
