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

    /** The default color which is used to draw the line **/
    private final static Color DEFAULT_COLOR = new Color(25, 25, 25);
    /** The default line style which is kind of dottet **/
    private final static Stroke DEFAULT_STROKE = new BasicStroke(2.0f
                                                                ,BasicStroke.CAP_SQUARE
                                                                ,BasicStroke.JOIN_MITER
                                                                ,4.0f
                                                                ,new float[] {4.0f}
                                                                ,0.0f);

    /** One of the Territories from which the line is drawn **/
    private final Territory a;
    /** One of the Territories from which the line is drawn **/
    private final Territory b;

    /**
     * To construnct a Graphical connection between two Territories these must have allready
     * set the Capital, otherwise the line does origin in the default value (which should
     * be 0,0!)
     * It does not mather if the Territory a is left of b or otherwise, it should get
     * handled correctly.
     * If the Territories are located at the Borders of the Map the connection will
     * wrap the line trough the border, therefore it does take the shortest path
     *
     * @see Territory
     * @param a one of the Territories
     * @param b another Territory
     */
    public Connection(Territory a,Territory b) {
        /* Need to call a helper Function for this rather
         * complicated task (wrapping the line through the
         * border)
         */
        super(Connection.createNewObjectShape(a,b));

        this.a = a;
        this.b = b;
    }

    /**
     * This Method does all the complicated Stuff which is needed allready in
     * the Constructor of the Object.
     *
     * @param a Territory form constructor
     * @param b Territory form constructor
     * @return the shortest line from a to b as polygon
     */
    private static Polygon createNewObjectShape(Territory a, Territory b) {
        //check if the direct distance is bigger than a theshold
        if( a.getCapital().getCoords().distance(b.getCapital().getCoords()) >= 720 ) {
            //get the most left and right territory
            Point left = a.getCapital().getCoords().x < b.getCapital().getCoords().x ?
                         a.getCapital().getCoords() : b.getCapital().getCoords();
            Point right = b.getCapital().getCoords().x > a.getCapital().getCoords().x ?
                          b.getCapital().getCoords() : a.getCapital().getCoords();

            //some helper points
            Point leftPartner = new Point(-5,right.y);
            Point rightPartner = new Point(GameWindow.WINDOW_WIDTH +5,left.y);

            //start drawing the line
            Polygon line = new Polygon();
            line.addPoint(left.x,left.y);
            line.addPoint(leftPartner.x,leftPartner.y);
            line.addPoint(-5,-5);
            line.addPoint(GameWindow.WINDOW_WIDTH +5,-5);
            line.addPoint(rightPartner.x,rightPartner.y);
            line.addPoint(right.x,right.y);

            //Backtracking (due the fact that the polygon is always closed)
            line.addPoint(rightPartner.x,rightPartner.y);
            line.addPoint(GameWindow.WINDOW_WIDTH +5,-5);
            line.addPoint(-5,-5);
            line.addPoint(leftPartner.x,leftPartner.y);

            return line;

        } else {
            //this is simple, do not need to wrap throug the borders
            return new Polygon(new int[]{a.getCapital().getCoords().x,b.getCapital().getCoords().x}
                              ,new int[]{a.getCapital().getCoords().y,b.getCapital().getCoords().y}
                              ,2);
        }
    }

    /**
     * @return a uniqu hashcode which does not change if a and b are swapped
     */
    @Override
    public int hashCode() {
        int aHash = a.hashCode();
        int bHash = b.hashCode();

        return aHash ^ bHash;
    }

    /**
     * This function does compare the Objects, it does take into account
     * that a and b can be swapped and returns true if thats the case
     *
     * @param obj should be an instance of connection otherwise its false
     * @return true if the Objects equals
     */
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

        //restore old stroke
        g.setStroke(backupDefaultStroke);
    }

    /**
     * This function does nothing, it is here just in case
     * something would happen to this line ...
     */
    @Override
    public void mouseClicked() { /* what do you expect to happen when clicking to this line? */ }
}
