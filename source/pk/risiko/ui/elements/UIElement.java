package pk.risiko.ui.elements;

import pk.risiko.pojo.MouseState;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.Interactable;
import pk.risiko.ui.listener.MouseEventListener;

import java.awt.event.MouseEvent;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public abstract class UIElement implements Drawable, Interactable, MouseEventListener {

    private MouseState mouseState = MouseState.NORMAL;

    public MouseState getMouseState() {
        return this.mouseState;
    }

    protected void setMouseState(MouseState state) {
        this.mouseState = state;
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

        if( inMe ) { this.mouseState = MouseState.CLICKED; return true; }
        else if( this.mouseState == MouseState.CLICKED ) { this.mouseState = MouseState.NORMAL; return true; }

        return false;
    }
}
