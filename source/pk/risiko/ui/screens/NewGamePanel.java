package pk.risiko.ui.screens;

import pk.risiko.ui.listener.SwingMouseEventDispatcher;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;

/**
 * Created by
 *
 * @author Raphael
 * @version 19.01.2016
 */
public class NewGamePanel implements GameScreen {

    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return null;
    }

    @Override
    public KeyAdapter getKeyAdapter() {
        return null;
    }

    @Override
    public void paint(Graphics2D g) {

    }
}
