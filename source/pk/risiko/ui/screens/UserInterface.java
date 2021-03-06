package pk.risiko.ui.screens;

import pk.risiko.pojo.GameState;
import pk.risiko.pojo.MouseState;
import pk.risiko.pojo.PlayerAI;
import pk.risiko.pojo.Territory;
import pk.risiko.ui.Drawable;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.MouseEventHandler;
import pk.risiko.ui.elements.DefaultDesigns;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.elements.InGameMenu;
import pk.risiko.ui.elements.WinLoseDialog;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.util.ImageStore;
import pk.risiko.util.SettingsProvider;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class handles all the Interaction with non MapStuff in the Game
 * and holds all the UserInterface-relevant objects
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 08.01.2016
 */
public class UserInterface extends MouseAdapter implements Drawable, MouseClickedListener {

    /**
     * The default BackgroundColor of the Interface
     */
    private static final Color BACKGROUND_COLOR = new Color(0.15f,0.15f,0.15f,0.65f);

    /**
     * The default TextColor
     */
    private static final Color TEXT_COLOR = DefaultDesigns.TEXT_COLOR;

    /**
     * This is the height of the top bar
     */
    public static final int BAR_HEIGHT = 26;

    /**
     * The GamePanel, that holds the UserInterface
     */
    private final GamePanel master;

    /**
     * buttons that can be clicked
     * nextButton is only for human players, that are done with their turn
     * menuButton is to show the InGameMenu
     */
    private final GameButton nextButton, menuButton;

    /**
     * This is the InGameMenu, that can be opened while the game is running
     */
    private final InGameMenu menu;

    /**
     * The winLoseDialog is shown, when the game is over
     */
    private final WinLoseDialog winLoseDialog;

    /**
     * In order to create the UserInterface, the GamePanel that holds it, must be known
     * @param masterPanel is the Panel that holds the UserInterface
     */
    public UserInterface(GamePanel masterPanel) {
        this.master = masterPanel;

        this.nextButton = new GameButton(new Ellipse2D.Double(GameWindow.WINDOW_WIDTH -75,GameWindow.WINDOW_HEIGHT -100,65,65),">>");
        this.nextButton.setFontSize(24);
        this.menuButton = new GameButton(new Rectangle2D.Double(GameWindow.WINDOW_WIDTH -75,2,65,20),"MENU");

        this.nextButton.setListener(this);
        this.menuButton.setListener(this);

        this.menu = new InGameMenu();
        this.winLoseDialog=new WinLoseDialog();
    }

    @Override
    public void paint(Graphics2D g2d) {

        if (!this.menu.isActive())
            master.changeState(GameState.HIDE_MENU);
        if (this.master.isAIPlaying())
            this.nextButton.setActive(false);
        else
            this.nextButton.setActive(true);

        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0,0, GameWindow.WINDOW_WIDTH,BAR_HEIGHT);
        g2d.setColor(Color.WHITE);
        g2d.drawLine(0,BAR_HEIGHT,GameWindow.WINDOW_WIDTH,BAR_HEIGHT);

        this.menuButton.paint(g2d);
        this.nextButton.paint(g2d);

        //choose image:
        if( this.nextButton.getMouseState() == MouseState.NORMAL ) {
            switch (this.master.getGameState()) {
                case ATTACK_OR_MOVE_UNIT:
                    break;
                case REINFORCE_UNITS:
                    break;
                case SET_UNIT:
                    break;
            }
        }

        final int headStatusLength = g2d.getFontMetrics().stringWidth("Player: ");
        g2d.fillRect(5,4,2,BAR_HEIGHT-8);
        g2d.drawString("Player: ",15,17);
        g2d.setColor(this.master.getCurrentPlayer().getColor());
        g2d.fillRect(headStatusLength+21,BAR_HEIGHT/2-6,11,11);
        g2d.setColor(TEXT_COLOR);
        g2d.drawRect(headStatusLength+21,BAR_HEIGHT/2-6,11,11);
        g2d.drawString(this.master.getCurrentPlayer().getName(),headStatusLength + 35,17);

        g2d.fillRect(145,4,2,BAR_HEIGHT-8);
        g2d.drawString("Round: " + this.master.getRounds() ,160,17);

        g2d.fillRect(225,4,2,BAR_HEIGHT-8);
        g2d.drawString("Reinforcements: " +
                       (this.master.getCurrentPlayer().getReinforcements()==0?
                               "none":
                               this.master.getCurrentPlayer().getReinforcements())
                       ,235,17);

        g2d.fillRect(365,4,2,BAR_HEIGHT-8);
        final int statusStringLength = g2d.getFontMetrics().stringWidth(this.getGameStateString());
        g2d.drawString(this.getGameStateString(),(375+545)/2-statusStringLength/2,17);
        g2d.fillRect(555,4,2,BAR_HEIGHT-8);
        g2d.drawString(this.master.getLastAction(),565,17);


        if( this.menu.isActive() )
            this.menu.paint(g2d);
        if (this.winLoseDialog.isActive())
            this.winLoseDialog.paint(g2d);
    }

    /**
     * @return the information string, that is shown in the top bar of the UI
     */
    private String getGameStateString() {
        switch( this.master.getGameState() ) {
            case ATTACK_OR_MOVE_UNIT: return "Attack or move Units";
            case REINFORCE_UNITS: return "Reinforce your units!";
            case SET_UNIT: return "Deploy your units to conquer";
        }
        return "<NONE>";
    }

    /**
     * If a MouseClick happened on the UserInterface this method is executed
     * @param what defines the object, that was clicked
     */
    @Override
    public void mouseClicked(MouseEventHandler what) {
        if( what.equals(this.nextButton) && !this.master.isAIPlaying() &&this.master.getGameState()!=GameState.SET_UNIT&&this.master.getGameState()!=GameState.REINFORCE_UNITS) {
            this.master.changeState(GameState.NEXT_ROUND);
        } else if( what.equals(this.menuButton) ) {
            if( !this.menu.isActive() ) { this.master.changeState(GameState.SHOW_MENU); this.menu.show(); }
            else { this.master.changeState(GameState.HIDE_MENU); this.menu.hide(); }
        }
    }

    /**
     * this method delegates
     * the mouseMoved event to all the objects that need to process it
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        checkAttackMoveHover(e.getX(), e.getY());

        this.menuButton.mouseMoved(e);
        this.nextButton.mouseMoved(e);
        this.menu.mouseMoved(e);
        this.winLoseDialog.mouseMoved(e);
    }

    /**
     * This method sets an Image for the nextRound button,
     * if the a territory is hovered an it can be attacked from the selected territory
     * or if this territory could theoretically receive units from the selected territory
     * @param x is the x coordinate of the Click
     * @param y is the y coordinate
     */
    private void checkAttackMoveHover(int x, int y) {
        if (master.getGameState()!=GameState.ATTACK_OR_MOVE_UNIT||master.getCurrentPlayer().getCurrentActiveTerritory()==null) {
            this.nextButton.setImage(null);
            return;
        }
        for(Territory t:master.getCurrentPlayer().getCurrentActiveTerritory().getNeighbours())
            if( t.isMouseIn(x,y-BAR_HEIGHT) ) {
                if (t.getOwner()==master.getCurrentPlayer()) {
                    this.nextButton.setImage(ImageStore.getInstance().getImage("Shield"));
                }
                else
                    this.nextButton.setImage(ImageStore.getInstance().getImage("Swords"));
                return;
            }
        this.nextButton.setImage(null);
        return;
    }

    /**
     * this method delegates
     * the click event to all the objects that need to process it
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.menuButton.mouseClicked(e);
        this.nextButton.mouseClicked(e);
        this.menu.mouseClicked(e);
        this.menu.mouseClicked(e);
        this.winLoseDialog.mouseClicked(e);
    }

    /**
     * @return the InGameMenu of the UserInterface
     */
    public InGameMenu getMenu() {
        return this.menu;
    }

    /**
     * @return the WinLoseDialog of the UserInterface
     */
    public WinLoseDialog getWinLoseDialog() {
        return winLoseDialog;
    }
}
