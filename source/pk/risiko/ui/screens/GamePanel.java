package pk.risiko.ui.screens;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.listener.MouseEventListener;
import pk.risiko.ui.listener.MouseHandler;
import pk.risiko.util.CyclicList;
import pk.risiko.util.GameScreenManager;

import java.awt.Graphics;
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

    private final GameMapUI gameMap;
    private final UserInterface userInterface;
    private final GameScreenManager gsm;
    private final MouseHandler mouseHandler = new MouseHandler();

    private GameState currentGameState;

    private final CyclicList<Player> players = new CyclicList<>();
    private int currentRound;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameScreenManager gameScreenManager) {
        this.gameMap = new GameMapUI(gameMap);
        this.players.addAll(playerList);
        this.gsm = gameScreenManager;
        this.userInterface = new UserInterface(this, gameScreenManager.getWindow().getWidth(), gameScreenManager.getWindow().getHeight());

        //Init mouse handler
        this.mouseHandler.addMouseEventListener(this.userInterface.getMouseListener());
        this.mouseHandler.addMouseEventListener(this.gameMap);
    }

    public void changeState(GameState state) {
        System.out.println("Switch to State: " + state);

        switch (state) {
            case NEXT_ROUND:
                this.players.next();
                if( this.players.isAtBeginning() ) this.currentRound++;
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

    @Override
    public MouseEventListener getMouseAdapter() {
        return this.mouseHandler;
    }

    @Override
    public KeyAdapter getKeyAdapter() {
        return null;
    }

}
