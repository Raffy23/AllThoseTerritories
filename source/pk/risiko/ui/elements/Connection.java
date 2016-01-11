package pk.risiko.ui.elements;

import pk.risiko.pojo.Territory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class Connection extends UIElement {

    private final static Color DEFAULT_COLOR = new Color(25, 25, 25);
    private final static Stroke DEFAULT_STROKE = new BasicStroke(2.0f
                                                                ,BasicStroke.CAP_SQUARE
                                                                ,BasicStroke.JOIN_MITER
                                                                ,4.0f
                                                                ,new float[] {4.0f}
                                                                ,0.0f);

    private final Territory a;
    private final Territory b;

    public Connection(Territory a,Territory b) {
        super(new Line2D.Double(a.getElementShape().getBounds().getCenterX()
                               ,a.getElementShape().getBounds().getCenterY()
                               ,b.getElementShape().getBounds().getCenterX()
                               ,b.getElementShape().getBounds().getCenterY()));
        this.a = a;
        this.b = b;
    }

    @Override
    public int hashCode() {
        int aHash = a.hashCode();
        int bHash = b.hashCode();

        return aHash ^ bHash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Connection &&
              (a.equals(((Connection)obj).a) && b.equals(((Connection)obj).b) ||
               b.equals(((Connection)obj).a) && b.equals(((Connection)obj).a)    );
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        final Stroke backupDefaultStroke = g2d.getStroke();

        g2d.setColor(DEFAULT_COLOR);
        g2d.setStroke(DEFAULT_STROKE);

        g2d.draw(this.getElementShape());

        g2d.setStroke(backupDefaultStroke);
    }

    @Override
    public boolean mouseClicked() {
        return false;
    }
}
