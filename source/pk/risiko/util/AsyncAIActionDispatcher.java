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
 * Created by
 *
 * @author Raphael
 * @version 28.01.2016
 */
public class AsyncAIActionDispatcher {

    private final static int ACTION_TIMEOUT = 0; //in ms
    private final static int CLICK_TIMEOUT = 0; //in ms
    private final static int MOUSE_PRESSED_TIMEOUT = 0; //in ms

    private Thread worker;

    private final TaskFinishedListener finListener;
    private final Vector<Tripel<AI.AiTroupState,Territory,Territory>> actions = new Vector<>();
    private final Vector<String> verboseActions = new Vector<>();
    private final Runnable dispatcher = new Runnable() {
        @Override
        public void run() {
            while( true ) {
                final int max = actions.size();

                if( max > 0 ) { if( !executeTasks(max) ) return; }
                else {
                    new Thread(finListener::taskFinished).start();

                    try {
                       synchronized (actions) { actions.wait(); }
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                if( Thread.interrupted() ) return;
            }
        }

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

        private boolean wait(int timeout) {
            try { Thread.sleep(timeout); } catch (InterruptedException e) { return false; }
            return true;
        }
    };


    public AsyncAIActionDispatcher(TaskFinishedListener listener) {
        this.finListener = listener;
        this.verboseActions.add("AI is thinking ...");
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
            this.worker.interrupt();
    }

    public synchronized void queueActions(List<Tripel<AI.AiTroupState,Territory,Territory>> actions) {
        this.actions.addAll(actions);
    }

    public String getNextAction() {
        if( verboseActions.size() == 1 ) return verboseActions.get(0);
        return verboseActions.remove(0);
    }
}
