package pk.risiko.ui.screens;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.GameScreenManager;
import pk.risiko.util.RoundManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
    private final BufferedImage gameBoard = new BufferedImage(GameMapUI.GAME_MAP_WIDTH
                                                             ,GameMapUI.GAME_MAP_HEIGHT
                                                             ,BufferedImage.TYPE_INT_ARGB);
    private final UserInterface userInterface;
    private final SwingMouseEventDispatcher mouseHandler = new SwingMouseEventDispatcher();

    private GameState currentGameState;
    private GameState currentMetaState = GameState.HIDE_MENU;
    private RoundManager roundManager;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameScreenManager gameScreenManager) {
        this.roundManager = new RoundManager(playerList, this, gameMap);
        this.gameMapUI = new GameMapUI(gameMap,roundManager);

        this.currentGameState = GameState.SET_UNIT;
        this.userInterface = new UserInterface(this
                                              ,gameScreenManager.getWindow().getWidth()
                                              ,gameScreenManager.getWindow().getHeight());

        //Init mouse handler
        this.mouseHandler.registerListener(this.userInterface);
        this.mouseHandler.registerListener(new MouseAdapter() {
            private MouseEvent generateProxyEvent(MouseEvent e) {
                return new MouseEvent((Component) e.getSource()
                        ,e.getID()
                        ,e.getWhen()
                        ,e.getModifiers()
                        ,e.getX()
                        ,e.getY()-UserInterface.BAR_HEIGHT
                        ,e.getClickCount(),false);
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                gameMapUI.mouseClicked(generateProxyEvent(e));
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                gameMapUI.mouseMoved(generateProxyEvent(e));
            }
        });
    }

    public void changeState(GameState state) {
        System.out.println("Switch to State: " + state);

        switch (state) {
            case NEXT_ROUND: this.roundManager.nextPlayer(); break;
            case SHOW_MENU: this.gameMapUI.setCurrentlyPlaying(false); break;
            case HIDE_MENU: this.gameMapUI.setCurrentlyPlaying(true); break;
            default: System.out.println("State is unknown!");
        }

        /*if( state == GameState.SET_UNIT && this.roundManager.isAtTheBeginning() )
            state = GameState.REINFORCE_UNITS;
        else if( state == GameState.REINFORCE_UNITS && this.roundManager.isAtTheBeginning() )
            state = GameState.ATTACK_OR_MOVE_UNIT;
           */
        if( state == GameState.HIDE_MENU || state == GameState.SHOW_MENU )
            this.currentMetaState = state;


        this.currentGameState=state;
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
        /* shift the gamemap down because we have a HUD which can render parts inaccessible */
        Graphics2D gGBoard = (Graphics2D) this.gameBoard.getGraphics();
        gGBoard.setComposite(AlphaComposite.Src);
        gGBoard.setColor(new Color(255,255,255,0));
        gGBoard.fillRect(0,0,GameMapUI.GAME_MAP_WIDTH,GameMapUI.GAME_MAP_HEIGHT);
        this.gameMapUI.paint(gGBoard);

        g.drawImage(this.gameBoard,0,UserInterface.BAR_HEIGHT,null);
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
