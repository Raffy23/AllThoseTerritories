package pk.risiko.util;

import java.io.File;
import java.util.Properties;

/**
 * This class globally holds all the static information which
 * should be available to all parts of the Program such as
 * settings from the settings file or the parsed commandline
 *
 * Therefore it can only used as a singleton
 *
 * @author Raphael Ludwig
 * @version 18.01.2016
 */
public class SettingsProvider {

    /** stores the actual instance of the SettingsProvider **/
    private static SettingsProvider provider;

    /** the settings from the settings-file **/
    private final Properties settings;
    /** the parsed commandline **/
    private final CommandParser parser;

    /**
     * This constructor is private so only one instance can be created
     *
     * @param settings Settings from the Settingsfile
     * @param parser a CommandParser which is able to parse the CommandLine
     * @see Properties
     * @see CommandParser
     */
    private SettingsProvider(Properties settings, CommandParser parser) {
        this.settings = settings;
        this.parser = parser;
    }

    /**
     * This method creates a instance of the SettingsProvider. It should only be called
     * once in the complete Program lifecycle!
     *
     * @param  settings Settings from the Settingsfile
     * @param parser a CommandParser which is able to parse the CommandLine
     * @see Properties
     * @see CommandParser
     */
    public static void createSettingsProvider(Properties settings,CommandParser parser) {
        assert provider == null : "SettingsProvider should not be initialized multiple times!";

        provider = new SettingsProvider(settings,parser);
    }

    /**
     * Before this method can be called a instance must be created with
     * the #{createSettingsProvider} Method first!
     *
     * @return a instance of the SettingsProvider
     */
    public static SettingsProvider getInstance() {
        assert provider != null : "SettingsProvider must be initialized first!";

        return provider;
    }

    /**
     * @return the default path of the Map Directory
     */
    public String getMapDirectoryPath() {
        return  settings.getProperty("assets") + settings.getProperty("maps") + File.separator;
    }

    /**
     * @return the default path of the Font directory
     */
    public String getFontDirectoryPath() {
        return  settings.getProperty("assets") + settings.getProperty("fonts") + File.separator;
    }

    public String getResourceDirectory() {
        return  settings.getProperty("assets") + File.separator;
    }

    /**
     * @return how many FPS the GameWindow should have
     * @see pk.risiko.ui.GameWindow
     */
    public int getFPS() {
        return Integer.valueOf(settings.getProperty("fps"));
    }

    /**
     * @return the default mapname
     */
    public String getDefaultMapName() {
        return settings.getProperty("defaultMap");
    }

    /**
     * @return the CommandParser which parsed the commandline
     */
    public CommandParser getCommandLine() {
        return this.parser;
    }

    /**
     * @return the path in which the savegame files are stored
     */
    public String getSavefileDirectory() {
        return settings.getProperty("saves");
    }

    /**
     * @return the internal stored settings
     */
    public Properties getSettings() {
        return this.settings;
    }
}
