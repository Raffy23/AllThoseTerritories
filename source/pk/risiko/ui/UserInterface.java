package pk.risiko.ui;

import pk.risiko.pojo.Drawable;
import pk.risiko.ui.listener.UserInterfaceMouseListener;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/**
 * Class handles all the Interaction with non MapStuff in the Game
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterface implements Drawable {

    private static final Color uiColor   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color textColor = new Color(255,255,255);

    private final GamePanel master;
    private final UserInterfaceMouseListener listener;

    private int width,height;
    private Shape next,menu;

    public UserInterface(GamePanel masterPanel,int width,int height) {
        this.master = masterPanel;
        this.listener = new UserInterfaceMouseListener(this,masterPanel);

        this.width = width;
        this.height = height;

        this.next = new Ellipse2D.Double(width-75,height-95,65,65);
        this.menu = new Rectangle2D.Double(width-75,2,65,20);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(uiColor);
        g2d.fillRect(0,0,width,25);
        g2d.fill(next);
        g2d.draw(menu);

        g2d.setColor(textColor);
        g2d.drawString("Player: " + this.master.getCurrentPlayer().getName() +
                       "  Round: " + this.master.getRounds(),5,17);

        g2d.setColor(textColor);
        g2d.drawString("MENU",(int)this.menu.getBounds().getX()+14,(int)this.menu.getBounds().getHeight()-3);
        g2d.drawString(">>",(int)this.next.getBounds().getCenterX(),(int)this.next.getBounds().getCenterY());
    }

    public boolean isInMenuButton(int x,int y) {
        return this.menu.contains(x,y);
    }

    public boolean isInNextButton(int x,int y) {
        return this.next.contains(x,y);
    }

    public UserInterfaceMouseListener getMouseListener() {
        return this.listener;
    }
}
