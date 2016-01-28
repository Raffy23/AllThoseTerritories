package pk.risiko.ui.screens;

import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.FontLoader;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * This class represents the main screen which is viewed to the user when the .exe is launched
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
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

    private static final int BUTTON_WIDTH=75*2;
    private static final int BUTTON_HEIGHT=20*2;
    private static final int BUTTON_GAP=20;


    public MainMenuPanel(int windowWidth, int windowHeight) {
        int offset=0;
        this.newGame = new GameButton(new Rectangle2D.Double(windowWidth/2- BUTTON_WIDTH /2
                                                            ,windowHeight/3+ (BUTTON_HEIGHT + BUTTON_GAP)*offset++
                                                            , BUTTON_WIDTH, BUTTON_HEIGHT),"New Game");
        this.loadGame = new GameButton(new Rectangle2D.Double(windowWidth/2- BUTTON_WIDTH /2
                                                             ,windowHeight/3+(BUTTON_HEIGHT + BUTTON_GAP)*offset++
                                                             , BUTTON_WIDTH, BUTTON_HEIGHT),"Load Game");
        this.exitGame = new GameButton(new Rectangle2D.Double(windowWidth/2- BUTTON_WIDTH /2
                                                              ,windowHeight/3+(BUTTON_HEIGHT + BUTTON_GAP)*offset++
                                                              , BUTTON_WIDTH, BUTTON_HEIGHT),"Exit");

        this.newGame.setFontSize(BUTTON_HEIGHT /2);
        this.loadGame.setFontSize(BUTTON_HEIGHT /2);
        this.exitGame.setFontSize(BUTTON_HEIGHT /2);

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
    public void shown() {}

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
