package pk.risiko;

import pk.risiko.pojo.Capital;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Territory;
import pk.risiko.ui.GameWindow;

import java.awt.*;
import java.util.Collections;

/**
 * This is the Main class, it does handle all
 * static stuff which is needed to set up and
 * shut down the game.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Risiko {

    private GameWindow gameWindow;

    public static void main(String[] args) {
        /* TODO: implement some useful stuff:
            Properties settings = new Properties(...)
            MapFileReader fileReader = new MapFileReader(...)
            etc ...
         */

        //TODO: After Setup populate models:
        GameMap map = Risiko.constructGameMap();

        //TODO: Start Game / Show Game window:
        Risiko.showUserInterface(map);
    }

    public Risiko(GameMap map) {
        GameWindow gameWindow = new GameWindow(map);
        gameWindow.setVisible(true);
    }

    /** Every UI Modification and Event must be dispatched by the EventQueue! **/
    private static void showUserInterface(GameMap map) {
        EventQueue.invokeLater(() -> new Risiko(map));
    }

    /** This is DEBUG Code: **/
    private static GameMap constructGameMap() {
        //The data in this code was taken from squeres.map but multipled with 100 because
        //otherwise it does draw under the Taskbar (at least in Windows 10)

        Capital cp = new Capital("Western",145,145);
        Polygon p1 = new Polygon(new int[]{120,170,170,120},new int[]{120,120,170,120},4);
        Territory western = new Territory("Western",cp,p1);

        return new GameMap("squares.map (DEBUG)", Collections.singletonList(western));
    }

}
