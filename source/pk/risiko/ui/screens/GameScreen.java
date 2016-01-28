package pk.risiko.ui.screens;

import pk.risiko.ui.Drawable;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;

/**
 * Created by:
 *
 * @author Raphael Ludwig
 * @version 10.01.2016
 */
public interface GameScreen extends Drawable {

    SwingMouseEventDispatcher getMouseEventDispatcher();
    void shown();

}
