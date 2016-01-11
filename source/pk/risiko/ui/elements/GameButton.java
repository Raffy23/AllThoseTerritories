package pk.risiko.ui.elements;

import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.*;

/** Created by:
 *
 * @author Raphael
 * @version 11.01.2016
 */
public class GameButton extends UIElement {
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final Color DEFAULT_BORDER_COLOR = new Color(1f,1f,1f,0.65f);
    private static final Color DEFAULT_TEXT_COLOR = new Color(255,255,255);

    private Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Color borderColor = DEFAULT_BORDER_COLOR;
    private Color textColor = DEFAULT_TEXT_COLOR;

    private MouseClickedListener listener;

    private String text;
    private int fontSize = 12;

    public GameButton(Shape shape,String text) {
        super(shape);
        this.text = text;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

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

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public MouseClickedListener getListener() {
        return listener;
    }

    public void setListener(MouseClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean mouseClicked() {
        return this.listener!=null && this.listener.mouseclicked(this);
    }
}
