package pk.risiko;

import pk.risiko.pojo.Capital;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.Territory;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.screens.GamePanel;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the Main class, it does handle all
 * static stuff which is needed to set up and
 * shut down the game.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class Risiko extends GameWindow {

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
        super();

        this.getGameScreenManager().showGame(new GamePanel(map
                                                ,Arrays.asList(new Player("Me",Color.BLUE),new Player("You",Color.RED))
                                                ,this.getGameScreenManager())
                                    );
        this.setVisible(true);
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
        Polygon p1 = new Polygon(new int[]{120,170,170,120,120,120},
                                 new int[]{120,120,170,170,120,120},5);
        Territory western = new Territory("Western",cp,p1);

        Capital cp1 = new Capital("Eastern",295,295);
        Polygon p2 = new Polygon(new int[]{220,370,370,220,220,220},
                                 new int[]{220,220,370,370,220,220},5);
        Territory estern = new Territory("Eastern",cp1,p2);

        western.getNeighbours().add(estern);
        estern.getNeighbours().add(western);

        return new GameMap("squares.map (DEBUG)", new ArrayList<>(Arrays.asList(western,estern)));
    }

}
