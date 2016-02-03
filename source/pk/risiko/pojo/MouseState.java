package pk.risiko.pojo;

/**
 * Type does specify which mouse states are important
 * to the game and also get to be handled by it
 *
 * @author Raphael Ludwig, Wilma Weixelbaum
 * @version 10.01.2016
 */
public enum MouseState {
    /**
     * This MouseState should be active,
     * when the Left mousebutton is pressed
     */
    L_CLICKED,
    /**
     * This MouseState should be active,
     * when the Right mousebutton is pressed
     */
    R_CLICKED,
    /**
     * This MouseState should be active,
     * when the mouse currently hovers over
     * an object
     */
    HOVER,
    /**
     * This MouseState should be active,
     * when none of the other states are
     */
    NORMAL
}
