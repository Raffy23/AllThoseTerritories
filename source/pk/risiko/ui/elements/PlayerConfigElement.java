package pk.risiko.ui.elements;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * This Graphical Element does contain three Buttons which are displaying
 * all changeable parameters of the Player class (Color, Name and Type)
 *
 * The Type can be one of #{PlayerType} elements
 *
 * @author Raphael Ludwig
 * @version 19.01.2016
 */
public class PlayerConfigElement extends UIElement {

    /** The default Background color of the Buttons **/
    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);

    /**
     * This Type does express one of the following choises
     * the user has take for the specific player
     **/
    public enum PlayerType {
        /** This player does not play at all **/
        NOT_PLAYING,
        /** a human should play **/
        HUMAN,
        /** one of the AIs should play this player **/
        AI
    }

    /** This Button lets the User choose another color for the Player **/
    private final GameButton colorChooser;
    /** This Button lets the User choose another name for the Player **/
    private final GameButton playerNameBtn;
    /** This Button switches the PlayerType for the current Player **/
    private final GameButton playerTypeBtn;

    /** The current Playername **/
    private String playerName;
    /** The current Playercolor **/
    private Color playerColor;
    /** The current Playertype **/
    private PlayerType type;

    /**
     * To construct the PlayerConfig Element following parametes about the player must
     * be known:
     * @param x the x coordinate on which the elements hould be displayed
     * @param y the y coordinate on which the elements hould be displayed
     * @param name the default name of the player which can be changed
     * @param c the default color of the player which can also be changed
     */
    public PlayerConfigElement(int x,int y,String name,Color c) {
        super(new Area()); //This Element does not have a area ...
        this.playerName = name;
        this.playerColor = c;
        this.type = PlayerType.NOT_PLAYING;

        //generate the buttons
        this.playerTypeBtn = new GameButton(new Rectangle2D.Double(x+245,y+4,90,20),this.type.name());
        this.colorChooser = new GameButton(new Rectangle2D.Double(x,y+4,20,20),"");
        this.playerNameBtn = new GameButton(new Rectangle2D.Double(x+30,y,200,28),name);

        //set the colors & other parameters:
        this.colorChooser.setBackgroundColor(c);
        this.playerNameBtn.setBorderColor(DEFAULT_BACKGROUND_COLOR);
        this.playerNameBtn.setFontSize(24);

        //register this as listener for the button actions
        this.playerTypeBtn.setListener((btn) -> this.changePlayerType());
        this.colorChooser.setListener((btn) -> this.changeColor());
        this.playerNameBtn.setListener((btn) -> this.changeName());
    }

    /**
     * This function does change the PlayerType value by iterating
     * through the possibilities
     */
    private void changePlayerType() {
        switch( this.type ) {
            case NOT_PLAYING: this.type = PlayerType.HUMAN; break;
            case HUMAN: this.type = PlayerType.AI; break;
            case AI: this.type = PlayerType.NOT_PLAYING; break;
        }

        this.playerTypeBtn.setText(this.type.name());
    }

    /**
     * This Method does create a Text Input dialog for the user
     * to change his name
     *
     * @see JOptionPane
     */
    private void changeName() {
        String newName = JOptionPane.showInputDialog("Type in a new Name for the Player:");
        if( newName != null ) {
            this.playerName = newName;
            this.playerNameBtn.setText(newName);
        }
    }

    /**
     * This Method does create a ColorChooser dialog so
     * the user can change the color
     *
     * @see JColorChooser
     */
    private void changeColor() {
        Color newColor = JColorChooser.showDialog(null,"Choose a new Color for the Player:",this.playerColor);
        if( newColor != null ) {
            this.playerColor = new Color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),200);
            this.colorChooser.setBackgroundColor(this.playerColor);
        }
    }

    /**
     * @return the Name of the Player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @return the Color of the Player
     */
    public Color getPlayerColor() {
        return playerColor;
    }

    /**
     * @return the current Type
     * @see PlayerType
     */
    public PlayerType getType() {
        return type;
    }

    /**
     * This Method dose delegate all events to the Buttons
     * @see UIElement
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        colorChooser.mouseMoved(e);
        playerNameBtn.mouseMoved(e);
        playerTypeBtn.mouseMoved(e);
    }

    /**
     * This Method dose delegate all events to the Buttons
     * @see UIElement
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        colorChooser.mouseClicked(e);
        playerNameBtn.mouseClicked(e);
        playerTypeBtn.mouseClicked(e);
    }

    @Override
    public void paint(Graphics2D g) {
        colorChooser.paint(g);
        playerTypeBtn.paint(g);
        playerNameBtn.paint(g);
    }

    /**
     * This Method dose delegate all events to the Buttons
     * @see UIElement
     */
    @Override
    public boolean isMouseIn(int x, int y) {
        colorChooser.isMouseIn(x,y);
        playerNameBtn.isMouseIn(x,y);
        playerTypeBtn.isMouseIn(x,y);

        return false;
    }

    /**
     * This Method dose delegate all events to the Buttons
     * @see UIElement
     */
    @Override
    public void mouseClicked() {
        colorChooser.mouseClicked();
        playerNameBtn.mouseClicked();
        playerTypeBtn.mouseClicked();
    }
}
