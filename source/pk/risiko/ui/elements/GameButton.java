package pk.risiko.ui.elements;

import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;

/**
 * This Element is simply a Button which can be drawn to the Screen,
 * it is a simple alternative to the JButton (does not have LayoutManger
 * the tight swing bindings)
 *
 * @author Raphael
 * @version 11.01.2016
 */
public class GameButton extends UIElement {
    /** The default color of the of the Button which is kind of black trasparent **/
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    /** The default border color is white but slightly transparent **/
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    /** Default Text color which should be solid white **/
    private static final Color DEFAULT_TEXT_COLOR = new Color(255, 255, 255);

    /** The actual backgroud color of the Button **/
    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    /** The actual border color of the Button **/
    private Color borderColor = DEFAULT_BORDER_COLOR;
    /** The actual text color of the Button **/
    private Color textColor = DEFAULT_TEXT_COLOR;

    /** The Mouse clicked listener which is registered with the button **/
    private MouseClickedListener listener;

    /** The text of the Button which is display in the middle of the area **/
    private String text;
    /** The size of the font of the button text **/
    private int fontSize = 12;

    /**
     * To construct a game Button a shape must be give (it can have any closed shape)
     *
     * @param shape a closed shape which represents the button area
     * @param text the text which is displayed in the middle of the button area
     */
    public GameButton(Shape shape,String text) {
        super(shape);
        this.text = text;
    }

    @Override
    public void paint(Graphics2D g2d) {
        g2d.setColor(backgroundColor);
        g2d.fill(this.getElementShape());

        g2d.setColor(borderColor);
        g2d.draw(this.getElementShape());

        final Font oldFont = g2d.getFont();
        g2d.setFont(oldFont.deriveFont((float)this.fontSize));

        g2d.setColor(textColor);
        final int fontHeight = g2d.getFontMetrics().getHeight();
        final int fontWidth  = g2d.getFontMetrics().stringWidth(this.text);

        g2d.drawString(this.text
                      ,(float)this.getElementShape().getBounds2D().getCenterX()-fontWidth/2
                      ,(float)this.getElementShape().getBounds2D().getCenterY()+fontHeight/3);

        g2d.setFont(oldFont);
    }

    /**
     * @param fontSize new font size for the text
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return the Button Listener which does get triggered if the user does click in the area
     * @see MouseClickedListener
     */
    public MouseClickedListener getListener() {
        return listener;
    }

    /**
     * @param listener a MouseClickedListener which should listen to the button events (click)
     * @see MouseClickedListener
     */
    public void setListener(MouseClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked() {
        if( this.listener!=null ) //just check if there is a listener present
            this.listener.mouseClicked(this);
    }

    /**
     * @param backgroundColor the new Background color of the Button area
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @param borderColor a new Color of the Border (Outline)
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @param textColor a new color for the Text
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * @param text a String wich is displayed in the middle of the button area
     */
    public void setText(String text) {
        this.text = text;
    }
}
