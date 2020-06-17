package siri_xlite.common;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

import static org.apache.commons.lang3.tuple.Pair.of;

public interface OSMUtils {

    int ZOOM = 16;

    default Box location(final int x, final int y) {
        return location(x, y, ZOOM);
    }

    default Box location(final int x, final int y, final int z) {

        Point upperLeft = fromTile(x, y, z);
        Point bottomRight = fromTile(x + 1, y + 1, z);
        System.out.println(upperLeft);
        System.out.println(bottomRight);

        Point bottomLeft = new Point(upperLeft.getX(), bottomRight.getY());
        Point upperRight = new Point(bottomRight.getX(), upperLeft.getY());

        Box result = new Box(bottomLeft, upperRight);
        System.out.println(result);

        return result;
    }

    default Point fromTile(final int x, final int y, final int zoom) {
        double n = (1 << zoom);
        double lon = (double) x / n * 360d - 180d;
        double lat = Math.atan(Math.sinh(Math.PI * (1d - 2d * (double) y / n))) * 180d / Math.PI;
        return new Point(lon, lat);
    }

    default Pair<Integer, Integer> toTile(final double lon, final double lat, final int zoom) {
        double n = (1 << zoom);
        int x = (int) (n * ((lon + 180d) / 360d));
        double lat_rad = Math.toRadians(lat);
        int y = (int) (n * (1 - (Math.log(Math.tan(lat_rad) + 1d / Math.cos(lat_rad)) / Math.PI)) / 2d);

        return of(x, y);
    }

}
