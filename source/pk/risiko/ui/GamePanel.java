package pk.risiko.ui;

import pk.risiko.pojo.Drawable;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.util.CyclicList;
import pk.risiko.util.GameStateMachine;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;


/**
 * This class represents the Game Frame with the User Interface <br/>
 * It does also handle some state changes in the game
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class GamePanel implements Drawable {

    private final GameMap gameMap;
    private final UserInterface userInterface;
    private final GameStateMachine gsm;

    private GameState currentGameState;

    private final CyclicList<Player> players = new CyclicList<>();
    private int currentRound;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameStateMachine gameStateMachine) {
        this.gameMap = gameMap;
        this.players.addAll(playerList);
        this.gsm = gameStateMachine;
        this.userInterface = new UserInterface(this,gameStateMachine.getWindow().getWidth(),gameStateMachine.getWindow().getHeight());
    }

    public void changeState(GameState state) {
        System.out.println("Switch to State: " + state);

        switch (state) {
            case NEXT_ROUND:
                this.players.next();
                this.currentRound++;
                break;
        }

        this.currentGameState = state;
        gsm.repaintWindow();
    }

    public Player getWinningPlayer() {
        return null; //TODO implement ...
    }

    public int getRounds() {
        return this.currentRound;
    }

    public Player getCurrentPlayer() {
        return this.players.peek();
    }

    @Override
    public void paint(Graphics g) {
        this.gameMap.paint(g);
        this.userInterface.paint(g);
    }

    public MouseAdapter getMouseAdapter() {
        return this.userInterface.getMouseListener();
    }
}
