package pk.risiko.util;

import pk.risiko.pojo.TaskFinishedListener;
import pk.risiko.ui.screens.GamePanel;

/**
 * This class does dispatch the Finished Event from the
 * AsyncAIDispatcher and acts to it according the the
 * RoundManger and GamePanel
 *
 * @author Raphael
 * @version 28.01.2016
 * @see RoundManager
 * @see GamePanel
 */
public class AsyncRoundListener implements TaskFinishedListener {

    private RoundManager rm;
    //private AsyncAIActionDispatcher aiActionDispatcher;
    private GamePanel panel;

    public AsyncRoundListener(GamePanel p) {
        this.panel = p;
    }

    @Override
    public void taskFinished() {
        //wait while game is paused
        while( panel.isGamePaused() )
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        //if there is only player left exit computing
        if( this.rm.isOnePlayerLeft() ) Thread.currentThread().interrupt();
        else this.rm.nextPlayer();
    }

    public void setRm(RoundManager rm) {
        this.rm = rm;
    }

    /*
    public void setAiActionDispatcher(AsyncAIActionDispatcher aiActionDispatcher) {
        this.aiActionDispatcher = aiActionDispatcher;
    }
    */
}
