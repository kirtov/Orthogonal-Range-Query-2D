import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BinTree {
    Node root;
    int leftYInd, rightYInd;
    int x1,x2,y1,y2;

    public BinTree(Point[] points) {
        Point[] yPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(points, (Point p1, Point p2) -> (p1.x == p2.x) ? (p1.y - p2.y) : (p1.x - p2.x));
        Arrays.sort(yPoints, (Point p1, Point p2) -> (p1.y - p2.y));
        root = build(points, 0, points.length, yPoints);
    }

    Node build(Point[] xPoints, int l, int r, Point[] yPoints) {
        if (r - l == 0) return null;
        int mid = l + (r - l) / 2;
        //If middle and another points has equal X
        //We need to place all this points in one subtree
        int xElementsLeft = mid - Utils.binSearchByX(xPoints, xPoints[mid].x, l, r);
        Point[] yLeft = new Point[mid - l];
        Point[] yRight = new Point[r - mid - 1];
        int lLinks[] = new int[r - l];
        int rLinks[] = new int[r - l];
        int mid_l = 0;
        int mid_r = 0;
        int i = 0;
        for (Point p : yPoints) {
            lLinks[i] = mid_l;
            rLinks[i++] = mid_r;
            if (xPoints[mid].equals(p)) continue;
            if (p.x == xPoints[mid].x) {
                if (xElementsLeft != 0) {
                    xElementsLeft--;
                    yLeft[mid_l++] = p;
                } else {
                    yRight[mid_r++] = p;
                }
            } else if (p.x < xPoints[mid].x) {
                yLeft[mid_l++] = p;
            } else {
                yRight[mid_r++] = p;
            }
        }
        Node ans = new Node(xPoints[mid], build(xPoints, l, mid, yLeft), build(xPoints, mid + 1, r, yRight));
        ans.yPoints = yPoints;
        ans.lLinks = lLinks;
        ans.rLinks = rLinks;
        return ans;
    }

    //Find points
    List<Point> findSubTree(Node curNode, int yInd) {
        List<Point> ans = new ArrayList<>();
        if (curNode == null) return ans;
        while (yInd < curNode.yPoints.length && curNode.yPoints[yInd].y <= y2) {
            ans.add(curNode.yPoints[yInd]);
            yInd++;
        }
        return ans;
    }

    //Find points from right subtree
    List<Point> findRight(Node curNode) {
        List<Point> ans = new ArrayList<>();
        if (curNode == null) return ans;
        if (curNode.p.x <= x2) {
            if (rightYInd < curNode.rLinks.length) {
                ans = findSubTree(curNode.l, curNode.lLinks[rightYInd]);
                if (curNode.r != null)
                    rightYInd = Utils.binSearchByY(curNode.r.yPoints, y1, 0, curNode.r.yPoints.length);
            }
            ans.addAll(findRight(curNode.r));
            if (curNode.p.y <= y2 && curNode.p.y >= y1)
                ans.add(curNode.p);
        } else {
            if (rightYInd < curNode.rLinks.length)
                if (curNode.r != null)
                    rightYInd = Utils.binSearchByY(curNode.r.yPoints, y1, 0, curNode.r.yPoints.length);
            ans.addAll(findRight(curNode.l));
        }
        return ans;
    }

    //Find points from left subtree
    List<Point> findLeft(Node curNode) {
        List<Point> ans = new ArrayList<>();
        if (curNode == null) return ans;
        if (curNode.p.x >= x1) {
            if (leftYInd < curNode.lLinks.length) {
                ans.addAll(findSubTree(curNode.r, curNode.rLinks[leftYInd]));
                if (curNode.l != null)
                    leftYInd = Utils.binSearchByY(curNode.l.yPoints, y1, 0, curNode.l.yPoints.length);
            }
            ans.addAll(findLeft(curNode.l));
            if (curNode.p.y <= y2 && curNode.p.y >= y1)
                ans.add(curNode.p);
        } else {
            if (leftYInd < curNode.lLinks.length)
                if (curNode.l != null)
                    leftYInd = Utils.binSearchByY(curNode.l.yPoints, y1, 0, curNode.l.yPoints.length);
            ans.addAll(findLeft(curNode.r));
        }
        return ans;
    }

    boolean nodeIsNotInBound(Node curNode, int a, int b) {
        return (curNode.p.x != a && curNode.p.x != b) && ((curNode.l != null && a < curNode.p.x && b < curNode.p.x) || (curNode.r != null && a > curNode.p.x && b > curNode.p.x));
    }

    boolean nodeIsInBound(Node curNode) {
        return (curNode.p.y <= y2 && curNode.p.y >= y1 && curNode.p.x <= x2 && curNode.p.x >= x1);
    }

    public Point[] getPointsByRect(int x1, int y1, int w, int h) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1 + w - 1;
        this.y2 = y1 + h - 1;
        Node curNode = root;

        //Find node in [x1,x2]
        while (nodeIsNotInBound(curNode, x1, x2)) {
            if (curNode.l != null && x1 < curNode.p.x && x2 < curNode.p.x) {
                curNode = curNode.l;
            }
            else {
                curNode = curNode.r;
            }
        }

        //Find right and left indices in y arrays
        if (curNode.r != null)
            rightYInd = Utils.binSearchByY(curNode.r.yPoints, y1, 0, curNode.r.yPoints.length);
        if (curNode.l != null)
            leftYInd = Utils.binSearchByY(curNode.l.yPoints, y1, 0, curNode.l.yPoints.length);

        //Accumulate points from left and right subtrees
        List<Point> ans = findRight(curNode.r);
        ans.addAll(findLeft(curNode.l));
        if (nodeIsInBound(curNode)) ans.add(curNode.p);
        return ans.toArray(new Point[]{});
    }

    class Node {
        Point p;
        Node l, r;
        Point[] yPoints;
        int rLinks[], lLinks[];

        Node(Point root, Node l, Node r) {
            p = root;
            this.l = l;
            this.r = r;
        }
    }
}