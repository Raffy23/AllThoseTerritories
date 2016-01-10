package pk.risiko.util;

import pk.risiko.ui.GameWindow;
import pk.risiko.ui.listener.MouseEventListener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class MouseEventTranslator implements MouseListener, MouseMotionListener {

    private final GameWindow window;
    private final MouseEventListener listener;

    public MouseEventTranslator(GameWindow window,MouseEventListener listener) {
        this.window = window;
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( this.listener.mouseClicked(e) ) this.window.updateGraphics();
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
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        if( this.listener.mouseMoved(e) ) this.window.updateGraphics();
    }
}
