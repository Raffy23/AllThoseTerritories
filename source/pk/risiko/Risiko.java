package pk.risiko;

import pk.risiko.dao.MapFileReader;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameScreenType;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.MainMenuPanel;
import pk.risiko.ui.screens.NewGamePanel;
import pk.risiko.util.CommandParser;
import pk.risiko.util.SettingsProvider;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This is the Main class, it does handle all
 * static stuff which is needed to set up and
 * shut down the game.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Risiko extends GameWindow {

    private static final String SETTINGS_FILE = "./settings.properties";

    private final MainMenuPanel gameMenu;
    private final NewGamePanel newGameMenu;

    public static void main(String[] args) {
        //Todo: remove this skip menu hack when not needed:
        List<String> argvs = new ArrayList<>(Arrays.asList(args));
        //argvs.add("--skip-menu");

        final CommandParser cmdParser = new CommandParser(argvs.toArray(new String[argvs.size()]));
        if( cmdParser.isInvalid() ) {
            System.out.println("Usage: Risik.java [--map <path-of-mapfile>] [--skip-menu]");
            return; //huston we have a problem, garbage on our command line!
        }

        final Properties settings = new Properties();
        try (FileReader sFile = new FileReader(new File(SETTINGS_FILE))) {
            settings.load(sFile);
        } catch (IOException e) {
            System.err.println("Unable to read Settings file ("+SETTINGS_FILE+")!");
            e.printStackTrace();
            return; //well at least we failed soon enough and mess not up
        }

        /* Globally saves constants as in settings.properties */
        SettingsProvider.createSettingsProvider(settings,cmdParser);
        MapFileReader fileReader = new MapFileReader(SettingsProvider.getInstance().getMapDirectoryPath());

        EventQueue.invokeLater(() -> new Risiko(fileReader));
    }

    //TODO: Map should not be placed here somewhere later
    public Risiko(MapFileReader mapFileReader) {
        super(SettingsProvider.getInstance().getFPS());

        /* initialize components */
        this.gameMenu = new MainMenuPanel(this.getWidth(),this.getHeight());
        this.newGameMenu = new NewGamePanel(mapFileReader);

        /* register components */
        this.getGameScreenManager().addScreen(GameScreenType.START_MENU_SCREEN,gameMenu);
        this.getGameScreenManager().addScreen(GameScreenType.NEW_GAME_SCREEN,this.newGameMenu);

        /* setup menu listeners */
        this.gameMenu.getExitGame().setListener((what) -> this.exitGame());
        this.gameMenu.getLoadGame().setListener((what) -> System.out.println("No loading implemented!"));
        this.gameMenu.getNewGame().setListener((what) -> this.showNewGameScreen());

        /* setup new game menu listeners */
        this.newGameMenu.registerNewGameListener((btn) -> this.startNewGame(this.newGameMenu.getSelectedGameMap()));

        if( !SettingsProvider.getInstance().getCommandLine().isSkipMenu() ) this.getGameScreenManager().showMenu();
        else startNewGame(mapFileReader.readMap(SettingsProvider.getInstance().getCommandLine().getMapFile()));

        this.setVisible(true);
    }

    private void showNewGameScreen() {
        this.getGameScreenManager().showScreen(GameScreenType.NEW_GAME_SCREEN);
    }

    private void startNewGame(GameMap map) {
        System.out.println("Start new Game");
        GamePanel gamePanel = new GamePanel(map
                                            ,this.newGameMenu.getPlayers()
                                            ,this.getGameScreenManager());

        this.getGameScreenManager().showGame(gamePanel);
    }

    private void exitGame() {
        //Save settings or do some other stuff before disposing the window

        this.dispose();
    }

    /** This is DEBUG Code: **/
    /*private static GameMap constructGameMap() {
        //The data in this code was taken from squeres.map but multipled with 100 because
        //otherwise it does draw under the Taskbar (at least in Windows 10)

        Capital cp = new Capital("Western",145,145);
        Polygon p1 = new Polygon(new int[]{120,170,170,120,120,120},
                                 new int[]{120,120,170,170,120,120},5);
        Polygon p2 = new Polygon(new int[]{220,370,370,220,220,220},
                                 new int[]{220,220,370,370,220,220},5);
        Territory western = new Territory("Western",cp, GeometryHelper.generateAreaFrom(Arrays.asList(p1,p2)));

        Polygon p3 = new Polygon(new int[]{914,1111,916},
                                 new int[]{141,159,248},3);
        Capital cp1 = new Capital("Eastern",914,200);
        Territory estern = new Territory("Eastern",cp1,GeometryHelper.generateAreaFrom(Arrays.asList(p3)));

        western.getNeighbours().add(estern);
        estern.getNeighbours().add(western);

        return new GameMap("squares.map (DEBUG)", new ArrayList<>(Arrays.asList(western,estern)));
    }*/

}
