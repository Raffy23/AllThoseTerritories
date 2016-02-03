package pk.risiko.util;

import pk.risiko.pojo.AI;
import pk.risiko.pojo.MouseState;
import pk.risiko.pojo.PlayerAI;
import pk.risiko.pojo.TaskFinishedListener;
import pk.risiko.pojo.Territory;
import pk.risiko.pojo.Tripel;

import java.util.List;
import java.util.Vector;

/**
 * This class is responsible for dispatching the AI actions
 * in a speed that a human can see what the AI is doing
 *
 * @author Raphael Ludwig
 * @version 28.01.2016
 */
public class AsyncAIActionDispatcher {

    private final static int ACTION_TIMEOUT = 200; //in ms
    private final static int CLICK_TIMEOUT = 800; //in ms
    private final static int MOUSE_PRESSED_TIMEOUT = 2; //in ms

    /** Background Thread whi does the actual work **/
    private Thread worker;

    /** This Listener is informed after the the taskqueue is empty **/
    private final TaskFinishedListener finListener;
    private final Vector<Tripel<AI.AiTroupState,Territory,Territory>> actions = new Vector<>();
    private final Vector<String> verboseActions = new Vector<>();

    /**
     * This class does handle the logic and work in the run Method:
     */
    private final Runnable dispatcher = new Runnable() {
        @Override
        public void run() {
            //yes this is a endless loop ... we wait for a thread interruption
            while( true ) {
                final int max = actions.size();

                //exceute tasks
                if( max > 0 ) { if( !executeTasks(max) ) return; }
                else {
                    //we are finished
                    new Thread(finListener::taskFinished).start();

                    //let's wait for more work
                    try {
                       synchronized (actions) { actions.wait(); }
                    } catch (InterruptedException e) {
                        break;
                    }
                }

                //if we are interrupted we do stop
                if( Thread.interrupted() ) return;
            }
        }

        /**
         * All tasks till max index are dispatched
         * @param max the max number of tasks which should be dispatched
         * @return true if all the tasks could be dispatched
         */
        private boolean executeTasks(int max) {
            String moveType,succType;

            int i = 0;
            for(;i<max;i++) {
                //Pump Action:
                Tripel<AI.AiTroupState,Territory,Territory> act = actions.get(0);

                assert (act.y.getOwner() instanceof AI) : "Can only process AIs!";

                //validate action:
                if( act.y.getStationedArmies() <= 1                                                  ||
                    ( !((PlayerAI)act.y.getOwner()).canAttack() && act.x == AI.AiTroupState.ATTACK ) ||
                    ( !act.z.getOwner().equals(act.y.getOwner()) && act.x == AI.AiTroupState.MOVE  ) ){ // abort action
                    actions.remove(act);
                    continue;
                }

                //do some user feedback
                if( !act.y.getOwner().equals(act.z.getOwner()) ) moveType = " attacks ";
                else moveType = " moves ";

                //do left click action
                act.y.getOwner().setCurrentActiveTerritory(act.y);
                if( !wait(CLICK_TIMEOUT) ) break;

                //do right click action
                act.z.setMouseState(MouseState.R_CLICKED);
                if( !wait(MOUSE_PRESSED_TIMEOUT) ) break;
                act.z.setMouseState(MouseState.HOVER);
                if( !wait(CLICK_TIMEOUT) ) break;

                final int oldAttacker = act.y.getStationedArmies();
                //final int oldDefender = act.z.getStationedArmies();

                //perform attack
                if( act.y.getOwner().attackOrMove(act.z) ) {
                    if (moveType.equals(" attacks ")) {
                        succType = " (successfully conquered with " + (act.y.getStationedArmies() - oldAttacker) + " armies !)";
                    } else succType = " (moved 1 army there)";
                } else {
                    if (moveType.equals(" attacks ")) {
                        succType = " (failed to conquer Territory, " + (act.z.getStationedArmies()) + " armies still alive)";
                    } else succType = " (failed to move the army there ....)";
                }
                verboseActions.add(act.y.getOwner().getName() + moveType + act.z.getName() + succType);

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

        /**
         * Just waits for a specific timeout
         * @param timeout timeout in ms
         * @return true if successful, false if interrupted
         */
        private boolean wait(int timeout) {
            try { Thread.sleep(timeout); } catch (InterruptedException e) { return false; }
            return true;
        }
    };


    public AsyncAIActionDispatcher(TaskFinishedListener listener) {
        this.finListener = listener;
        this.verboseActions.add("AI is thinking ...");
    }

    /**
     * Send a notification the the BackgroundTask, should be used
     * to notify the BackgroundTask about new work
     */
    private void notifyRunningTask() {
        if( this.worker.isAlive() ) synchronized (actions) { actions.notifyAll(); }
        else this.startWorker();
    }

    /**
     * Starts the BackgroundTask
     */
    private void startWorker() {
        this.worker = new Thread(dispatcher);
        this.worker.setDaemon(true);
        this.worker.setName("AsyncAIDispatcher$worker");
        this.worker.start();
    }

    /**
     * Starts Dispatching the tasks in the TaskQueue
     */
    public synchronized void startDispatching() {
        if( this.worker != null ) this.notifyRunningTask();
        else this.startWorker();
    }


    /**
     * Stops Dispatching, leaves data in the TaskQueue
     */
    public synchronized void abortDispatching() {
        if( this.worker != null && this.worker.isAlive() )
            this.worker.interrupt();
    }

    /**
     * Add some AI actions to the Queue
     * @param actions a list of actions
     */
    public synchronized void queueActions(List<Tripel<AI.AiTroupState,Territory,Territory>> actions) {
        this.actions.addAll(actions);
    }

    /**
     * @return a string of the dispatched actions, in the order in which they where dispatched
     */
    public String getNextAction() {
        if( verboseActions.size() == 1 ) return verboseActions.get(0);
        return verboseActions.remove(0);
    }
}
