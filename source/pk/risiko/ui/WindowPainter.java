package pk.risiko.ui;

import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The window painter does force a repaint to the given game window
 * according to the set fps in the constructor
 *
 * @author Raphael Ludwig
 * @version 14.01.2016
 */
public class WindowPainter extends Timer {

    /** Stores the current instance of the GameWindow **/
    private final GameWindow window;
    /** This task is repeated ever few ms depending on the FPS **/
    private final TimerTask job = new TimerTask() {
        @Override
        public void run() {
            //repainting is done in the EventQueue we just say they should do something
            if( EventQueue.isDispatchThread() ) WindowPainter.this.window.updateGraphics();
            else EventQueue.invokeLater(WindowPainter.this.window::updateGraphics);
        }
    };

    /**
     * @param window GameWindow which should be repainted every few my depending on the FPS
     * @param fps the speed in which the GameWindow should be repainted
     */
    public WindowPainter(GameWindow window,int fps) {
        this.window = window;
        this.scheduleAtFixedRate(this.job,1000L/(long)fps,1000L/(long)fps);
    }

}
