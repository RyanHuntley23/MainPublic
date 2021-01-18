package bearmaps;
import java.util.List;

/** Slow implementation of PointSet used
 *  to test correctness of KDTree.
 *  @author Ryan Huntley */
public class NaivePointSet implements PointSet {
    /** List of points in our set. */
    private List<Point> pointList;

    /** Constructor.
     *  @param points list of points */
    public NaivePointSet(List<Point> points) {
        pointList = points;
    }

    /** @return the nearest point in our set to (x, y)
     *  @param x x-coordinate
     *  @param y y-coordinate */
    @Override
    public Point nearest(double x, double y) {
        Point newP = new Point(x, y);
        Point best = null;
        boolean firstPoint = true;
        for (Point p : pointList) {
            if (firstPoint) {
                best = p;
                firstPoint = false;
            } else {
                if (Point.distance(p, newP) < Point.distance(best, newP)) {
                    best = p;
                }
            }
        }
        return best;
    }
}
