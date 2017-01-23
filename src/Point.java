public class Point {
    int x, y;
    Point(int x, int y) {
        this.x = x; this.y = y;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Point)obj).x == x && ((Point)obj).y == y;
    }
}