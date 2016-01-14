package pk.risiko.ui;

/**
 * This Interface specifies a way to tell any
 * component that a mouse event happened
 *
 * @author Raphael
 * @version 10.01.2016
 */
public interface MouseEventHandler {

    boolean isMouseIn(int x,int y);
    void mouseClicked();

}
