package pk.risiko.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * This class is responsible for loading fonts from the Font Directory
 * which is specified by the @link{SettingsProvider}
 *
 * @author Raphael
 * @version 18.01.2016
 */
public class FontLoader {

    /** The name of the standard font which should be present any system
     *  it is used to as fallback one if the other font can not be loaded **/
    private static final String DEFAULT_FONT = "Arial";

    /**
     * This Method tries to load and register a font with the given name
     * in the System. The Path is taken from the SettingsProvider
     *
     * @param name name of the Font (FileName)
     * @param size the size in px of the Font
     * @return the loaded instance of the Font
     */
    public static Font loadFont(String name, float size) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Font newF = Font.createFont(Font.TRUETYPE_FONT
                                       ,new File(SettingsProvider
                                                    .getInstance()
                                                    .getFontDirectoryPath()+name)
                                        );
            ge.registerFont(newF);
            return newF.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            System.err.println("Unable to load Font, falling back to default!");
            e.printStackTrace();

            assert false : "Unable to load Font, aborting in debug mode!";
            return Font.getFont(DEFAULT_FONT).deriveFont(size);
        }
    }

}
