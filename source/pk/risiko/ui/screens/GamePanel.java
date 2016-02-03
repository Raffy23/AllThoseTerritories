package pk.risiko.ui.screens;

import pk.risiko.dao.SaveGameDAO;
import pk.risiko.pojo.AI;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameScreenType;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.elements.SaveGameUIRow;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.AsyncAIActionDispatcher;
import pk.risiko.util.AsyncRoundListener;
import pk.risiko.util.GameScreenManager;
import pk.risiko.util.RoundManager;
import pk.risiko.util.SettingsProvider;

import javax.swing.JOptionPane;
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
 * @author Raphael Ludwig
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
    private final AsyncRoundListener roundListener;

    private RoundManager roundManager;
    private GameScreenManager gsm;

    public GamePanel(GameMap gameMap,List<Player> playerList,GameScreenManager gameScreenManager,boolean loaded) {
        this.gsm = gameScreenManager;
        this.roundListener = new AsyncRoundListener(this);
        this.aiActionDispatcher = new AsyncAIActionDispatcher(this.roundListener);
        this.roundManager = new RoundManager(playerList,gameMap,aiActionDispatcher,loaded);
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
                //If AI plays, no events possible
                if( roundManager.getCurrentPlayer() instanceof AI) gameMapUI.setAIPlaying(true);
                else gameMapUI.setAIPlaying(false);

                gameMapUI.mouseClicked(generateProxyEvent(e));
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                gameMapUI.mouseMoved(generateProxyEvent(e));
            }
        });

        //Init Button Listener in Menu:
        this.userInterface.getMenu().setExitGameListener((btn) -> this.endGame());
        this.userInterface.getMenu().setSaveGameListener((btn) -> {
            gameMapUI.setCurrentlyPlaying(false);

            final SaveGamePanel slpanel = (SaveGamePanel) gameScreenManager.getScreen(GameScreenType.SAVE_LOAD_GAME_SCREEN);
            slpanel.saveGameOnly();
            slpanel.registerSaveGameListener((saveBtn) -> {
                final int slot = ((SaveGameUIRow)saveBtn).getVisibleSlot();
                final SaveGameDAO dao = new SaveGameDAO(SettingsProvider.getInstance().getSavefileDirectory());

                String saveGameName = JOptionPane.showInputDialog("Type in a name for the SaveGame: ");
                if( saveGameName == null ) saveGameName = "Slot " + slot;

                dao.saveGameToSlot(gameMap,playerList,saveGameName,this.roundManager.getCurrentRound(),slot);

                gameScreenManager.showScreen(GameScreenType.GAME_SCREEN);
                //gameMapUI.setCurrentlyPlaying(true); //player must close menu!
            });

            gameScreenManager.showScreen(GameScreenType.SAVE_LOAD_GAME_SCREEN);
        });

        this.userInterface.getWinLoseDialog().setExitToMenuListener((btn) -> this.endGame());


        //Start Game (round 0 - AI events)
        this.roundManager.startGame();
    }

    /**
     *  This function does recieve all GameState changes which can change the
     *  Meta state of the Game (thus all which change the UI responses)
     * @param state new GameState
     */
    public void changeState(GameState state) {
        switch (state) { //switch Meta Gamestates:
            case NEXT_ROUND: this.roundManager.nextPlayer(); break;
            case SHOW_MENU: this.gameMapUI.setCurrentlyPlaying(false); break;
            case HIDE_MENU: this.gameMapUI.setCurrentlyPlaying(true);  break;
            case SHOW_WINLOSE: this.gameMapUI.setCurrentlyPlaying(false); break;
            case HIDE_WINLOSE: this.gameMapUI.setCurrentlyPlaying(true); break;
            //default: System.out.println("State is unknown!");
        }
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

        Player p;
        if ((p=this.roundManager.checkWinLose())!=null){//&&!this.userInterface.getWinLoseDialog().isActive()) {
            System.out.println("winlose");

            //if( !this.userInterface.getWinLoseDialog().isActive() )
            {
                this.changeState(GameState.SHOW_WINLOSE);
                this.userInterface.getWinLoseDialog().show(p);
            }
        }
    }

    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return this.mouseHandler;
    }

    @Override
    public void shown() { }

    public String getLastAction() {
        return this.roundManager.getLastAction();
    }

    private void endGame() {
        this.aiActionDispatcher.abortDispatching();
        gsm.showMenu();
    }

    public boolean isGamePaused() {
        return !this.gameMapUI.isCurrentlyPlaying();
    }

    public boolean isAIPlaying() {
        return this.roundManager.getCurrentPlayer() instanceof AI;
    }
}
