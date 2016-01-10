package pk.risiko.ui.listener;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.screens.GamePanel;
import pk.risiko.ui.screens.UserInterface;

import java.awt.event.MouseEvent;

/**
 * Created by:
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterfaceMouseListener implements MouseEventListener {

    private final UserInterface userInterface;
    private final GamePanel gamePanel;

    public UserInterfaceMouseListener(UserInterface ui,GamePanel gp) {
        this.userInterface = ui;
        this.gamePanel = gp;
    }

    @Override
    public boolean mouseMoved(MouseEvent e) {
        return false;
    }

    @Override
    public boolean mouseClicked(MouseEvent e) {
        if(this.userInterface.isInNextButton(e.getX(),e.getY())) {
            this.gamePanel.changeState(GameState.NEXT_ROUND);
            return true;
        }

        if(this.userInterface.isInMenuButton(e.getX(),e.getY())) {
            this.gamePanel.changeState(GameState.SHOW_MENU);
            return true;
        }

        return false;
    }

}
