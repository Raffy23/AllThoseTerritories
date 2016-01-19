package pk.risiko.ui.screens;

import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.FontLoader;

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
public class MainMenuPanel implements GameScreen {

    public static final String TITLE = "All those Territories";
    public static final String HEADLINE_FONT = "aniron/anicb___.ttf";
    public static final float HEADLINE_FONT_SIZE = 64.5f;
    public static final Color HEADLINE_COLOR = new Color(14, 7, 4);

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();

    private final GameButton newGame;
    private final GameButton loadGame;
    private final GameButton exitGame;

    private final int windowWidth, windowHeight;
    private final Font headlineFont;

    public MainMenuPanel(int windowWidth, int windowHeight) {
        this.newGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,windowHeight/3,75,20),"New Game");
        this.loadGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,windowHeight/3+40,75,20),"Load Game");
        this.exitGame = new GameButton(new Rectangle2D.Double(windowWidth/2-75,windowHeight/3+80,75,20),"Exit");

        this.windowHeight = windowHeight;
        this.windowWidth = windowWidth;

        this.dispatcher.registerListener(this.newGame);
        this.dispatcher.registerListener(this.loadGame);
        this.dispatcher.registerListener(this.exitGame);

        this.headlineFont = FontLoader.loadFont(HEADLINE_FONT,HEADLINE_FONT_SIZE);
    }

    @Override
    public void paint(Graphics2D g) {
        final Font oldFont = g.getFont();
        g.setFont(this.headlineFont);

        final int fontHeight = g.getFontMetrics().getHeight();
        final int fontWidth  = g.getFontMetrics().stringWidth(TITLE);
        g.setColor(HEADLINE_COLOR);
        g.drawString(TITLE,windowWidth/2-fontWidth/2,fontHeight + 5);

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
