import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private class Node {
        private final Point2D p; // the point
        private final RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        public Node(Point2D p, Node lb, Node rt, RectHV rect) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
            this.rect = rect;
        }

    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            size += 1;
            this.root = this.put(root, p, 0, new RectHV(0.0, 0.0, 1.0, 1.0));
        }
    }

    private Node put(Node x, Point2D p, int level, RectHV rect) {

        if (x == null) return new Node(p, null, null, rect);


        RectHV lbEven = new RectHV(x.rect.xmin(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        RectHV lbOdd = new RectHV(x.p.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
        RectHV rtEven = new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.p.y());
        RectHV rtOdd = new RectHV(x.rect.xmin(), x.p.y(), x.rect.xmax(), rect.ymax());


        if (level % 2 == 0) {
            if (p.x() < x.p.x()) {
                x.lb = this.put(x.lb, p, level + 1, lbEven);
            } else {
                x.rt = this.put(x.rt, p, level + 1, lbOdd);
            }
        } else {
            if (p.y() < x.p.y()) {
                x.lb = this.put(x.lb, p, level + 1, rtEven);
            } else {
                x.rt = this.put(x.rt, p, level + 1, rtOdd);
            }
        }


        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return this.find(root, p, 0);
    }

    private boolean find(Node x, Point2D p, int level) {
        if (x == null) return false;
        if (p.equals(x.p)) return true;
        if (level % 2 == 0) {
            if (p.x() < x.p.x()) {
                return this.find(x.lb, p, level + 1);
            } else {
                return this.find(x.rt, p, level + 1);
            }
        } else {
            if (p.y() < x.p.y()) {
                return this.find(x.lb, p, level + 1);
            } else {
                return this.find(x.rt, p, level + 1);
            }
        }

    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setXscale(0, 1.0);
        StdDraw.setYscale(0, 1.0);
        draw(root, 0);


    }

    private void draw(Node x, int level) {
        if (x == null) return;
        StdDraw.setPenRadius();
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }


        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        x.p.draw();

        draw(x.lb, level + 1);
        draw(x.rt, level + 1);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> results = new ArrayList<>();
        this.search(root, rect, results);
        return results;
    }

    private void search(Node x, RectHV rect, List<Point2D> points) {
        if (x == null) return;
        if (rect.contains(x.p)) {
            points.add(x.p);
        }
        if (x.lb != null && rect.intersects(x.lb.rect)) search(x.lb, rect, points);
        if (x.rt != null && rect.intersects(x.rt.rect)) search(x.rt, rect, points);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearestRecur(root, p, root.p);
    }

    private Point2D nearestRecur(Node x, Point2D p, Point2D minPoint) {
        if (x == null || x.rect.distanceSquaredTo(p) > minPoint.distanceSquaredTo(p)) return minPoint;


        if (x.p.distanceSquaredTo(p) < minPoint.distanceSquaredTo(p)) {
            minPoint = x.p;
        }


        if (x.lb != null && x.rt != null) {
            if (x.lb.p.distanceSquaredTo(p) < x.rt.p.distanceSquaredTo(p)) {
                minPoint = nearestRecur(x.lb, p, minPoint);
                minPoint = nearestRecur(x.rt, p, minPoint);
                return minPoint;
            } else {
                minPoint = nearestRecur(x.rt, p, minPoint);
                minPoint = nearestRecur(x.lb, p, minPoint);
                return minPoint;
            }
        } else {
            if (x.lb == null && x.rt == null) {
                return minPoint;
            }
            if (x.lb != null) return nearestRecur(x.lb, p, minPoint);
            if (x.rt != null) return nearestRecur(x.rt, p, minPoint);
        }

        return minPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {


        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));
        StdDraw.enableDoubleBuffering();
        kdtree.draw();
//        StdDraw.setPenRadius();
//        RectHV rect = new RectHV(0.3, 0.3, 0.6, 0.8);
//        rect.draw();
//        Iterable<Point2D> results = kdtree.range(rect);
//        for (Point2D p : results) {
//            System.out.println(p);
//        }
        Point2D p = new Point2D(0.392, 0.928);
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(StdDraw.MAGENTA);
        p.draw();

        Point2D nearestPoint = kdtree.nearest(p);
        StdDraw.setPenColor(StdDraw.ORANGE);
        nearestPoint.draw();
        StdDraw.show();
    }
}
