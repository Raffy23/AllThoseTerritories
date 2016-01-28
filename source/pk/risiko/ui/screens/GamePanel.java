package pk.risiko.ui.screens;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.AsyncAIActionDispatcher;
import pk.risiko.util.AsyncRoundListener;
import pk.risiko.util.GameScreenManager;
import pk.risiko.util.RoundManager;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;


/**
 * This class represents the Game Frame with the User Interface.
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
    private final AsyncAIActionDispatcher aiActionDispatcher;
    private final AsyncRoundListener roundListener = new AsyncRoundListener();


    private GameState currentMetaState = GameState.HIDE_MENU;
    private RoundManager roundManager;
    private GameScreenManager gsm;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameScreenManager gameScreenManager) {
        this.gsm = gameScreenManager;
        this.aiActionDispatcher = new AsyncAIActionDispatcher(this.roundListener);
        this.roundManager = new RoundManager(playerList,gameMap,aiActionDispatcher);
        this.gameMapUI = new GameMapUI(gameMap,roundManager);

        this.userInterface = new UserInterface(this);

        //lazy init round listener
        this.roundListener.setRm(this.roundManager);
        this.roundListener.setAiActionDispatcher(this.aiActionDispatcher);

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

        //Init Button Listener in Menu:
        this.userInterface.getMenu().setExitGameListener((btn) -> this.endGame());
        this.userInterface.getMenu().setSaveGameListener((btn) -> System.out.println("Not Implemented! (in GamePanel)"));
    }

    /**
     *  This function does recieve all GameState changes which can change the
     *  Meta state of the Game (thus all which change the UI responses)
     * @param state new GameState
     */
    public void changeState(GameState state) {
        System.out.println("Switch to State: " + state);

        switch (state) { //switch Meta Gamestates:
            case NEXT_ROUND: this.roundManager.nextPlayer(); break;
            case SHOW_MENU: this.gameMapUI.setCurrentlyPlaying(false); break;
            case HIDE_MENU: this.gameMapUI.setCurrentlyPlaying(true); break;
            default: System.out.println("State is unknown!");
        }

        if( state == GameState.HIDE_MENU || state == GameState.SHOW_MENU )
            this.currentMetaState = state;
    }

    public Player getWinningPlayer() {
        return null; //TODO implement ...
    }

    public int getRounds() {
        return this.roundManager.getCurrentRound();
    }

    public GameState getGameState() {
        return this.roundManager.getCurrentGameState();
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
    public void shown() { }


    private void endGame() {
        this.aiActionDispatcher.abortDispatching();
        gsm.showMenu();
    }

}
