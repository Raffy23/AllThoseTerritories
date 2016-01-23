package pk.risiko.ui.listener;

import pk.risiko.ui.MouseEventHandler;

/**
 * This class represents a simple MouseClicked Listener
 * which does only transport the Object to the Listener
 * which was clicked
 *
 * @author Raphael Ludwig
 */
public interface MouseClickedListener {
    /**
     * This Method does get called by the EventQueue after the mouse
     * clicked in the Object which is given as parameter
     *
     * @param what the origin of the Event
     */
    void mouseClicked(MouseEventHandler what);
}
