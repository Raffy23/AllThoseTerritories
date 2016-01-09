package pk.risiko.ui.listener;

import pk.risiko.pojo.GameState;
import pk.risiko.ui.GamePanel;
import pk.risiko.ui.UserInterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by:
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class UserInterfaceMouseListener extends MouseAdapter {

    private final UserInterface userInterface;
    private final GamePanel gamePanel;

    public UserInterfaceMouseListener(UserInterface ui,GamePanel gp) {
        this.userInterface = ui;
        this.gamePanel = gp;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(this.userInterface.isInNextButton(e.getX(),e.getY()))
            this.gamePanel.changeState(GameState.NEXT_ROUND);

        if(this.userInterface.isInMenuButton(e.getX(),e.getY()))
            this.gamePanel.changeState(GameState.SHOW_MENU);
    }
}
