package pk.risiko.util;

import pk.risiko.pojo.TaskFinishedListener;

/**
 * Created by
 *
 * @author Raphael
 * @version 28.01.2016
 */
public class AsyncRoundListener implements TaskFinishedListener {

    private RoundManager rm;
    private AsyncAIActionDispatcher aiActionDispatcher;

    @Override
    public void taskFinished() {
        //TODO: is won do not do that -> may crash something (also stop aiActionDispatcher)
        this.rm.nextPlayer();
    }

    public void setRm(RoundManager rm) {
        this.rm = rm;
    }

    public void setAiActionDispatcher(AsyncAIActionDispatcher aiActionDispatcher) {
        this.aiActionDispatcher = aiActionDispatcher;
    }
}
