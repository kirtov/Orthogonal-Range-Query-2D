import java.util.ArrayList;
import java.util.List;

public class NaiveSolver {
    Point[] points;

    NaiveSolver(Point[] points) {
        this.points = points;
    }

    public Point[] getPointsByRect(int x1, int y1, int w, int h) {
        List<Point> ans = new ArrayList<>();
        int x2 = x1 + w - 1;
        int y2 = y1 + h - 1;
        for (Point p : points) {
            if (p.x >= x1 && p.x <= x2 && p.y >= y1 && p.y <= y2) {
                ans.add(p);
            }
        }
        return ans.toArray(new Point[]{});
    }
}