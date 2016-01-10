package pk.risiko.ui;

/**
 * Created by:
 *
 * @author Raphael
 * @version 10.01.2016
 */
public interface Interactable {

    boolean isMouseIn(int x,int y);
    boolean mouseClicked();
    boolean keyEntered(char key);

}
