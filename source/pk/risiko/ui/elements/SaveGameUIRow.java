package pk.risiko.ui.elements;

import pk.risiko.pojo.SaveGameContent;
import pk.risiko.ui.MouseEventHandler;
import pk.risiko.ui.listener.MouseClickedListener;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 *
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
    private SaveGameContent saveGame;

    private MouseClickedListener saveGameListener;
    private MouseClickedListener loadBtnListener;
    private MouseClickedListener saveBtnListener;


    public SaveGameUIRow(int x,int y,SaveGameContent saveGame,boolean hideLoadBtn) {
        super(new Rectangle(x,y,355,DefaultDesigns.BUTTON_HEIGHT));
        this.hideLoadBtn = hideLoadBtn;

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
        if( !hideLoadBtn )
            this.loadBtn.paint(g);

        this.saveBtn.paint(g);
        this.saveGameBtn.paint(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if( !hideLoadBtn )
            this.loadBtn.mouseClicked(e);

        this.saveBtn.mouseClicked(e);
        this.saveGameBtn.mouseClicked(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if( !hideLoadBtn )
            this.loadBtn.mouseMoved(e);

        this.saveBtn.mouseMoved(e);
        this.saveGameBtn.mouseMoved(e);
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
        if( this.loadBtnListener != null ) loadBtnListener.mouseClicked(this);
        if( this.saveGameListener != null ) saveGameListener.mouseClicked(this);
        if( this.saveBtnListener != null ) saveBtnListener.mouseClicked(this);
    }
}
