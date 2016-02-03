package pk.risiko.ui.screens;

import pk.risiko.dao.SaveGameDAO;
import pk.risiko.pojo.SaveGameContent;
import pk.risiko.ui.GameWindow;
import pk.risiko.ui.elements.DefaultDesigns;
import pk.risiko.ui.elements.GameButton;
import pk.risiko.ui.elements.SaveGameUIRow;
import pk.risiko.ui.listener.MouseClickedListener;
import pk.risiko.ui.listener.SwingMouseEventDispatcher;
import pk.risiko.util.FontLoader;
import pk.risiko.util.SettingsProvider;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Raphael Ludwig
 * @version 03.02.2016
 */
public class SaveGamePanel implements GameScreen {

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();

    private static final int SAVE_GAME_ROWS = 5;
    private static final SaveGameContent EMPTY_SAVE_GAME = new SaveGameContent(null,"EMPTY","",new ArrayList<>(),0,-1);

    private boolean hideSaveButton = false;
    private final SaveGameUIRow[] saveGameUIRows = new SaveGameUIRow[SAVE_GAME_ROWS];
    private final GameButton backToMenuBtn;

    private final Font headlineFont;

    public SaveGamePanel() {
        final int X_OFFSET = 230;

        for(int i=0;i<SAVE_GAME_ROWS;i++) {
            saveGameUIRows[i] = new SaveGameUIRow(GameWindow.WINDOW_WIDTH / 2 - SaveGameUIRow.ELEMENT_WIDTH / 2
                    , i * (20 + DefaultDesigns.BUTTON_HEIGHT) + X_OFFSET, EMPTY_SAVE_GAME
                    , false);
            this.dispatcher.registerListener(saveGameUIRows[i]);
        }

        this.backToMenuBtn = new GameButton(new Rectangle2D.Double(15
                                                                  ,GameWindow.WINDOW_HEIGHT-95
                                                                  ,100
                                                                  ,35),"Back to Menu");
        this.dispatcher.registerListener(this.backToMenuBtn);

        this.headlineFont = FontLoader.loadFont(DefaultDesigns.HEADLINE_FONT,MainMenuPanel.HEADLINE_FONT_SIZE);
    }


    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void shown() {
        final SaveGameDAO dao = new SaveGameDAO(SettingsProvider.getInstance().getSavefileDirectory());

        Map<Integer,SaveGameContent> saveGames = dao.getSaveGames();
        for(int i=0;i<SAVE_GAME_ROWS;i++) this.saveGameUIRows[i].setSaveGame(saveGames.getOrDefault(i,EMPTY_SAVE_GAME));

    }

    @Override
    public void paint(Graphics2D g) {
        final Font oldFont = g.getFont();
        g.setFont(this.headlineFont);

        final int fontHeight = g.getFontMetrics().getHeight();
        final int fontWidth  = g.getFontMetrics().stringWidth(MainMenuPanel.TITLE);
        g.setColor(MainMenuPanel.HEADLINE_COLOR);
        g.drawString(MainMenuPanel.TITLE,GameWindow.WINDOW_WIDTH/2-fontWidth/2,fontHeight + 5);

        g.setFont(oldFont);

        for(int i=0;i<SAVE_GAME_ROWS;i++) saveGameUIRows[i].paint(g);
        this.backToMenuBtn.paint(g);
    }

    public GameButton getBackToMenuBtn() {
        return this.backToMenuBtn;
    }

    public void registerLoadGameListener(MouseClickedListener l) {
        for(int i=0;i<SAVE_GAME_ROWS;i++) saveGameUIRows[i].setLoadBtnListener(l);
    }
}
