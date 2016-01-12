package pk.risiko.util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.List;

/**
 * Created by
 *
 * @author Raphael
 * @version 12.01.2016
 */
public class GeometryHelper {

    public static Area generateAreaFrom(List<Shape> shapes) {
        final Area area = new Area();
        shapes.forEach(s -> area.add(new Area(s)));
        return area;
    }

}
