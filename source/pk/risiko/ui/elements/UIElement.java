package pk.risiko.ui.elements;

import pk.risiko.pojo.MouseState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.MouseEventHandler;

import javax.swing.SwingUtilities;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents any Graphical Element which can be drawn to the
 * game screen. It does handle basic mouse actions and events. The class
 * which implements UiElement is responsible for drawing and reacting to
 * events!
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 22.01.2016
 */
public abstract class UIElement extends MouseAdapter implements Drawable, MouseEventHandler {

    /** The current State in which the component is **/
    private MouseState mouseState = MouseState.NORMAL;
    /** The shape of the Component **/
    private Shape elementShape;

    /**
     * For the creation of the component the shape must be known
     * @param shape a which does represent the component
     */
    public UIElement(Shape shape) {
        this.elementShape = shape;
    }

    /**
     * @return the current MouseState of the component
     * @see MouseState
     */
    public MouseState getMouseState() {
        return this.mouseState;
    }

    /**
     * @return the current shape of the component
     */
    public Shape getElementShape() {
        return this.elementShape;
    }

    /**
     * @param state a new MouseState which the component should have
     */
    public void setMouseState(MouseState state) {
        this.mouseState = state;
    }

    /**
     * This Method does change the current Shape and therefore can cause
     * trouble with events!
     * @param shape a new shape which does represent the component
     */
    protected void setShape(Shape shape) {
        this.elementShape = shape;
    }

    /**
     * This Method des set the internal MouseState given by the SwingEvent
     * @param e a MouseEvent from Swing
     * @see MouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        /* Check if the mouse is in the component and the component
         * has one of the following characteristics: Normal or Click (right/left)
         */
        if( inMe && ( this.mouseState == MouseState.NORMAL     ||
                      this.mouseState == MouseState.L_CLICKED  ||
                      this.mouseState == MouseState.R_CLICKED    ) ){
            //set it to hover:
            this.setMouseState(MouseState.HOVER);
        } else if( !inMe && this.mouseState == MouseState.HOVER ) {
            //set it to normal again
            this.setMouseState(MouseState.NORMAL);
        }
    }

    /**
     * This Method des set the internal MouseState given by the SwingEvent
     * @param e a MouseEvent from Swing
     * @see MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        //first check if the mouse is in the component
        if( inMe )
        {
            //now we do differ between right and left click
            if (SwingUtilities.isLeftMouseButton(e)) {
                this.mouseState = MouseState.L_CLICKED;
                this.mouseClicked();
            }
            else if (SwingUtilities.isRightMouseButton(e)) {
                this.mouseState = MouseState.R_CLICKED;
                this.mouseClicked();
            }

            //consume the click event
            e.consume();
        } //otherwise we do set the state to normal again
        else if( this.mouseState == MouseState.L_CLICKED||this.mouseState==MouseState.R_CLICKED)
        { this.mouseState = MouseState.NORMAL; }
    }

    /**
     * @param x x position of the pointer in the window
     * @param y y position of the pointer in the window
     * @return true if the pointer is in the Shape of the Component
     */
    @Override
    public boolean isMouseIn(int x, int y) {
        return this.elementShape.contains(x,y);
    }

}
