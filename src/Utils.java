public class Utils {

    static final int binSearchByX(Point[] pts, int val, int l, int r) {
        int midVal;
        while (l < r) {
            int midInd = l + (r - l) / 2;
            midVal = pts[midInd].x;
            if (midVal < val) {
                l = midInd + 1;
            } else {
                r = midInd;
            }
        }
        return r;
    }

    static final int binSearchByY(Point[] pts, int val, int l, int r) {
        int midVal;
        while (l < r) {
            int midInd = l + (r - l) / 2;
            midVal = pts[midInd].y;
            if (midVal < val) {
                l = midInd + 1;
            } else {
                r = midInd;
            }
        }
        return r;
    }
}