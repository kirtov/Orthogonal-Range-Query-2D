import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Tester {
    final int POINTS_TO = 1048577;
    final int RANGE = 200000;
    final int QUERIES = 5000;
    final double TIME_DIV = 1000000000.0;
    public static void main(String[] args) {
        new Tester().start();
    }

    void start() {
        Random rand = new Random();
        for (int pointsCount = 32; pointsCount <= POINTS_TO; pointsCount *= 4) {
            long tTime = 0, nsTime = 0;
            Set<Point> pts = new HashSet<>();
            while (pts.size() != pointsCount) {
                pts.add(new Point(rand.nextInt(RANGE), rand.nextInt(RANGE)));
            }

            Point[] points = pts.toArray(new Point[]{});
            Point[] pointsCopy = Arrays.copyOf(points, points.length);
            FCTree t = new FCTree(points);
            NaiveSolver ns = new NaiveSolver(pointsCopy);

            for (int i = 0; i < QUERIES; ++i) {
                int x1 = rand.nextInt(RANGE);
                int y1 = rand.nextInt(RANGE);
                int w = rand.nextInt(RANGE) + 1;
                int h = rand.nextInt(100) + 1;
                long time = System.nanoTime();
                Point[] tRes = t.getPointsByRect(x1, y1, w, h);
                long time1 = System.nanoTime();
                Point[] nsRes = ns.getPointsByRect(x1, y1, w, h);
                long time2 = System.nanoTime();
                tTime += time1 - time;
                nsTime += time2 - time1;

                if (tRes.length != nsRes.length) {
                    throw new AssertionError((pointsCount) + " test failed");
                } else {
                    Set<Point> result = new HashSet<>();
                    for (Point p : tRes) { result.add(p); }
                    for (Point p : nsRes) { if (!result.contains(p)) {
                        throw new AssertionError((pointsCount) + " test failed"); } }
                }
            }
            System.out.println("Points count: " + pointsCount + "\tCascading time: " + tTime / TIME_DIV + "\tNaive time: " + nsTime / TIME_DIV);
        }
    }
}