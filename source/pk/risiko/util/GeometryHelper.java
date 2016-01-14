package pk.risiko.util;

import java.awt.Shape;
import java.awt.geom.Area;
import java.util.List;

/**
 * This class does function as helper for the various transformations
 * between #java.awt.Shape classes to #java.awt.geom.Area
 *
 * @author Raphael
 * @version 12.01.2016
 */
public class GeometryHelper {

    /**
     * Does generate a #java.awt.geom.Area from a List of #java.awt.Shape
     * These Shapes are added with the Area.add method
     *
     * @param shapes List of shapes
     * @return a Area which reference all as on Area
     */
    public static Area generateAreaFrom(List<Shape> shapes) {
        final Area area = new Area();
        shapes.forEach(s -> area.add(new Area(s)));
        return area;
    }

}
