package pk.risiko.ui.elements;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Territory;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class GameMapUI extends UIElement {

    private final GameMap gameMap;

    public GameMapUI(GameMap map) {
        this.gameMap = map;
    }

    @Override
    public void paint(Graphics g) {
        this.gameMap.getTerritories().forEach(territory -> territory.paint(g));
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseClicked(e) ) return true;

        return false;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseMoved(e) ) return true;

        return false;
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        for(Territory t:this.gameMap.getTerritories())
            if( t.isMouseIn(x,y) ) return true;

        return false;
    }

    @Override
    public boolean mouseClicked() {
        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseClicked() ) return true;

        return false;
    }

    @Override
    public boolean keyEntered(char key) { return false; }
}
