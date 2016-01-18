package pk.risiko.util;

import java.util.Properties;

/**
 * Created by
 *
 * @author Raphael
 * @version 18.01.2016
 */
public class SettingsProvider {

    private static SettingsProvider provider;

    private final Properties settings;
    private final CommandParser parser;

    private SettingsProvider(Properties settings, CommandParser parser) {
        this.settings = settings;
        this.parser = parser;
    }

    public static void createSettingsProvider(Properties settings,CommandParser parser) {
        provider = new SettingsProvider(settings,parser);
    }

    public static SettingsProvider getInstance() {
        assert provider != null : "SettingsProvider must be initialized first!";

        return provider;
    }

    public String getMapDirectoryPath() {
        return  settings.getProperty("assets") + settings.getProperty("maps") + "/";
    }

    public String getFontDirectoryPath() {
        return  settings.getProperty("assets") + settings.getProperty("fonts") + "/";
    }

    public int getFPS() {
        return Integer.valueOf(settings.getProperty("fps"));
    }

    public CommandParser getCommandLine() {
        return this.parser;
    }
}
