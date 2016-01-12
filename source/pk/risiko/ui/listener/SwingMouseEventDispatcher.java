package pk.risiko.ui.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class SwingMouseEventDispatcher implements MouseMotionListener, MouseListener {

    private List<MouseMotionListener> motionListeners = new LinkedList<>();
    private List<MouseListener> mouseListeners = new LinkedList<>();


    public void registerListener(EventListener el) {
        if( el instanceof MouseMotionListener ) this.motionListeners.add((MouseMotionListener) el);
        if( el instanceof MouseListener ) this.mouseListeners.add((MouseListener) el);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.motionListeners.stream().forEach(el -> el.mouseDragged(e));
    }

    @Override
    public void mouseMoved(MouseEvent e) {
       this.motionListeners.stream().forEach(el -> el.mouseMoved(e));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.mouseListeners.stream().forEach(el -> el.mouseClicked(e));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseListeners.stream().forEach(el -> el.mousePressed(e));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseListeners.stream().forEach(el -> el.mouseReleased(e));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.mouseListeners.stream().forEach(el -> el.mouseEntered(e));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseListeners.stream().forEach(el -> el.mouseExited(e));
    }
}
