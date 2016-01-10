package pk.risiko.ui;

import java.awt.Graphics;

/**
 * All Objects / Classes which can be drawn into a Grahpics buffer
 * can be represented by this class
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public interface Drawable {
    void paint(Graphics g);
}
