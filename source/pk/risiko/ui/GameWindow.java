package pk.risiko.ui;

import pk.risiko.ui.elements.GameMapUI;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.ui.screens.UserInterface;
import pk.risiko.util.GameScreenManager;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * GameWindow represents the Game Window of the Game.
 * It is responsible to draw all changes which occur in
 * the models or other UI Elements.
 *
 * @author Raphael Ludwig
 * @version 27.12.2015
 */
public class GameWindow extends JFrame {

    //TODO: Should be max of game window + ui
    public static final int WINDOW_SIZE_WIDTH = GameMapUI.GAME_MAP_WIDTH;
    public static final int WINDOW_SIZE_HEIGHT = GameMapUI.GAME_MAP_HEIGHT + UserInterface.BAR_HEIGHT;
    private static final Color BACKGROUND_COLOR = new Color(58, 164, 255);

    private final PanelArea rootPanel;
    private final GameScreenManager gameScreenManager;
    private final WindowPainter painterTimer;

    private BufferedImage background;
    private BufferedImage icon;

    private SwingMouseEventDispatcher dispatcher;

    private int x;
    private int y;

    // PanelArea describes the area of the window, where the different panels (GamePanel, NewGamePanel, MainMenuPanel) are placed
    private class PanelArea extends JPanel {

        public PanelArea() {
            this.setBounds(0,0, WINDOW_SIZE_WIDTH, WINDOW_SIZE_HEIGHT);
        }

        @Override
        public void paint(Graphics g) {
            if( !(g instanceof  Graphics2D) ) throw new RuntimeException("Graphics Object can't handle 2D Areas!");
            Graphics2D g2d = (Graphics2D) g;

            //Enables antialiasing for drawing
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if( GameWindow.this.background == null ) {
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, GameWindow.this.getWidth(), GameWindow.this.getHeight());
            } else {
                g.drawImage(GameWindow.this.background,0,0,GameWindow.this.getWidth(),GameWindow.this.getHeight(),null);
            }

            if( GameWindow.this.gameScreenManager.getActiveScreen() != null )
                GameWindow.this.gameScreenManager.getActiveScreen().paint(g2d);
            else {
                g.setColor(new Color(75,75,75));
                g.fillRect(0,0,GameWindow.WINDOW_SIZE_WIDTH,GameWindow.WINDOW_SIZE_HEIGHT);
                g.setColor(new Color(240,240,240));
                g.drawString("Still waiting for something useful to display ..."
                            ,GameWindow.WINDOW_SIZE_WIDTH/2-g.getFontMetrics().stringWidth("Still waiting for something useful to display ...")/2
                            ,GameWindow.WINDOW_SIZE_HEIGHT/2);
            }

            /*
            g.setColor(Color.BLACK);
            g.drawString("X:" + x + " Y:" + y ,0, GameWindow.this.getHeight()-45);
            */
        }
    }

    private class WindowEventUpdater extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            GameWindow.this.updateGraphics();
        }
    }

    /**
     * Constructs a Window with a certain size.
     * (Size is fixed by the Game)
     * #setVisible must be called explicitly to show the window!
     *
     * @param fps The Frames Per Seconds in which the Window does repaint itself (@see WindowPainter)
     */
    public GameWindow(int fps) {
        super("AllThoseTerritories - Risiko");
        //Setup Window:
        this.setSize(GameWindow.WINDOW_SIZE_WIDTH,GameWindow.WINDOW_SIZE_HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        x =(int)((windowSize.getWidth() - WINDOW_SIZE_WIDTH)/2);
        y =(int)((windowSize.getHeight() - WINDOW_SIZE_HEIGHT)/2);
        System.out.println(x);
        System.out.println(y);
        this.setBounds(x,y,WINDOW_SIZE_WIDTH,WINDOW_SIZE_HEIGHT);


        //Setup Manager:
        this.gameScreenManager = new GameScreenManager(this);


        try {
            this.background = ImageIO.read(new File("./assets/background-game-small.jpg"));
            this.icon = ImageIO.read(new File("./assets/risiko.icon.png"));

            this.setIconImage(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Setup Draw Area:
        this.add(this.rootPanel = new PanelArea());
        this.rootPanel.addMouseListener(new WindowEventUpdater());

        this.rootPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }
        });

        //Setup async Tasks:
        this.painterTimer = new WindowPainter(this,fps);
    }

    public GameScreenManager getGameScreenManager() {
        return this.gameScreenManager;
    }

    public void updateGraphics() {
        this.rootPanel.invalidate();
        this.invalidate();
        this.repaint();
    }

    public void setSwingEventDispatcher(SwingMouseEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.rootPanel.addMouseListener(dispatcher);
        this.rootPanel.addMouseMotionListener(dispatcher);
    }

    public void registerKeyAdapter(KeyAdapter keyAdapter) {
        this.addKeyListener(keyAdapter);
    }

    public void removeMouseEventListener() {
        this.rootPanel.removeMouseMotionListener(this.dispatcher);
        this.rootPanel.removeMouseListener(this.dispatcher);
    }

    public void unregisterKeyAdapter(KeyAdapter keyAdapter) {
        this.removeKeyListener(keyAdapter);
    }

    @Override
    public void dispose() {
        this.painterTimer.cancel();
        super.dispose();
    }

}
