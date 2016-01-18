package pk.risiko.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 * Created by
 *
 * @author Raphael
 * @version 18.01.2016
 */
public class FontLoader {

    private static final String DEFAULT_FONT = "Arial";


    public static Font loadFont(String name, float size) {

        try {
            Font newF = Font.createFont(Font.TRUETYPE_FONT
                                       ,new File(SettingsProvider
                                                    .getInstance()
                                                    .getFontDirectoryPath()+name)
                                        );

            return newF.deriveFont(size);
        } catch (FontFormatException | IOException e) {
            System.err.println("Unable to load Font, falling back to default!");
            e.printStackTrace();

            assert false : "Unable to load Font, aborting in debug mode!";
            return Font.getFont(DEFAULT_FONT).deriveFont(size);
        }
    }

}
