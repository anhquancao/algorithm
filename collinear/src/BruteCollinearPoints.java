import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;


    public BruteCollinearPoints(Point[] points) {

        if (points == null) throw new IllegalArgumentException();
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException();
        }
        for (int i1 = 0; i1 < points.length; i1++) {
            for (int i2 = i1 + 1; i2 < points.length; i2++) {
                Point p1 = points[i1];
                Point p2 = points[i2];
                if (p1.compareTo(p2) == 0) throw new IllegalArgumentException();
            }
        }

        Point[] pCopy = points.clone();

        Arrays.sort(pCopy, Point::compareTo);

        lineSegments = new ArrayList<>();
        for (int i1 = 0; i1 < pCopy.length; i1++) {
            for (int i2 = i1 + 1; i2 < pCopy.length; i2++) {
                for (int i3 = i2 + 1; i3 < pCopy.length; i3++) {
                    for (int i4 = i3 + 1; i4 < pCopy.length; i4++) {
                        Point p1 = pCopy[i1];
                        Point p2 = pCopy[i2];
                        Point p3 = pCopy[i3];
                        Point p4 = pCopy[i4];
//                        System.out.println(p1 + " " + p2 + " " + p3 + " " + p4);
                        double slope12 = p1.slopeTo(p2);
                        double slope13 = p1.slopeTo(p3);
                        double slope14 = p1.slopeTo(p4);
                        if (Double.compare(slope12, slope13) == 0 && Double.compare(slope13, slope14) == 0) {
                            LineSegment lineSegment = new LineSegment(p1, p4);
                            lineSegments.add(lineSegment);
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }
}
