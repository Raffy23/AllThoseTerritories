package pk.risiko.ui;

import pk.risiko.ui.listener.MouseEventListener;
import pk.risiko.util.GameScreenManager;
import pk.risiko.util.MouseEventTranslator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * GameWindow represents the Game Window of the Game.
 * It is responsible to draw all changes which occur in
 * the models or other UI Elements.
 * <br/>
 * <b>TODO: draw stuff in other class JPanel because we can draw into the Taskbar ... lol </b>
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameWindow extends JFrame {

    //TODO: Should be max of game window + ui
    public static final int WINDOW_SIZE_X = 1250;
    public static final int WINDOW_SIZE_Y = 650;
    private static final Color BACKGROUND_COLOR = new Color(58, 164, 255);

    private final GameArea rootPanel;
    private final GameScreenManager gameScreenManager;

    private MouseEventTranslator mouseEvents;

    private int x;
    private int y;

    private class GameArea extends JPanel {
        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            //Enables antialiasing for drawing
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0,0,GameWindow.this.getWidth(),GameWindow.this.getHeight());

            GameWindow.this.gameScreenManager.getActiveScreen().paint(g);

            g.setColor(Color.MAGENTA);
            g.drawString("X:" + x + " Y:" + y ,0, GameWindow.this.getHeight()-45);
        }
    }

    /**
     * Constructs a Window with a certain size.
     * (Size is fixed by the Game) <br/>
     * #setVisible(true); must be called explicitly to show the window!
     */
    public GameWindow() {
        super("AllThoseTerritories - Risiko");
        //Setup Window:
        this.setSize(GameWindow.WINDOW_SIZE_X,GameWindow.WINDOW_SIZE_Y);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        //Setup Manager:
        this.gameScreenManager = new GameScreenManager(this);

        //Setup Draw Area:
        this.add(this.rootPanel = new GameArea());

        this.rootPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                updateGraphics();
            }
        });
    }

    public GameScreenManager getGameScreenManager() {
        return this.gameScreenManager;
    }

    public void updateGraphics() {
        this.rootPanel.invalidate();
        this.invalidate();
        this.repaint();
    }

    public void setMouseEventListener(MouseEventListener mouse) {
        this.mouseEvents = new MouseEventTranslator(this,mouse);
        this.rootPanel.addMouseListener(mouseEvents);
        this.rootPanel.addMouseMotionListener(mouseEvents);
    }

    public void registerKeyAdapter(KeyAdapter keyAdapter) {
        this.addKeyListener(keyAdapter);
    }

    public void removeMouseEventListener() {
        this.rootPanel.removeMouseMotionListener(this.mouseEvents);
        this.rootPanel.removeMouseListener(this.mouseEvents);
    }

    public void unregisterKeyAdapter(KeyAdapter keyAdapter) {
        this.removeKeyListener(keyAdapter);
    }
}
