package pk.risiko.ui.listener;

import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class MouseHandler implements MouseEventListener {

    private List<MouseEventListener> mice = new LinkedList<>();

    public void addMouseEventListener(MouseEventListener m) {
        assert m != null : "MouseAdapter is NULL!";
        this.mice.add(m);
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        for(MouseEventListener l:this.mice)
           if( l.mouseMoved(e)) {
               return true;
           }

        return false;
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        for(MouseEventListener l:this.mice)
            if( l.mouseClicked(e) ) {
                return true;
            }

        return false;
    }
}
