package pk.risiko.ui.elements;

import pk.risiko.pojo.MouseState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.MouseEventHandler;

import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents any Graphical Element which can be drawn to the
 * game screen. It does handle basic mouse actions and events. The class
 * which implements UiElement is responsible for drawing and reacting to
 * events!
 *
 * @author Raphael
 * @version 10.01.2016
 */
public abstract class UIElement extends MouseAdapter implements Drawable, MouseEventHandler {

    private MouseState mouseState = MouseState.NORMAL;
    private Shape elementShape;

    public UIElement(Shape shape) {
        this.elementShape = shape;
    }

    public MouseState getMouseState() {
        return this.mouseState;
    }
    public Shape getElementShape() {
        return this.elementShape;
    }

    protected void setMouseState(MouseState state) {
        this.mouseState = state;
    }
    protected void setShape(Shape shape) {
        this.elementShape = shape;
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        if( inMe && this.mouseState == MouseState.NORMAL || this.mouseState == MouseState.CLICKED )  {
            this.setMouseState(MouseState.HOVER);
        } else if( !inMe && this.mouseState == MouseState.HOVER ) {
            this.setMouseState(MouseState.NORMAL);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        if( inMe ) { this.mouseState = MouseState.CLICKED; this.mouseClicked(); e.consume(); }
        else if( this.mouseState == MouseState.CLICKED ) { this.mouseState = MouseState.NORMAL; }
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        return this.elementShape.contains(x,y);
    }

}
