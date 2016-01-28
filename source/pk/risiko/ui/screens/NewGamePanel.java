package pk.risiko.ui.screens;

import pk.risiko.dao.MapFileReader;
import pk.risiko.pojo.GameMap;
import pk.risiko.pojo.Player;
import pk.risiko.pojo.RandomAI;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.elements.PlayerConfigElement;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.CyclicList;
import pk.risiko.util.FontLoader;
import pk.risiko.util.SettingsProvider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the Menue which is shown after the new game
 * button in the MainMenu has been clicked
 *
 * @author Raphael Ludwig
 * @version 19.01.2016
 */
public class NewGamePanel implements GameScreen {

    private static final Color DEFAULT_BACKGROUND_COLOR   = new Color(0.15f,0.15f,0.15f,0.65f);
    private static final int PREVIEW_MAP_WIDTH = GameWindow.WINDOW_SIZE_WIDTH/3;
    private static final int PREVIEW_MAP_HEIGHT = GameWindow.WINDOW_SIZE_HEIGHT/3;

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();
    private final MapFileReader mapFileReader;

    private final GameButton beginGameBtn,nextMap,prevMap;
    private final PlayerConfigElement player1,player2,player3,player4;
    private final CyclicList<String> mapFiles = new CyclicList<>();

    private GameMap currentSelctedMap;
    private BufferedImage gameMapPreview;
    private final Font headlineFont;

    private static final int START_BUTTON_W=75*2;
    private static final int START_BUTTON_H=20*2;
    private static final int MAP_BUTTONS_WH= 20*2;
    private static final int SEGMENT_LEFT= 115;
    private static final int SEGMENT_MIDDLE= GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH-75;
    private static final int SEGMENT_RIGHT=GameWindow.WINDOW_SIZE_WIDTH-95+20;

    public NewGamePanel(MapFileReader mapFileReader) {
        this.mapFileReader = mapFileReader;
        this.mapFiles.addAll(mapFileReader.getAvailableMapFiles());
        this.mapFiles.remove(SettingsProvider.getInstance().getDefaultMapName());
        this.mapFiles.add(0,SettingsProvider.getInstance().getDefaultMapName());

        /* setup ui: */
        this.beginGameBtn = new GameButton(new Rectangle2D.Double(SEGMENT_RIGHT-START_BUTTON_W  //GameWindow.WINDOW_SIZE_WIDTH-95+20-75
                                                            ,225+PREVIEW_MAP_HEIGHT+75,START_BUTTON_W,START_BUTTON_H),"Start Game");


        this.nextMap = new GameButton(new Rectangle2D.Double(SEGMENT_RIGHT-MAP_BUTTONS_WH  //GameWindow.WINDOW_SIZE_WIDTH-95
                                                            ,225+PREVIEW_MAP_HEIGHT,MAP_BUTTONS_WH,MAP_BUTTONS_WH),">");
        this.prevMap = new GameButton(new Rectangle2D.Double(SEGMENT_MIDDLE //GameWindow.WINDOW_SIZE_WIDTH-PREVIEW_MAP_WIDTH-75

                                                            ,225+PREVIEW_MAP_HEIGHT,MAP_BUTTONS_WH,MAP_BUTTONS_WH),"<");
        this.beginGameBtn.setFontSize(START_BUTTON_H/2);
        this.nextMap.setFontSize(MAP_BUTTONS_WH/2);
        this.prevMap.setFontSize(MAP_BUTTONS_WH/2);

        this.player1 = new PlayerConfigElement(115,230,"Player 1",new Color(10,10,200,200));
        this.player2 = new PlayerConfigElement(115,290,"Player 2",new Color(10,200,10,200));
        this.player3 = new PlayerConfigElement(115,350,"Player 3",new Color(200,10,10,200));
        this.player4 = new PlayerConfigElement(115,410,"Player 4",new Color(75,75,25,200));

        /* load font */
        this.headlineFont = FontLoader.loadFont(MainMenuPanel.HEADLINE_FONT,MainMenuPanel.HEADLINE_FONT_SIZE);

        /* register event listener */
        this.dispatcher.registerListener(beginGameBtn);
        this.dispatcher.registerListener(nextMap);
        this.dispatcher.registerListener(prevMap);

        this.dispatcher.registerListener(this.player1);
        this.dispatcher.registerListener(this.player2);
        this.dispatcher.registerListener(this.player3);
        this.dispatcher.registerListener(this.player4);

        this.nextMap.setListener((next) -> this.loadMap(this.mapFiles.next()));
        this.prevMap.setListener((prev) -> this.loadMap(this.mapFiles.prev()));

        /* load default map */
        this.loadMap(this.mapFiles.get(0));
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
        List<Player> players = new ArrayList<>(4);

        if( this.player1.getType() == PlayerConfigElement.PlayerType.HUMAN )
            players.add(new Player(player1.getPlayerName(),player1.getPlayerColor(),currentSelctedMap));
        else if( this.player1.getType() == PlayerConfigElement.PlayerType.AI )
            players.add(new RandomAI(player1.getPlayerName(),player1.getPlayerColor(),currentSelctedMap));

        if( this.player2.getType() == PlayerConfigElement.PlayerType.HUMAN )
            players.add(new Player(player2.getPlayerName(),player2.getPlayerColor(),currentSelctedMap));
        else if( this.player2.getType() == PlayerConfigElement.PlayerType.AI )
            players.add(new RandomAI(player2.getPlayerName(),player2.getPlayerColor(),currentSelctedMap));

        if( this.player3.getType() == PlayerConfigElement.PlayerType.HUMAN )
            players.add(new Player(player3.getPlayerName(),player3.getPlayerColor(),currentSelctedMap));
        else if( this.player3.getType() == PlayerConfigElement.PlayerType.AI )
            players.add(new RandomAI(player3.getPlayerName(),player3.getPlayerColor(),currentSelctedMap));

        if( this.player4.getType() == PlayerConfigElement.PlayerType.HUMAN )
            players.add(new Player(player4.getPlayerName(),player4.getPlayerColor(),currentSelctedMap));
        else if( this.player4.getType() == PlayerConfigElement.PlayerType.AI )
            players.add(new RandomAI(player4.getPlayerName(),player4.getPlayerColor(),currentSelctedMap));

        return players;
    }

    public void registerNewGameListener(MouseClickedListener l) {
        this.beginGameBtn.setListener(l);
    }

    @Override
    public void shown() {
        this.loadMap(this.currentSelctedMap.getMapName());
    }
}
