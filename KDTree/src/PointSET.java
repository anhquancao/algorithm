import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1.0);
        StdDraw.setYscale(0, 1.0);
        for (Point2D p : points) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> results = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                results.add(p);
            }
        }
        return results;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D minPoint = null;
        for (Point2D p1 : points) {
            if (p.distanceSquaredTo(p1) < minDistance) {
                minDistance = p.distanceSquaredTo(p1);
                minPoint = p1;
            }
        }
        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        Point2D p1 = new Point2D(0.1, 0.4);
        Point2D p2 = new Point2D(0.0, 0.0);
        Point2D p3 = new Point2D(0.6, 0.5);
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.insert(p3);

        RectHV rectHV = new RectHV(0.4, 0.3, 0.8, 0.6);
        for (Point2D p : pointSET.range(rectHV)) {
            System.out.println("p: " + p);
        }

        System.out.println("Nearest: " + pointSET.nearest(new Point2D(0.1, 0.1)));

    }
}
