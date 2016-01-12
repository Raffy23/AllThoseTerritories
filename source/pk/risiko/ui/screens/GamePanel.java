package pk.risiko.ui.screens;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.GameScreenManager;
import pk.risiko.util.RoundManager;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.util.List;


/**
 * This class represents the Game Frame with the User Interface <br/>
 * It does also handle some state changes in the game
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class GamePanel implements GameScreen {

    private final GameMapUI gameMapUI;
    private final UserInterface userInterface;
    private final GameScreenManager gsm;
    private final SwingMouseEventDispatcher mouseHandler = new SwingMouseEventDispatcher();

    private GameState currentGameState;
    private RoundManager roundManager;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameScreenManager gameScreenManager) {
        this.roundManager = new RoundManager(playerList);
        this.gameMapUI = new GameMapUI(gameMap,roundManager);

        this.currentGameState = GameState.SET_UNIT;
        this.gsm = gameScreenManager;

        this.userInterface = new UserInterface(this
                                              ,gameScreenManager.getWindow().getWidth()
                                              ,gameScreenManager.getWindow().getHeight());

        //Init mouse handler
        this.mouseHandler.registerListener(this.userInterface);
        this.mouseHandler.registerListener(this.gameMapUI);
    }

    public void changeState(GameState state) {
        System.out.println("Switch to State: " + state);

        switch (state) {
            case NEXT_ROUND: this.roundManager.nextPlayer(); break;
        }

        if( state == GameState.SET_UNIT && this.roundManager.isAtTheBeginning() )
            state = GameState.REINFORCE_UNITS;
        else if( state == GameState.REINFORCE_UNITS && this.roundManager.isAtTheBeginning() )
            state = GameState.ATTACK_OR_MOVE_UNIT;

        gsm.repaintWindow();
    }

    public Player getWinningPlayer() {
        return null; //TODO implement ...
    }

    public GameState getCurrentGameState() {
        return this.currentGameState;
    }

    public int getRounds() {
        return this.roundManager.getCurrentRound();
    }

    public Player getCurrentPlayer() {
        return this.roundManager.getCurrentPlayer();
    }

    @Override
    public void paint(Graphics2D g) {
        this.gameMapUI.paint(g);
        this.userInterface.paint(g);
    }

    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return this.mouseHandler;
    }

    @Override
    public KeyAdapter getKeyAdapter() {
        return null;
    }

}
