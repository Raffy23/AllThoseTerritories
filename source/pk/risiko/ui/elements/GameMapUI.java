package pk.risiko.ui.elements;

import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.PlayerAI;
import pk.risiko.pojo.Territory;
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
 * the Territories.
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 15.01.2016
 */
public class GameMapUI extends UIElement {

    /** The default Map width (as given in the task description) **/
    public static final int GAME_MAP_WIDTH  = 1250;
    /** The default Map height (as given in the task description) **/
    public static final int GAME_MAP_HEIGHT = 650;

    /* TODO: is the RoundManager really needed here? */
    /** The RoundManager which does handle all the Round specific stuff **/
    private final RoundManager roundManager;
    /** The actual Data of the GameMap is stored in the GameMap Class **/
    private final GameMap gameMap;
    /** A list of all connections which should be drawn between territories (without duplicates) **/
    private final Set<Connection> connections = new LinkedHashSet<>();
    /** The area in which no territory is present (so the water) **/
    private final Area waterArea;

    /** The little Hover Element which is displayed on the Territory Hover **/
    private TerritoryHover territoryHover;
    /** A flag which is false if the game is paused otherwise its true **/
    private boolean isCurrentlyPlaying = true;

    private boolean isAIPlaying = false;

    /**
     * To be able for the GameMapUI to function corretly it does need to known what it
     * should paint and therefore needs the GameMap
     *
     * @param map the current GameMap which should be drawn
     * @param manager (Roundmanger, do i really need this?)
     */
    public GameMapUI(GameMap map,RoundManager manager) {
        super(new Rectangle(0,0,GAME_MAP_WIDTH,GAME_MAP_HEIGHT));
        this.gameMap = map;
        this.roundManager = manager;
        this.waterArea = new Area(this.getElementShape());

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

        //generate water area for clipping
        this.waterArea.subtract(territoryArea);
    }

    /**
     * @see UIElement
     */
    @Override
    public void paint(Graphics2D g) {
        Graphics2D g2dCon = (Graphics2D) g.create(); //Clone for clipping
        g2dCon.clip(this.waterArea); //clip area to water only
        this.connections.forEach(connection -> connection.paint(g2dCon));
        g2dCon.dispose(); //get rid of the new graphics object -> not needed anymore

        //paint the Territories (GameMap)
        this.gameMap.getTerritories().forEach(territory -> territory.paint(g));
        this.gameMap.getTerritories().forEach(territory -> territory.paintTopComponents(g));

        //if present paint the TerritoryHover
        if( this.territoryHover != null ) this.territoryHover.paint(g);
    }

    /**
     * @see UIElement
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        //if not playing why should events be processed
        if( !this.isCurrentlyPlaying() || this.isAIPlaying ) return;

        //deligate all the events to the territories
        this.gameMap.getTerritories().forEach(t -> t.mouseClicked(e));
        this.mouseClicked();
    }

    /**
     * @see UIElement
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        //if not playing why should events be processed
        if( !this.isCurrentlyPlaying() ) return;

        //do handle own events ...
        this.isMouseIn(e.getX(),e.getY());
        //deligate all the events to the territories
        this.gameMap.getTerritories().forEach(t -> t.mouseMoved(e));
    }

    /**
     * This function does create and move the Territory Hover which is
     * only displayed if a territory is hovered
     *
     * @see UIElement
     */
    @Override
    public boolean isMouseIn(int x, int y) {
        //if not playing why should events be processed
        //if( !this.isCurrentlyPlaying() ) return false;

        //search for hovered Territory
        for(Territory t:this.gameMap.getTerritories())
            if( t.isMouseIn(x,y) ) { //found one & check if it's not allready displayed
                if( this.territoryHover == null || !this.territoryHover.getTerritory().equals(t) ) {
                    //does not exitst or is from another territory, crete a new one
                    this.territoryHover = new TerritoryHover(t,x + 15 ,y + 15);
                } else if( this.territoryHover != null ) {
                    //if the hover is allready displayed just move it to the new position
                    this.territoryHover.moveTo(x + 15 ,y + 15);
                }
                return true;
            }

        //If we did not find anything we do delete the current Hover element
        this.territoryHover = null;
        return false;
    }

    /**
     * This function does trigger the function #{handleClickEvent} if any
     * territory is hovered and clicked and the game is currently not paused
     *
     * @see UIElement
     */
    @Override
    public void mouseClicked() {
        //mouse can't possible be on a territory so not interesting
        if( this.territoryHover == null || !this.isCurrentlyPlaying()) return;

        //process some AI and Player stuff in the RoundManager
        this.handleClickEvent();
    }

    /**
     * Here all the Stuff is deligated to the RoundManager which should
     * know what should happen ...
     *
     * @see RoundManager
     */
    private void handleClickEvent() {
        if (!(roundManager.getCurrentPlayer() instanceof PlayerAI))
            roundManager.manageActions(this.territoryHover.getTerritory());
    }

    /**
     * @return true if the Game is currently running, false if paused
     */
    public boolean isCurrentlyPlaying() {
        return isCurrentlyPlaying;
    }

    /**
     * @param currentlyPlaying changes to game to running (true) or paused (false)
     */
    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        isCurrentlyPlaying = currentlyPlaying;
    }

    public boolean isAIPlaying() {
        return isAIPlaying;
    }

    public void setAIPlaying(boolean AIPlaying) {
        isAIPlaying = AIPlaying;
    }
}
