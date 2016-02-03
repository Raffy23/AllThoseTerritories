package pk.risiko.util;

import pk.risiko.pojo.TaskFinishedListener;
import pk.risiko.ui.screens.GamePanel;

/**
 * Created by
 *
 * @author Raphael
 * @version 28.01.2016
 */
public class AsyncRoundListener implements TaskFinishedListener {

    private RoundManager rm;
    private AsyncAIActionDispatcher aiActionDispatcher;
    private GamePanel panel;

    public AsyncRoundListener(GamePanel p) {
        this.panel = p;
    }

    @Override
    public void taskFinished() {

        while( panel.isGamePaused() )
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }

        if( this.rm.isOnePlayerLeft() || panel.isGamePaused() ) Thread.currentThread().interrupt();
        else this.rm.nextPlayer();
    }

    public void setRm(RoundManager rm) {
        this.rm = rm;
    }

    public void setAiActionDispatcher(AsyncAIActionDispatcher aiActionDispatcher) {
        this.aiActionDispatcher = aiActionDispatcher;
    }
}
