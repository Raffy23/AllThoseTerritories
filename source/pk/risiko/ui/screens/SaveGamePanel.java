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
 * This Panel graphically represents the screen where the user can choose between save slots
 *
 * @author Raphael Ludwig
 * @version 03.02.2016
 */
public class SaveGamePanel implements GameScreen {

    private final SwingMouseEventDispatcher dispatcher = new SwingMouseEventDispatcher();

    /** Number of slots which are displayed **/
    private static final int SAVE_GAME_ROWS = 5;
    /** Data which represents an non-valid / empty save slots **/
    private static final SaveGameContent EMPTY_SAVE_GAME = new SaveGameContent(null,"EMPTY","",new ArrayList<>(),0,-1);

    /** the ui elements for the load / save stuff **/
    private final SaveGameUIRow[] saveGameUIRows = new SaveGameUIRow[SAVE_GAME_ROWS];
    private final GameButton backToMenuBtn;

    private final Font headlineFont;

    /**
     * After initialization the data of the slots is not loaded, this is done after every time the
     * Panel is shown. Also default the view is created so that all save buttons are hidden.
     */
    public SaveGamePanel() {
        final int X_OFFSET = 230;

        //Create all the Save/Load Buttons
        for(int i=0;i<SAVE_GAME_ROWS;i++) {
            saveGameUIRows[i] = new SaveGameUIRow(GameWindow.WINDOW_WIDTH / 2 - SaveGameUIRow.ELEMENT_WIDTH / 2
                                                 , i * (20 + DefaultDesigns.BUTTON_HEIGHT) + X_OFFSET, EMPTY_SAVE_GAME
                                                 , false, true,i);
            this.dispatcher.registerListener(saveGameUIRows[i]);
        }

        //Create the Back to menu button
        this.backToMenuBtn = new GameButton(new Rectangle2D.Double(15
                                                                  ,GameWindow.WINDOW_HEIGHT-95
                                                                  ,100
                                                                  ,35),"Back to Menu");
        this.dispatcher.registerListener(this.backToMenuBtn);


        //set headline front (like mainmenu)
        this.headlineFont = FontLoader.loadFont(DefaultDesigns.HEADLINE_FONT,MainMenuPanel.HEADLINE_FONT_SIZE);
        //default the menu allows only loading
        this.loadGameOnly();
    }


    @Override
    public SwingMouseEventDispatcher getMouseEventDispatcher() {
        return this.dispatcher;
    }

    /**
     * After the screen is shown the meta data of all saves is loaded again
     */
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

    /**
     * @return the button which is responsible to go back to the menu
     */
    public GameButton getBackToMenuBtn() {
        return this.backToMenuBtn;
    }

    /**
     * Registers the listener to all load buttons, these can be identified by the data in the SaveGameContent Data
     * @param l a listener
     */
    public void registerLoadGameListener(MouseClickedListener l) {
        for(int i=0;i<SAVE_GAME_ROWS;i++) saveGameUIRows[i].setLoadBtnListener(l);
    }

    /**
     * Registers the listener to all save buttons, these can be identified by the data in the SaveGameContent Data
     * @param l a listener
     */
    public void registerSaveGameListener(MouseClickedListener l) {
        for(int i=0;i<SAVE_GAME_ROWS;i++) saveGameUIRows[i].setSaveGameListener(l);
    }

    /**
     * Hides all the Load Buttons in the screen
     * unhides all the save buttons in the screen if hidden
     */
    public void saveGameOnly() {
        for(int i=0;i<SAVE_GAME_ROWS;i++) {
            saveGameUIRows[i].setHideLoadBtn(true);
            saveGameUIRows[i].setHideSaveBtn(false);
        }
    }

    /**
     * Hides all the Save Buttons in the screen
     * Unhides all the Load Buttons if hidden
     */
    public void loadGameOnly() {
        for(int i=0;i<SAVE_GAME_ROWS;i++) {
            saveGameUIRows[i].setHideSaveBtn(true);
            saveGameUIRows[i].setHideLoadBtn(false);
        }
    }
}
