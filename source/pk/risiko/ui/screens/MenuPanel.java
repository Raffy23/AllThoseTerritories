package pk.risiko.ui.screens;

import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.geom.Rectangle2D;

/**
 * TODO: Start Menu
 *
 * @author Raphael
 * @version 12.01.2016
 */
public class MenuPanel implements GameScreen {

    private static final String TITLE = "AllThoseTerritories";

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();

    private final GameButton newGame;
    private final GameButton loadGame;
    private final GameButton exitGame;

    private final int windowWidth, windowHeight;

    public MenuPanel(int windowWidth,int windowHeight) {
        this.newGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,180,75,20),"New Game");
        this.loadGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,240,75,20),"Load Game");
        this.exitGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,280,75,20),"Exit");

        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;

        this.dispatcher.registerListener(this.newGame);
        this.dispatcher.registerListener(this.loadGame);
        this.dispatcher.registerListener(this.exitGame);
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(Color.GRAY);
        g.fillRect(0,0,windowWidth,windowHeight);

        final Font oldFont = g.getFont();
        g.setFont(oldFont.deriveFont(64.5f));

        final int fontHeight = g.getFontMetrics().getHeight();
        final int fontWidth  = g.getFontMetrics().stringWidth(TITLE);
        g.setColor(Color.WHITE);
        g.drawString(TITLE,windowWidth/2-fontWidth/2,fontHeight + 15);

        g.setFont(oldFont);

        this.newGame.paint(g);
        this.loadGame.paint(g);
        this.exitGame.paint(g);
    }

    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return dispatcher;
    }

    @Override
    public KeyAdapter getKeyAdapter() {
        return null;
    }

    public GameButton getNewGame() {
        return newGame;
    }

    public GameButton getLoadGame() {
        return loadGame;
    }

    public GameButton getExitGame() {
        return exitGame;
    }
}
