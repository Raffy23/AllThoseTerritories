package pk.risiko.ui.elements;

import pk.risiko.pojo.Territory;
import pk.risiko.ui.GameWindow;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;

/**
 * This class graphically represents the connection line between
 * two territories
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
        super(Connection.createNewObjectShape(a,b));

        this.a = a;
        this.b = b;
    }

    private static Polygon createNewObjectShape(Territory a, Territory b) {
        if( a.getCapital().getCoords().distance(b.getCapital().getCoords()) >= 720 ) {
            Point left = a.getCapital().getCoords().x < b.getCapital().getCoords().x ?
                         a.getCapital().getCoords() : b.getCapital().getCoords();
            Point right = b.getCapital().getCoords().x > a.getCapital().getCoords().x ?
                          b.getCapital().getCoords() : a.getCapital().getCoords();

            Point leftPartner = new Point(-5,right.y);
            Point rightPartner = new Point(GameWindow.WINDOW_SIZE_WIDTH +5,left.y);

            Polygon line = new Polygon();
            line.addPoint(left.x,left.y);
            line.addPoint(leftPartner.x,leftPartner.y);
            line.addPoint(-5,-5);
            line.addPoint(GameWindow.WINDOW_SIZE_WIDTH +5,-5);
            line.addPoint(rightPartner.x,rightPartner.y);
            line.addPoint(right.x,right.y);

            //Backtracking
            line.addPoint(rightPartner.x,rightPartner.y);
            line.addPoint(GameWindow.WINDOW_SIZE_WIDTH +5,-5);
            line.addPoint(-5,-5);
            line.addPoint(leftPartner.x,leftPartner.y);


            return line;

        } else {
            return new Polygon(new int[]{a.getCapital().getCoords().x,b.getCapital().getCoords().x}
                              ,new int[]{a.getCapital().getCoords().y,b.getCapital().getCoords().y}
                              ,2);
        }
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
    public void paint(Graphics2D g) {
        final Stroke backupDefaultStroke = g.getStroke();

        g.setColor(DEFAULT_COLOR);
        g.setStroke(DEFAULT_STROKE);

        g.draw(this.getElementShape());

        g.setStroke(backupDefaultStroke);
    }

    @Override
    public void mouseClicked() { /* what do you expect to happen when clicking to this line? */ }
}
