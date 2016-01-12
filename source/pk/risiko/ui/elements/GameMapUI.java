package pk.risiko.ui.elements;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Territory;
import pk.risiko.util.RoundManager;

import java.awt.Graphics2D;
import java.awt.Rectangle;
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

    private final RoundManager roundManager;
    private final GameMap gameMap;
    private final Set<Connection> connections = new LinkedHashSet<>();

    private TerritoryHover territoryHover;


    public GameMapUI(GameMap map,RoundManager manager) {
        super(new Rectangle(0,0,1250,650));
        this.gameMap = map;
        this.roundManager = manager;

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
    public void paint(Graphics2D g) {
        this.connections.forEach(connection -> connection.paint(g));
        this.gameMap.getTerritories().forEach(territory -> territory.paint(g));

        if( this.territoryHover != null ) this.territoryHover.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.gameMap.getTerritories().forEach(t -> t.mouseClicked(e));
        this.mouseClicked();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.isMouseIn(e.getX(),e.getY());
        this.gameMap.getTerritories().forEach(t -> t.mouseMoved(e));
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

    /**
     * TODO: Implement Round making logic:
     */
    @Override
    public void mouseClicked() {
        if( this.territoryHover == null ) return; //mouse can't possible be on a territory so not interresting

        Territory target = this.territoryHover.getTerritory();
        if( target.getOwner() == null ) target.setOwner(this.roundManager.getCurrentPlayer());
        else if ( target.getOwner().equals(this.roundManager.getCurrentPlayer()) ) target.increaseArmy(1);
    }
}
