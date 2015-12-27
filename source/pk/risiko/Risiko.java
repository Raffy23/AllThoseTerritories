package pk.risiko;

import pk.risiko.ui.GameWindow;

import java.awt.*;

/**
 * This is the Main class, it does handle all
 * static stuff which is needed to set up and
 * shut down the game.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Risiko {

    public static void main(String[] args) {
        /* TODO: implement some useful stuff:
            Properties settings = new Properties(...)
            MapFileReader fileReader = new MapFileReader(...)
            etc ...
         */

        //TODO: After Setup populate models:

        //TODO: Start Game / Show Game window:
        GameWindow gameWindow = new GameWindow();
        EventQueue.invokeLater(() -> gameWindow.setVisible(true));
    }

}
