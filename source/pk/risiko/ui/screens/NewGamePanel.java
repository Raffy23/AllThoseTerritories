package pk.risiko.ui.screens;

import pk.risiko.dao.MapFileReader;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.PlayerAI;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.CyclicList;
import pk.risiko.util.FontLoader;
import pk.risiko.util.SettingsProvider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 *
 * @author Raphael
 * @version 19.01.2016
 */
public class NewGamePanel implements GameScreen {

    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final int PREVIEW_MAP_WIDTH = GameWindow.WINDOW_SIZE_WIDTH/3;
    private static final int PREVIEW_MAP_HEIGHT = GameWindow.WINDOW_SIZE_HEIGHT/3;

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();
    private final MapFileReader mapFileReader;

    private final GameButton beginGameBtn,nextMap,prevMap;
    private final GameButton player1,player2,player3,player4;
    private final CyclicList<String> mapFiles = new CyclicList<>();
    private final List<Player> players = new ArrayList<>();

    private GameMap currentSelctedMap;
    private BufferedImage gameMapPreview;
    private final Font headlineFont;

    public NewGamePanel(MapFileReader mapFileReader) {
        this.mapFileReader = mapFileReader;
        this.mapFiles.addAll(mapFileReader.getAvailableMapFiles());
        /* setup ui: */
        this.beginGameBtn = new GameButton(new Rectangle2D.Double(GameWindow.WINDOW_SIZE_WIDTH-95
                                                                  ,GameWindow.WINDOW_SIZE_HEIGHT-75,75,20),"Start Game");

        this.nextMap = new GameButton(new Rectangle2D.Double(GameWindow.WINDOW_SIZE_WIDTH-95
                                                            ,225+PREVIEW_MAP_HEIGHT,20,20),">");
        this.prevMap = new GameButton(new Rectangle2D.Double(GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH-75
                                                            ,225+PREVIEW_MAP_HEIGHT,20,20),"<");

        this.player1 = new GameButton(new Rectangle2D.Double(235,230,90,20)," Human Player ");
        this.player2 = new GameButton(new Rectangle2D.Double(235,290,90,20)," KI ");
        this.player3 = new GameButton(new Rectangle2D.Double(235,350,90,20)," Not Playing ");
        this.player4 = new GameButton(new Rectangle2D.Double(235,410,90,20)," Not Playing ");

        /* load font */
        this.headlineFont = FontLoader.loadFont(MainMenuPanel.HEADLINE_FONT,MainMenuPanel.HEADLINE_FONT_SIZE);

        /* register event listener */
        this.dispatcher.registerListener(beginGameBtn);
        this.dispatcher.registerListener(nextMap);
        this.dispatcher.registerListener(prevMap);

        this.nextMap.setListener((next) -> this.loadMap(this.mapFiles.next()));
        this.nextMap.setListener((prev) -> this.loadMap(this.mapFiles.prev()));

        /* load default map */
        this.loadMap(SettingsProvider.getInstance().getDefaultMapName());
    }

    private void loadMap(String name) {
        this.currentSelctedMap = mapFileReader.readMap(name);
        this.drawMapPreview();
    }

    private void drawMapPreview() {
        this.gameMapPreview = new BufferedImage(GameWindow.WINDOW_SIZE_WIDTH
                                               ,GameWindow.WINDOW_SIZE_HEIGHT
                                               ,BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = this.gameMapPreview.createGraphics();
        this.currentSelctedMap.getTerritories().forEach(t -> t.paint(g2d));
        g2d.dispose();

    }

    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return this.dispatcher;
    }

    @Override
    public KeyAdapter getKeyAdapter() {
        return null;
    }

    @Override
    public void paint(Graphics2D g) {

        final Font oldFont = g.getFont();
        g.setFont(headlineFont);

        int fontHeight = g.getFontMetrics().getHeight();
        int fontWidth  = g.getFontMetrics().stringWidth(MainMenuPanel.TITLE);
        g.setColor(MainMenuPanel.HEADLINE_COLOR);
        g.drawString(MainMenuPanel.TITLE,GameWindow.WINDOW_SIZE_WIDTH/2-fontWidth/2,fontHeight + 5);

        g.setFont(headlineFont.deriveFont(31f));
        g.drawString("Start a new Game: ",75,fontHeight+g.getFontMetrics().getHeight());

        g.setColor(DEFAULT_BACKGROUND_COLOR);
        g.fillRect(GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH-76
                  ,220
                  ,PREVIEW_MAP_WIDTH+1
                  ,PREVIEW_MAP_HEIGHT);
        g.drawImage(this.gameMapPreview
                   ,GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH-75
                   ,220
                   ,GameWindow.WINDOW_SIZE_WIDTH-75
                   ,220+PREVIEW_MAP_HEIGHT
                   ,0,0,GameWindow.WINDOW_SIZE_WIDTH,GameWindow.WINDOW_SIZE_HEIGHT,null);

        g.setFont(oldFont.deriveFont(28f));
        g.setColor(Color.BLACK);
        fontWidth  = g.getFontMetrics().stringWidth(this.currentSelctedMap.getMapName());
        g.drawString(this.currentSelctedMap.getMapName()
                    ,GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH/2-76-fontWidth/2
                    ,225+PREVIEW_MAP_HEIGHT+g.getFontMetrics().getHeight()/2);



        g.drawString("Player1",115,250);
        g.drawString("Player2",115,310);
        g.drawString("Player3",115,370);
        g.drawString("Player4",115,430);

        g.setFont(oldFont);
        this.player1.paint(g);
        this.player2.paint(g);
        this.player3.paint(g);
        this.player4.paint(g);
        this.prevMap.paint(g);
        this.nextMap.paint(g);
        this.beginGameBtn.paint(g);
    }

    public GameMap getSelectedGameMap() {
        return this.currentSelctedMap;
    }

    public List<Player> getPlayers() {
        /* todo: construct players here */
        this.players.clear();

         /* Todo: color chooser or something like that */
        players.add(new Player("Spieler",new Color(10,10,200,200),currentSelctedMap));
        players.add(new PlayerAI("Computer",new Color(200,10,10,200),currentSelctedMap));

        return this.players;
    }

    public void registerNewGameListener(MouseClickedListener l) {
        this.beginGameBtn.setListener(l);
    }
}
