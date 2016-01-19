package pk.risiko.ui.elements;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

/**
 * Created by
 *
 * @author Raphael
 * @version 19.01.2016
 */
public class PlayerConfigElement extends UIElement {

    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);

    public enum PlayerType {
        NOT_PLAYING,HUMAN,AI
    }

    private final GameButton colorChooser;
    private final GameButton playerNameBtn;
    private final GameButton playerTypeBtn;

    private String playerName;
    private Color playerColor;
    private PlayerType type;

    public PlayerConfigElement(int x,int y,String name,Color c) {
        super(new Area());
        this.playerName = name;
        this.playerColor = c;
        this.type = PlayerType.NOT_PLAYING;

        this.playerTypeBtn = new GameButton(new Rectangle2D.Double(x+245,y+4,90,20),this.type.name());
        this.colorChooser = new GameButton(new Rectangle2D.Double(x,y+4,20,20),"");
        this.playerNameBtn = new GameButton(new Rectangle2D.Double(x+30,y,200,28),name);

        this.colorChooser.setBackgroundColor(c);
        this.playerNameBtn.setBorderColor(DEFAULT_BACKGROUND_COLOR);
        this.playerNameBtn.setFontSize(24);

        this.playerTypeBtn.setListener((btn) -> this.changePlayerType());
        this.colorChooser.setListener((btn) -> this.changeColor());
        this.playerNameBtn.setListener((btn) -> this.changeName());
    }

    private void changePlayerType() {
        switch( this.type ) {
            case NOT_PLAYING: this.type = PlayerType.HUMAN; break;
            case HUMAN: this.type = PlayerType.AI; break;
            case AI: this.type = PlayerType.NOT_PLAYING; break;
        }

        this.playerTypeBtn.setText(this.type.name());
    }

    private void changeName() {
        String newName = JOptionPane.showInputDialog("Type in a new Name for the Player:");
        if( newName != null ) {
            this.playerName = newName;
            this.playerNameBtn.setText(newName);
        }
    }

    private void changeColor() {
        Color newColor = JColorChooser.showDialog(null,"Choose a new Color for the Player:",this.playerColor);
        if( newColor != null ) {
            this.playerColor = new Color(newColor.getRed(),newColor.getGreen(),newColor.getBlue(),200);
            this.colorChooser.setBackgroundColor(this.playerColor);
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public PlayerType getType() {
        return type;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        colorChooser.mouseMoved(e);
        playerNameBtn.mouseMoved(e);
        playerTypeBtn.mouseMoved(e);
    }

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

    @Override
    public boolean isMouseIn(int x, int y) {
        colorChooser.isMouseIn(x,y);
        playerNameBtn.isMouseIn(x,y);
        playerTypeBtn.isMouseIn(x,y);

        return false;
    }

    @Override
    public void mouseClicked() {
        colorChooser.mouseClicked();
        playerNameBtn.mouseClicked();
        playerTypeBtn.mouseClicked();
    }
}
