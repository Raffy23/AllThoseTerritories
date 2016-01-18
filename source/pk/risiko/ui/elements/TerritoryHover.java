package pk.risiko.ui.elements;

import pk.risiko.pojo.Territory;
import pk.risiko.util.FontLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class draws a little hover element above the hovered territory
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class TerritoryHover extends UIElement {
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);
    private static final String HEADLINE_FONT = "aniron/anirm___.ttf";
    private static final float HEADLINE_FONT_SIZE = 11f;

    private static final int SIZE_X = 165;
    private static final int SIZE_Y = 85;

    private final Territory territory;
    private final Font headlineFont;

    public TerritoryHover(Territory territory,int x,int y) {
        super(new Rectangle(x,y,SIZE_X,SIZE_Y));
        this.territory = territory;
        this.headlineFont = FontLoader.loadFont(HEADLINE_FONT,HEADLINE_FONT_SIZE);
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(DEFAULT_BACKGROUND_COLOR);
        g2d.fill(this.getElementShape());

        final Font oldFont = g2d.getFont();
        g2d.setFont(this.headlineFont);
        g2d.setColor(DEFAULT_TEXT_COLOR);
        final int fontWidth = g2d.getFontMetrics().stringWidth(this.territory.getName());
        final int fontHeight = g2d.getFontMetrics().getHeight()/2;
        float currentTextPosition = (float)this.getElementShape().getBounds2D().getY() + fontHeight + 5;


        g2d.drawString(this.territory.getName()
                ,(float)this.getElementShape().getBounds2D().getCenterX()-fontWidth/2
                ,currentTextPosition);

        g2d.setFont(oldFont);
        g2d.setColor(DEFAULT_BORDER_COLOR);
        g2d.drawLine(this.getElementShape().getBounds().x
                ,(int)(currentTextPosition+=fontHeight/2)
                ,this.getElementShape().getBounds().x+SIZE_X
                ,(int)(currentTextPosition));

        g2d.setColor(DEFAULT_TEXT_COLOR);

        g2d.drawString("Owend by: " + (this.territory.getOwner()!=null?this.territory.getOwner().getName():"none")
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+=fontHeight+5);


        g2d.drawString("Stationed Armies: " + String.valueOf(this.territory.getStationedArmies())
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+fontHeight+5);
    }

    public void moveTo(int x,int y) {
        this.setShape(new Rectangle(x,y,SIZE_X,SIZE_Y));
    }

    public Territory getTerritory() {
        return territory;
    }

    @Override
    public void mouseClicked() {
        /* do nothing ... btw this element can't be clicked */
    }
}
