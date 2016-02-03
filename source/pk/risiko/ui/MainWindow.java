package pk.risiko.ui;

import pk.risiko.dao.MapFileReader;
import pk.risiko.dao.SaveGameDAO;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameScreenType;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.RandomAI;
import pk.risiko.pojo.SaveGameContent;
import pk.risiko.ui.elements.SaveGameUIRow;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.MainMenuPanel;
import pk.risiko.ui.screens.NewGamePanel;
import pk.risiko.ui.screens.SaveGamePanel;
import pk.risiko.util.SettingsProvider;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the MainWindow, which is able to load all the different screens into
 * the window.
 *
 * @author Wilma Weixelbaum, Raphael Ludwig
 * @version 21.1.2016
 */
public class MainWindow extends GameWindow {
    private final MainMenuPanel gameMenu;
    private final NewGamePanel newGameMenu;
    private final MapFileReader mapFileReader;
    private final SaveGamePanel saveGamePanel;

    /**
     * Constructs a Window with a certain size.
     * (Size is fixed by the Game)
     * #{setVisible} must be called explicitly to show the window!
     */
    public MainWindow() {
        super(SettingsProvider.getInstance().getFPS());
        mapFileReader = new MapFileReader(SettingsProvider.getInstance().getMapDirectoryPath());

        /* initialize components */
        this.gameMenu = new MainMenuPanel(this.getWidth(),this.getHeight());
        this.newGameMenu = new NewGamePanel(mapFileReader);
        this.saveGamePanel = new SaveGamePanel();

        /* initialize SaveGamePanel */
        this.saveGamePanel.registerLoadGameListener((btn) -> this.loadGame(((SaveGameUIRow)btn).getSaveGame()));
        this.saveGamePanel.getBackToMenuBtn().setListener((btn) -> getGameScreenManager().showMenu());

        /* register components */
        this.getGameScreenManager().addScreen(GameScreenType.START_MENU_SCREEN, this.gameMenu);
        this.getGameScreenManager().addScreen(GameScreenType.NEW_GAME_SCREEN,this.newGameMenu);
        this.getGameScreenManager().addScreen(GameScreenType.SAVE_LOAD_GAME_SCREEN,this.saveGamePanel);

        /* setup menu listeners */
        this.gameMenu.getExitGame().setListener((what) -> this.exitGame());
        this.gameMenu.getLoadGame().setListener((what) -> {
            this.saveGamePanel.loadGameOnly();
            this.getGameScreenManager().showScreen(GameScreenType.SAVE_LOAD_GAME_SCREEN);
        });
        this.gameMenu.getNewGame().setListener((what) -> this.showNewGameScreen());

        /* setup new game menu listeners */
        this.newGameMenu.registerNewGameListener((btn) -> this.startNewGame(this.newGameMenu.getSelectedGameMap()));

        this.setVisible(true);
    }
    private void showNewGameScreen() {
        this.getGameScreenManager().showScreen(GameScreenType.NEW_GAME_SCREEN);
    }

    private void startNewGame(GameMap map) {
        //should be changed to >1 probably, playing alone will help test win/lose logic tho
        List<Player> players = this.newGameMenu.getPlayers();
        if( players.size() != 0 ) {
            this.startNewGame(map,players);
        } else {
            System.err.println("A Game without Players is not very useful!");
        }
    }

    private void loadGame(SaveGameContent content) {
        final SaveGameDAO dao = new SaveGameDAO(SettingsProvider.getInstance().getSavefileDirectory());
        if( content.getSlot() >= 0 ) {
            GameMap map = dao.loadSaveGame(content.getSlot());
            List<Player> players = dao.getSaveGames().get(content.getSlot()).getPlayerList();
            this.getGameScreenManager().showGame( new GamePanel(map,players,this.getGameScreenManager(),true));
        } else {
            System.err.println("Unable to load EMPTY savegame!");
        }
    }

    private void startNewGame(GameMap map,List<Player> players) {
        this.getGameScreenManager().showGame( new GamePanel(map,players,this.getGameScreenManager(),false));
    }

    private void exitGame() {
        EventQueue.invokeLater(this::dispose);
    }

    public void loadMenuPanel() {
        this.getGameScreenManager().showMenu();
    }

    // called only from Risiko.java when name of mapfile was extracted from cmdline
    public  void loadGamePanel(String mapFilePath)
    {
        GameMap map = this.mapFileReader.readMap(mapFilePath);
        List<Player> players = new ArrayList<>(2);
        players.add(new Player("Player 1",new Color(10,10,200,200),map));
        players.add(new RandomAI("Player 2",new Color(10,200,10,200),map));

        this.startNewGame(map,players);
    }
}
