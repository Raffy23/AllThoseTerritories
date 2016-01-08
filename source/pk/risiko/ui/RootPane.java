package pk.risiko.ui;

import pk.risiko.util.GameStateMachine;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the root panel in the frame
 * which is responsible for drawing the active screen
 * TODO: Merge with GameWindow ...
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class RootPane extends JPanel  {

    private static final Color BACKGRUND_COLOR = new Color(58, 164, 255);
    private final GameStateMachine stateMachine;


    public RootPane(GameStateMachine gameStateMachine) {
        this.stateMachine = gameStateMachine;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(BACKGRUND_COLOR);
        g.fillRect(0,0,this.stateMachine.getWindow().getWidth(),this.stateMachine.getWindow().getHeight());

        this.stateMachine.getActiveScreen().paint(g);
    }

}
