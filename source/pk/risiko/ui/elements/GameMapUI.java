package pk.risiko.ui.elements;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Territory;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class GameMapUI extends UIElement {

    private final GameMap gameMap;
    private final Set<Connection> connections = new LinkedHashSet<>();
    private TerritoryHover territoryHover;

    public GameMapUI(GameMap map) {
        super(new Rectangle(1254,504));
        this.gameMap = map;

        //Generate Connection between Territories for visualization
        this.gameMap.getTerritories().forEach(territory ->
            territory.getNeighbours().forEach(neighbour -> {
                Connection con = new Connection(territory,neighbour);
                if( !this.connections.contains(con) )
                    this.connections.add(con);
            })
        );
    }

    @Override
    public void paint(Graphics g) {
        this.connections.forEach(connection -> connection.paint(g));
        this.gameMap.getTerritories().forEach(territory -> territory.paint(g));

        if( this.territoryHover != null ) this.territoryHover.paint(g);
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseClicked(e) ) return true;

        return false;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        this.isMouseIn(e.getX(),e.getY());

        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseMoved(e) ) return true;

        return false;
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        for(Territory t:this.gameMap.getTerritories())
            if( t.isMouseIn(x,y) ) {
                if( this.territoryHover == null || !this.territoryHover.getTerritory().equals(t) ) {
                    this.territoryHover = new TerritoryHover(t,x + 10 ,y + 10);
                } else if( this.territoryHover != null ) {
                    this.territoryHover.moveTo(x + 10 ,y + 10);
                }
                return true;
            }

        this.territoryHover = null;
        return false;
    }

    @Override
    public boolean mouseClicked() {
        for(Territory t:this.gameMap.getTerritories())
            if( t.mouseClicked() ) return true;

        return false;
    }
}
