package pk.risiko.ui;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The window painter does force a repaint to the given game window
 * according to the set fps in the constructor
 *
 * @author Raphael
 * @version 14.01.2016
 */
public class WindowPainter extends Timer {

    private final GameWindow window;
    private final TimerTask job = new TimerTask() {
        @Override
        public void run() {
            if( EventQueue.isDispatchThread() ) WindowPainter.this.window.updateGraphics();
            else EventQueue.invokeLater(WindowPainter.this.window::updateGraphics);
        }
    };

    public WindowPainter(GameWindow window,int fps) {
        this.window = window;
        this.scheduleAtFixedRate(this.job,1000L/(long)fps,1000L/(long)fps);
    }

}
