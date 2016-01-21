package pk.risiko;

import pk.risiko.dao.MapFileReader;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameScreenType;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.RandomAI;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.MainWindow;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.MainMenuPanel;
import pk.risiko.ui.screens.NewGamePanel;
import pk.risiko.util.CommandParser;
import pk.risiko.util.SettingsProvider;

import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This is the class that holds the main method. It handles everything
 * that happens before the gamestart, and opens the Main Window.
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 21.1.2016
 */
public class Risiko {

    private static final String SETTINGS_FILE = "./settings.properties";
    private final MainWindow mainWindow;

    public static void main(String[] args) {
        //Todo: remove this skip menu hack when not needed:
        List<String> argvs = new ArrayList<>(Arrays.asList(args));
        //argvs.add("--skip-menu");

        final CommandParser cmdParser = new CommandParser(argvs.toArray(new String[argvs.size()]));
        if( cmdParser.isInvalid() ) {
            System.out.println("Usage: Risik.java [--map <path-of-mapfile>] [--skip-menu]");
            return; //huston we have a problem, garbage on our command line!
        }

        // read settings
        final Properties settings = new Properties();
        try (FileReader sFile = new FileReader(new File(SETTINGS_FILE))) {
            settings.load(sFile);
        } catch (IOException e) {
            System.err.println("Unable to read Settings file ("+SETTINGS_FILE+")!");
            e.printStackTrace();
            return; //well at least we failed soon enough and don't mess up
        }

        /* Globally saves constants as in settings.properties */
        SettingsProvider.createSettingsProvider(settings,cmdParser);


        EventQueue.invokeLater(() -> new Risiko());
    }


    public Risiko() {
        this.mainWindow = new MainWindow();

        if( !SettingsProvider.getInstance().getCommandLine().isSkipMenu() )
            mainWindow.loadMenuPanel();
        else {
            String mapFilePath = SettingsProvider.getInstance().getCommandLine().getMapFile() != null ?
                    SettingsProvider.getInstance().getCommandLine().getMapFile() :
                    SettingsProvider.getInstance().getDefaultMapName();

            mainWindow.loadGamePanel(mapFilePath);
        }
    }


}
