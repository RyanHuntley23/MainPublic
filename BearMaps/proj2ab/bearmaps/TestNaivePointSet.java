package bearmaps;
import java.util.List;
import static org.junit.Assert.assertTrue;

public class TestNaivePointSet {
    public static void main (String args[]) {
        int x1 = 1;
        int y1 = 1;
        int x2 = 2;
        int y2 = 3;
        int x3 = 7;
        double y3 = 9.2;
        double x4 = 5.3;
        double y4 = 2.3;
        int x5 = 0;
        int y5 = -8;
        double x6 = -3.9;
        double y6 = -9.1;

        Point p1 = new Point(x1, y1);
        Point p2 = new Point(x2, y2);
        Point p3 = new Point(x3, y3);
        Point p4 = new Point(x4, y4);
        Point p5 = new Point(x5, y5);
        Point p6 = new Point(x6, y6);

        NaivePointSet nps2 = new NaivePointSet(List.of(p1, p2));
        NaivePointSet nps3 = new NaivePointSet(List.of(p2, p1));
        NaivePointSet nps4 = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6));
        NaivePointSet nps5 = new NaivePointSet(List.of(p6, p5, p4, p3, p2, p1));

        x1 = 0;
        y1 = 0;
        x4 = -3.9;
        y4 = -1000000.5;
        x6 = 6.2;
        y6 = -0.1;

        assertTrue(nps2.nearest(x1, y1).equals(p1));
        assertTrue(nps3.nearest(x1, y1).equals(p1));
        assertTrue(nps4.nearest(x4, y4).equals(p6));
        assertTrue(nps5.nearest(x4, y4).equals(p6));
        assertTrue(nps4.nearest(x6, y6).equals(p4));
        assertTrue(nps5.nearest(x6, y6).equals(p4));
    }
}
