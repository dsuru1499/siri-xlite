package siri_xlite.common;

import org.springframework.data.geo.Point;
import org.springframework.data.geo.Polygon;

import java.util.Arrays;

public class OSMUtils {

    public static int ZOOM = 14;

    public static Polygon location(final int x, final int y) {
        return location(x, y, ZOOM);
    }

    public static Polygon location(final int x, final int y, final int z) {
        double[] upperLeft = fromTile(x, y, z);
        double[] bottomRight = fromTile(x + 1, y + 1, z);

        Polygon result = new Polygon(Arrays.asList(new Point(upperLeft[0], upperLeft[1]),
                new Point(bottomRight[0], upperLeft[1]), new Point(bottomRight[0], bottomRight[1]),
                new Point(upperLeft[0], bottomRight[1]), new Point(upperLeft[0], upperLeft[1])));
        return result;
    }

    public static double[] fromTile(final int x, final int y, final int zoom) {
        double n = (1 << zoom);
        double lon = (double) x / n * 360d - 180d;
        double lat = Math.atan(Math.sinh(Math.PI * (1d - 2d * (double) y / n))) * 180d / Math.PI;
        double[] result = { lon, lat };
        return result;
    }

    public static int[] toTile(final double lon, final double lat, final int zoom) {
        double n = (1 << zoom);
        int x = (int) (n * ((lon + 180d) / 360d));
        double lat_rad = Math.toRadians(lat);
        int y = (int) (n * (1 - (Math.log(Math.tan(lat_rad) + 1d / Math.cos(lat_rad)) / Math.PI)) / 2d);
        int[] result = { x, y };
        return result;
    }

}
