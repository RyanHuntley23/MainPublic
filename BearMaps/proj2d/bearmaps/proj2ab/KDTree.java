package bearmaps.proj2ab;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.StdRandom;

/** Efficient implementation of PointSet.
 *  @author Ryan Huntley */
public class KDTree implements PointSet {
    /** First point in KDTree. */
    private KDNode first;
    /** For testing. */
    private static final double THOUSAND = 1000.0;
    /** For testing. */
    private static final int NUM125 = 125;
    /** For testing. */
    private static final int NUM1000 = 1000;
    /** For testing. */
    private static final int NUM10000 = 10000;
    /** For testing. */
    private static final int NUM31250 = 31250;
    /** For testing. */
    private static final int NUM1000000 = 1000000;
    /** For testing. */
    private static final int NUM2000000 = 2000000;

    /** Object containing a point, its depth, and pointers
     *  to left and right points. */
    private class KDNode implements Comparable<KDNode> {
        /** The Point held in KDNode. */
        private Point point;
        /** Depth of KdNode within KDTree. */
        private int depth;
        /** The node to the left ("less than") the KDNode. */
        private KDNode left;
        /** The node to the left ("greater than") the KDNode. */
        private KDNode right;

        /** Constructor.
         *  @param p the Point held in KDNode
         *  @param dd the depth of the Point in the KDTree, determines
         *            how two KDNodes are compared (nodes at odd depths
         *            split the field by y values, nodes at even depths
         *            split the field by x values; the first node is at
         *            depth 0) */
        KDNode(Point p, int dd) {
            point = p;
            depth = dd;
            left = null;
            right = null;
        }

        /** @return negative int if KDNode < other
         *          zero if KDNode == other
         *          positive if KDNode > other
         *  @param other KDNode to compare this to */
        @Override
        public int compareTo(KDNode other) {
            int modDepth = Math.floorMod(depth, 2);
            if (modDepth == 0) {
                return (int) Math.floor(point.getX() - other.point.getX());
            }
            return (int) Math.floor(point.getY() - other.point.getY());
        }
    }

    /** Constructor.
     *  @param points list of points */
    public KDTree(List<Point> points) {
        first = null;
        for (Point p : points) {
            addPoint(p);
        }
    }

    /** Helper function for addPoint. Inserts newNode at its correct
     *  place within the KDTree.
     *  @return newNode at the base case, otherwise just sets the
     *          current node equal to itself
     *  @param newNode KDNode (containing a point) to be inserted
     *  @param currentNode KDNode to compare newNode to
     *  @param d current depth */
    private KDNode addHelper(KDNode newNode, KDNode currentNode, int d) {
        if (currentNode == null) {
            newNode.depth = d;
            return newNode;
        } else if (currentNode.compareTo(newNode) > 0) {
            currentNode.left = addHelper(newNode, currentNode.left, d + 1);
        } else {
            currentNode.right = addHelper(newNode, currentNode.right, d + 1);
        }
        return currentNode;
    }

    /** Adds Point p to KDTree.
     *  @param p the Point being added */
    private void addPoint(Point p) {
        KDNode newNode = new KDNode(p, 0);
        first = addHelper(newNode, first, 0);
    }

    /** Helper function for nearestHelper.
     *  @return true if there could be a better node on the
     *          bad side of current node
     *  @param currNode node that we are currently inspecting
     *         (we will look at the good and maybe bad sides)
     *  @param p given point
     *  @param bestNode the closest node to p that we have found so far */
    private boolean badSideUseful(KDNode currNode, Point p, KDNode bestNode) {
        double distance;
        if (Math.floorMod(currNode.depth, 2) == 0) {
            distance = Math.abs(p.getX() - currNode.point.getX());
        } else {
            distance = Math.abs(p.getY() - currNode.point.getY());
        }
        return distance < Math.sqrt(Point.distance(p, bestNode.point));
    }

    /** Helper method for nearest.
     *  @return KDNode that is closest to p
     *  @param currentNode node that we are currently inspecting
     *         (we will look at the good and maybe bad sides)
     *  @param point given point
     *  @param bestNode the closest node to p that we have found so far */
    private KDNode nHelper(KDNode currentNode, KDNode point, KDNode bestNode) {
        if (currentNode == null) {
            return bestNode;
        }
        if (bestNode == null) {
            bestNode = currentNode;
        } else {
            Point c = currentNode.point;
            Point p = point.point;
            Point b = bestNode.point;
            double currentDistance = Math.sqrt(Point.distance(c, p));
            double bestDistance = Math.sqrt(Point.distance(b, p));
            if (currentDistance < bestDistance) {
                bestNode = currentNode;
            }
        }
        KDNode good;
        KDNode bad;
        if (currentNode.compareTo(point) > 0) {
            good = currentNode.left;
            bad = currentNode.right;
        } else {
            good = currentNode.right;
            bad = currentNode.left;
        }
        bestNode = nHelper(good, point, bestNode);
        if (badSideUseful(currentNode, point.point, bestNode)) {
            bestNode = nHelper(bad, point, bestNode);
        }
        return bestNode;
    }

    /** @return the nearest point in our set to (x, y)
     *  @param x x-coordinate
     *  @param y y-coordinate */
    @Override
    public Point nearest(double x, double y) {
        KDNode p = new KDNode(new Point(x, y), 0);
        return nHelper(first, p, null).point;
    }
}
