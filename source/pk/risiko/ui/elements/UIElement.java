package pk.risiko.ui.elements;

import pk.risiko.pojo.MouseState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.Interactable;

import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public abstract class UIElement implements Drawable, Interactable, MouseMotionListener, MouseListener {

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
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public boolean isMouseIn(int x, int y) {
        return this.elementShape.contains(x,y);
    }

    @Override
    public void mouseClicked() { }
}
