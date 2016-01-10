package pk.risiko.ui.screens;

import pk.risiko.ui.Drawable;
import pk.risiko.ui.listener.MouseEventListener;

import java.awt.event.KeyAdapter;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public interface GameScreen extends Drawable {

    MouseEventListener getMouseAdapter();
    KeyAdapter getKeyAdapter();

}
