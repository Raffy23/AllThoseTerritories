package pk.risiko.ui.elements;

import pk.risiko.pojo.SaveGameContent;
import pk.risiko.ui.MouseEventHandler;
import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Graphical Object which represents the 3 buttons that are viewed
 * at the GameSavePanel
 *
 * @author Raphael ludwig
 * @version 03.02.2016
 */
public class SaveGameUIRow extends UIElement implements MouseClickedListener {

    public final static int ELEMENT_WIDTH = 445;

    private final GameButton loadBtn;
    private final GameButton saveBtn;
    private final GameButton saveGameBtn;

    private boolean hideLoadBtn;
    private boolean hideSaveBtn;
    private SaveGameContent saveGame;
    private int visibleSlot;

    private MouseClickedListener saveGameListener;
    private MouseClickedListener loadBtnListener;
    private MouseClickedListener saveBtnListener;


    public SaveGameUIRow(int x,int y,SaveGameContent saveGame,boolean hideLoadBtn,boolean hideSaveBtn,int visibleSlot) {
        super(new Rectangle(x,y,355,DefaultDesigns.BUTTON_HEIGHT));
        this.hideLoadBtn = hideLoadBtn;
        this.hideSaveBtn = hideSaveBtn;
        this.visibleSlot = visibleSlot;

        this.saveGameBtn = new GameButton(new Rectangle2D.Double(x,y,245,DefaultDesigns.BUTTON_HEIGHT),saveGame.getName());
        this.saveBtn = new GameButton(new Rectangle2D.Double(x+255,y,90,DefaultDesigns.BUTTON_HEIGHT),"SAVE");
        this.loadBtn = new GameButton(new Rectangle2D.Double(x+355,y,90,DefaultDesigns.BUTTON_HEIGHT),"LOAD");

        this.saveGameBtn.setListener(this);
        this.loadBtn.setListener(this);
        this.saveBtn.setListener(this);
    }


    public void setSaveGame(SaveGameContent saveGame) {
        this.saveGame = saveGame;
        this.saveGameBtn.setText(saveGame.getName());
    }

    public SaveGameContent getSaveGame() {
        return this.saveGame;
    }

    @Override
    public void paint(Graphics2D g) {
        if( !hideLoadBtn && this.saveGame.getSlot() != -1 )
            this.loadBtn.paint(g);

        if( !hideSaveBtn )
            this.saveBtn.paint(g);

        this.saveGameBtn.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( !hideLoadBtn && this.saveGame.getSlot() != -1 )
            this.loadBtn.mouseClicked(e);

        if( !hideSaveBtn )
            this.saveBtn.mouseClicked(e);

        this.saveGameBtn.mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if( !hideLoadBtn && this.saveGame.getSlot() != -1 )
            this.loadBtn.mouseMoved(e);

        if( !hideSaveBtn )
            this.saveBtn.mouseMoved(e);

        this.saveGameBtn.mouseMoved(e);
    }

    public void setHideLoadBtn(boolean hideLoadBtn) {
        this.hideLoadBtn = hideLoadBtn;
    }

    public void setHideSaveBtn(boolean hideSaveBtn) {
        this.hideSaveBtn = hideSaveBtn;
    }

    @Override
    public void mouseClicked() { /* nothing to do */ }

    public void setSaveGameListener(MouseClickedListener saveGameListener) {
        this.saveGameListener = saveGameListener;
    }

    public void setLoadBtnListener(MouseClickedListener loadBtnListener) {
        this.loadBtnListener = loadBtnListener;
    }

    public void setSaveBtnListener(MouseClickedListener saveBtnListener) {
        this.saveBtnListener = saveBtnListener;
    }

    @Override
    public void mouseClicked(MouseEventHandler what) {
        if( this.loadBtnListener != null && !hideLoadBtn) loadBtnListener.mouseClicked(this);
        if( this.saveGameListener != null && !hideSaveBtn) saveGameListener.mouseClicked(this);
        if( this.saveBtnListener != null ) saveBtnListener.mouseClicked(this);
    }

    public int getVisibleSlot() {
        return visibleSlot;
    }
}
