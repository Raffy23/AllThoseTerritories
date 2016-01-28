package pk.risiko.util;

import pk.risiko.pojo.AI;
import pk.risiko.pojo.MouseState;
import pk.risiko.pojo.TaskFinishedListener;
import pk.risiko.pojo.Territory;
import pk.risiko.pojo.Tripel;

import java.util.List;
import java.util.Vector;

/**
 * Created by
 *
 * @author Raphael
 * @version 28.01.2016
 */
public class AsyncAIActionDispatcher {

    private final static int ACTION_TIMEOUT = 2; //in ms
    private final static int CLICK_TIMEOUT = 8; //in ms
    private final static int MOUSE_PRESSED_TIMEOUT = 2; //in ms

    private Thread worker;
    private volatile boolean workerStarted = false;

    private final TaskFinishedListener finListener;
    private final Vector<Tripel<AI.AiTroupState,Territory,Territory>> actions = new Vector<>();
    private final Runnable dispatcher = new Runnable() {
        @Override
        public void run() {
            workerStarted = true;

            while( true ) {
                final int max = actions.size();

                if( max > 0 ) { if( !executeTasks(max) ) break; }
                else {
                    new Thread(finListener::taskFinished).start();

                    try {
                       synchronized (actions) { actions.wait(); }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }

                }

                if( Thread.interrupted() ) break;
            }

            System.err.println("Dispatcher ends here ...");
            new Thread(finListener::taskFinished).start();
        }

        private boolean executeTasks(int max) {
            int i = 0;
            for(;i<max;i++) {
                //Pump Action:
                Tripel<AI.AiTroupState,Territory,Territory> act = actions.get(0);

                //do left click action
                act.y.getOwner().setCurrentActiveTerritory(act.y);
                if( !wait(CLICK_TIMEOUT) ) break;

                //do right click action
                act.z.setMouseState(MouseState.R_CLICKED);
                if( !wait(MOUSE_PRESSED_TIMEOUT) ) break;
                act.z.setMouseState(MouseState.HOVER);
                if( !wait(CLICK_TIMEOUT) ) break;

                //perform attack
                act.y.getOwner().attackOrMove(act.z);
                if( !wait(CLICK_TIMEOUT/2) ) break;

                //undo selecting
                act.y.getOwner().setCurrentActiveTerritory(null);

                //remove only after all actions are complete -> multi core sanity!
                actions.remove(act);

                if( !wait(CLICK_TIMEOUT/2) ) break;
            }

            wait(ACTION_TIMEOUT);
            return i==max;
        }

        private boolean wait(int timeout) {
            try { Thread.sleep(timeout); } catch (InterruptedException e) { return false; }
            return true;
        }
    };


    public AsyncAIActionDispatcher(TaskFinishedListener listener) {
        this.finListener = listener;
    }

    private void notifyRunningTask() {
        if( this.worker.isAlive() ) synchronized (actions) { actions.notifyAll(); }
        else this.startWorker();
    }

    private void startWorker() {
        this.worker = new Thread(dispatcher);
        this.worker.setDaemon(true);
        this.worker.setName("AsyncAIDispatcher$worker");
        this.worker.start();
    }

    public synchronized void startDispatching() {
        if( this.worker != null ) this.notifyRunningTask();
        else this.startWorker();
    }

    public synchronized void restartDispatching() {
        if( this.worker.isAlive() ) this.worker.interrupt();
        this.startWorker();
    }

    public synchronized void abortDispatching() {
        if( this.worker != null && this.worker.isAlive() )
            this.worker.stop();
    }

    public synchronized void queueActions(List<Tripel<AI.AiTroupState,Territory,Territory>> actions) {
        this.actions.addAll(actions);
    }
}
