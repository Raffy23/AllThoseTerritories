package pk.risiko.ui;

import java.awt.Graphics2D;

/**
 * All Objects / Classes which can be drawn into a Graphics buffer
 * can be represented by this class
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public interface Drawable {
    /**
     * This Method does paint the main parts of the Object into the given
     * Graphics2D object.
     *
     * @param g a Graphics object which is big enough that the Element can be drawn in it
     */
    void paint(Graphics2D g);
}
