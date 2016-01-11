package pk.risiko.ui.elements;

import pk.risiko.pojo.MouseState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.Interactable;
import pk.risiko.ui.listener.MouseEventListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public abstract class UIElement implements Drawable, Interactable, MouseEventListener {

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
    public boolean mouseMoved(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        if( inMe && this.mouseState == MouseState.NORMAL || this.mouseState == MouseState.CLICKED )  {
            this.setMouseState(MouseState.HOVER);
            return true;
        } else if( !inMe && this.mouseState == MouseState.HOVER ) {
            this.setMouseState(MouseState.NORMAL);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        final boolean inMe = this.isMouseIn(e.getX(),e.getY());

        if( inMe ) { this.mouseState = MouseState.CLICKED; return this.mouseClicked(); }
        else if( this.mouseState == MouseState.CLICKED ) { this.mouseState = MouseState.NORMAL; return true; }

        return false;
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        return this.elementShape.contains(x,y);
    }

    @Override
    public boolean mouseClicked() {
        return true;
    }
}
