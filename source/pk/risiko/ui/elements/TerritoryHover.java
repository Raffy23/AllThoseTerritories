package pk.risiko.ui.elements;

import pk.risiko.pojo.Territory;
import pk.risiko.ui.GameWindow;
import pk.risiko.util.FontLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents the Hover element which is
 * shown if the mouse does hover a Territory on the
 * GameMapUI.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class TerritoryHover extends UIElement {
    /** The default background color of the Hover Element **/
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    /** The default border color of the Hover Element **/
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    /** The default text color of the Hover Element **/
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);
    /** The default headline font of the Hover Element **/
    private static final String HEADLINE_FONT = "aniron/anirm___.ttf";
    /** The default font size of the headline **/
    private static final float HEADLINE_FONT_SIZE = 11f;

    /** The min with of the Element **/
    private static final int MIN_ELEMENT_WIDTH = 165;
    /** The min height of the Element **/
    private static final int MIN_ELEMENT_HEIGHT = 105;

    /** the territory which is currently hovered therefore has its data displayed **/
    private final Territory territory;
    /** the actual headline font **/
    private final Font headlineFont;

    /**
     * @param territory the hovered territory
     * @param x x position on the screen where is should be displayed
     * @param y y position on the screen where it should be displayed
     */
    public TerritoryHover(Territory territory,int x,int y) {
        super(new Rectangle(x,y, MIN_ELEMENT_WIDTH, MIN_ELEMENT_HEIGHT));
        this.territory = territory;
        this.headlineFont = FontLoader.loadFont(HEADLINE_FONT,HEADLINE_FONT_SIZE);

        //correct myself if wrong size or out of window
        this.moveTo(x,y);
    }

    private void resizeIfNeeded(int width) {
        Rectangle rect = this.getElementShape().getBounds();

        if( width > rect.getWidth() )
            this.setShape(new Rectangle(rect.x,rect.y,width+15,rect.height));
    }

    @Override
    public void paint(Graphics2D g2d) {
        //backup font & change to the headline font
        final Font oldFont = g2d.getFont();
        g2d.setFont(this.headlineFont);


        //get all the Font related sizes
        final int fontHeight = g2d.getFontMetrics().getHeight()/2;
        final int fontWidth = g2d.getFontMetrics().stringWidth(this.territory.getName());
        this.resizeIfNeeded(fontWidth);

        //fill the area of the Hover with the Background color:
        g2d.setColor(DEFAULT_BACKGROUND_COLOR);
        g2d.fill(this.getElementShape());

        //set to correct color:
        g2d.setColor(DEFAULT_TEXT_COLOR);

        //calculate text position
        float currentTextPosition = (float)this.getElementShape().getBounds2D().getY() + fontHeight + 5;

        //Draw the Headline
        g2d.drawString(this.territory.getName()
                ,(float)this.getElementShape().getBounds2D().getCenterX()-fontWidth/2
                ,currentTextPosition);

        //load the old font
        g2d.setFont(oldFont);

        //draw the line between the headline and the body
        g2d.setColor(DEFAULT_BORDER_COLOR);
        g2d.drawLine(this.getElementShape().getBounds().x
                ,(int)(currentTextPosition+=fontHeight/2)
                ,this.getElementShape().getBounds().x+ this.getElementShape().getBounds().width
                ,(int)(currentTextPosition));

        //draw the rest of the Body:
        g2d.setColor(DEFAULT_TEXT_COLOR);
        g2d.drawString("Owend by: " + (this.territory.getOwner()!=null?this.territory.getOwner().getName():"none")
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+=fontHeight+5);

        g2d.drawString("Stationed Armies: " + String.valueOf(this.territory.getStationedArmies())
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+=fontHeight+5);

        g2d.drawString("Continent: " + this.territory.getContinent().getName()
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+=fontHeight+5);

        g2d.drawString("Continent Bonus: " + String.valueOf(this.territory.getContinent().getValue())
                ,(float)this.getElementShape().getBounds2D().getX() + 5
                ,currentTextPosition+fontHeight+5);
    }

    /**
     * This Method moves the current Position of the Hover to the given
     * coordinates x and y on the screen
     * @param x x coordinate on the screen
     * @param y y coordinate on the screen
     */
    public void moveTo(int x,int y) {
        if( x + this.getElementShape().getBounds().width  > GameWindow.WINDOW_SIZE_WIDTH-15 )
            x = x - this.getElementShape().getBounds().width - 10;
        if( y + this.getElementShape().getBounds().height  > GameWindow.WINDOW_SIZE_HEIGHT-15 )
            y = y - this.getElementShape().getBounds().height - 10;

        this.setShape(new Rectangle(x,y, this.getElementShape().getBounds().width , MIN_ELEMENT_HEIGHT));
    }

    /**
     * @return the currently set territory
     */
    public Territory getTerritory() {
        return territory;
    }

    /**
     * does nothing ...
     */
    @Override
    public void mouseClicked() {
        /* do nothing ... btw this element can't be clicked because it should be always out of reach ^^ */
    }
}
