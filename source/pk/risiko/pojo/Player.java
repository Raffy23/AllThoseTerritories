package pk.risiko.pojo;

import java.awt.*;

/**
 * This Class does represent the Player Entity
 *
 * @author Raphael
 * @version 08.01.2016
 */
public class Player {
    private String name;
    private Color color;
    protected boolean human = true;

    public Player(String name,Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return this.color;
    }
}
