package pk.risiko.ui.elements;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.GameState;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.Territory;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.util.RoundManager;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * GameMapUI is the Graphical representation of the GameMap
 * and does delegate the events of the input and drawing to
 * the Territories
 *
 * @author Raphael
 * @version 10.01.2016
 */
public class GameMapUI extends UIElement {

    public static final int GAME_MAP_WIDTH  = 1250;
    public static final int GAME_MAP_HEIGHT = 650;

    private final RoundManager roundManager;
    private final GameMap gameMap;
    private final Set<Connection> connections = new LinkedHashSet<>();
    private final Area waterArea;
    private final GamePanel gamePanel;

    private TerritoryHover territoryHover;
    private boolean isCurrentlyPlaying = true;


    public GameMapUI(GameMap map,RoundManager manager, GamePanel panel) {
        super(new Rectangle(0,0,GAME_MAP_WIDTH,GAME_MAP_HEIGHT));
        this.gameMap = map;
        this.roundManager = manager;
        this.waterArea = new Area(this.getElementShape());
        this.gamePanel=panel;

        final Area territoryArea = new Area();

        //Generate Connection between Territories for visualization
        this.gameMap.getTerritories().forEach(territory -> {
            territoryArea.add((Area) territory.getElementShape());
            territory.getNeighbours().forEach(neighbour -> {
                Connection con = new Connection(territory, neighbour);
                if (!this.connections.contains(con))
                    this.connections.add(con);
            });
        });

        this.waterArea.subtract(territoryArea);
    }

    @Override
    public void paint(Graphics2D g) {
        Graphics2D g2dCon = (Graphics2D) g.create(); //Clone for clipping
        g2dCon.clip(this.waterArea); //clip area to water only
        this.connections.forEach(connection -> connection.paint(g2dCon));
        g2dCon.dispose(); //get rid of the new graphics object -> not needed anymore

        this.gameMap.getTerritories().forEach(territory -> territory.paint(g));

        if( this.territoryHover != null ) this.territoryHover.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( !this.isCurrentlyPlaying() ) return;

        this.gameMap.getTerritories().forEach(t -> t.mouseClicked(e));
        this.mouseClicked();

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if( !this.isCurrentlyPlaying() ) return;

        this.isMouseIn(e.getX(),e.getY());
        this.gameMap.getTerritories().forEach(t -> t.mouseMoved(e));
    }

    @Override
    public boolean isMouseIn(int x, int y) {
        if( !this.isCurrentlyPlaying() ) return false;

        for(Territory t:this.gameMap.getTerritories())
            if( t.isMouseIn(x,y) ) {
                if( this.territoryHover == null || !this.territoryHover.getTerritory().equals(t) ) {
                    this.territoryHover = new TerritoryHover(t,x + 10 ,y + 10);
                } else if( this.territoryHover != null ) {
                    this.territoryHover.moveTo(x/* + 10*/ ,y/* + 10*/);
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

        /* TODO: Players stored in roundManager - let roundManager handle Player action implementation?*/

        //mouse can't possible be on a territory so not interesting
        if( this.territoryHover == null || !this.isCurrentlyPlaying()) return;

        Territory target = this.territoryHover.getTerritory();

        // SET_UNIT
        if (gamePanel.getCurrentGameState().equals(GameState.SET_UNIT)) {
            if (target.getOwner() == null) {
                target.setOwner(this.roundManager.getCurrentPlayer());
                target.increaseArmy(1);

                roundManager.nextPlayer();
                if (!gameMap.decreaseFreeTerritories()) {
                    /* TODO: replace reinforcement count for player*/
                    roundManager.getCurrentPlayer().setReinforcements(5);
                    roundManager.nextPlayer();
                    roundManager.getCurrentPlayer().setReinforcements(5);


                    gamePanel.changeState(GameState.REINFORCE_UNITS);
                    roundManager.nextPlayer(); // set to Player , not Computer
                }
            }

        }
        System.out.println(gamePanel.getCurrentGameState());
        if (gamePanel.getCurrentGameState().equals(GameState.REINFORCE_UNITS)) {
            Player p = this.roundManager.getCurrentPlayer();
            if (target.getOwner().equals(p))
                if (p.reinforcementPossible())
                    target.increaseArmy(1);
                else
                    roundManager.nextPlayer();
        }
    }

    public boolean isCurrentlyPlaying() {
        return isCurrentlyPlaying;
    }

    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        isCurrentlyPlaying = currentlyPlaying;
    }
}
