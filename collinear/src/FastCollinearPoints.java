import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {
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
        lineSegments = new ArrayList<>();
        ArrayList<Point> starts = new ArrayList<>();
        ArrayList<Point> ends = new ArrayList<>();

        Arrays.sort(pCopy, Point::compareTo);
        Point[] jCopy = pCopy.clone();

        for (int i1 = 0; i1 < pCopy.length; i1++) {
            Point p = pCopy[i1];
            Arrays.sort(jCopy, p.slopeOrder());


            ArrayList<Point> temp = new ArrayList<>();
            temp.add(p);

//            for (int k = 0; k < pCopy.length; k++) {
//                System.out.println(p + " - " + jCopy[k] + " " + p.slopeTo(jCopy[k]));
//            }
            int sIdx = 1;
            int eIdx = 2;

            boolean found = false;
            while (eIdx < jCopy.length) {
                double slope = p.slopeTo(jCopy[sIdx]);
                double nextSlope = p.slopeTo(jCopy[eIdx]);
                if (Double.compare(slope, nextSlope) == 0) {
                    temp.add(jCopy[eIdx]);
                    eIdx += 1;
                    if (!found) {
                        temp.add(jCopy[sIdx]);
                        found = true;
                    }

                } else {
                    if (found) {
                        break;
                    }
                    sIdx += 1;
                    eIdx += 1;
                }
            }


            if (found) {
                Point[] selectedPoints = temp.toArray(new Point[temp.size()]);
                Arrays.sort(selectedPoints, Point::compareTo);
                Point start = selectedPoints[0];
                Point end = selectedPoints[selectedPoints.length - 1];

                boolean isContained = false;
                for (int j = 0; j < starts.size(); j++) {
                    if (starts.get(j) == start && ends.get(j) == end) {
                        isContained = true;
                    }

                }
                if (!isContained) {
                    starts.add(start);
                    ends.add(end);
                    lineSegments.add(new LineSegment(selectedPoints[0], selectedPoints[selectedPoints.length - 1]));
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
