package pathplanning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Alex
 */
public class AStar {

    private HashMap<Point,Integer> prev;
    private HashMap<Point,Double> cost;
    private final double[] costs;
    private final PointDoubleHeap q;
    private int index;
    private Point dest;
    public static final int MAX_PATH_LENGTH = 800;

    int minX = 0;
    int maxX;
    int minY = 0;
    int maxY;
    
    private final HashSet<Point> obs;


    public AStar(int maxX, int maxY, HashSet<Point> obs) {
        this.maxX=maxX;
        this.maxY=maxY;
        prev = new HashMap<>();
        costs = new double[8000];
        q = new PointDoubleHeap(8000);
        cost = new HashMap<>();
        this.obs = obs;
    }

    /**
     * Finds a path from some start to some finish. This give absolutely no
     * guarantees about path optimality, in fact, I reasonably guarantee that
     * this will tend not to do that.
     *
     * @param start
     * @param finish
     * @return An array of Points representing a path that does not
     * intersect any obstacles.
     */
    public Point[] pathfind(Point start, Point finish) {
        prev = new HashMap<>();
        cost = new HashMap<>();
        index = 0;
        Point current;
        dest = start;
        final int desx = dest.x;
        final int desy = dest.y;
        for (int i = 8; i != 0; --i) {
            check(finish, i);
        }
        while (index != 0) {
            current = q.pop();
            if (current.x == desx && current.y == desy) {
                return reconstruct();
            }
            expand(current);
        }
        return null;
    }

    /**
     * Reconstruct the path.
     *
     * @return
     */
    private Point[] reconstruct() {
        Point current = dest;
        final Point[] path_temp = new Point[MAX_PATH_LENGTH];
        int count = 0;
        int dir = 0;
        do {
            int next = ((prev.get(current) + 3) & 7) + 1;
            if (dir == 0 || next != dir) { //this minimizes the path, for efficient radio-ing
                path_temp[count++] = current;
                dir = next;
            }
            current = moveTo(current, next);
        } while (prev.get(current) != 0);
        path_temp[count++] = current;
        final Point[] path = new Point[count];
        System.arraycopy(path_temp, 0, path, 0, count);
        return path;
    }

    private void expand(Point p) {
        final int dir = prev.get(p);
        if (dir == 0) {
            return;
        }
        check(p, dir);
        if ((dir & 1) == 0) { //is diagonal
            check(p, ((dir + 6) & 7) + 1); //-1
            check(p, (dir & 7) + 1); //+1
            check(p, ((dir + 5) & 7) + 1);
            check(p, ((dir + 1) & 7) + 1);
        } else {
            check(p, ((dir + 6) & 7) + 1);
            check(p, (dir & 7) + 1);
        }
    }

    private void check(Point parent, int dir) {
        final Point n = moveTo(parent, dir);
        if (obs.contains(n)) {
            return;
        }
        final int nx = n.x;
        final int ny = n.y;
        double potentialCost = cost.get(parent) + parent.dist(n);
        if (prev.get(n) == 0 || cost.get(n) > potentialCost) {
            q.add(n, n.dist(dest) + potentialCost);
            prev.put(n,dir);
            cost.put(n,potentialCost);
        }
    }

    /**
     * Moves a Point one cell along direction d.
     *
     * @param p
     * @param d
     * @return
     */
    private static Point moveTo(Point p, int d) {
        switch (d) {
            case 1:
                return new Point(p.x, p.y - 1);
            case 2:
                return new Point(p.x + 1, p.y - 1);
            case 3:
                return new Point(p.x + 1, p.y);
            case 4:
                return new Point(p.x + 1, p.y + 1);
            case 5:
                return new Point(p.x, p.y + 1);
            case 6:
                return new Point(p.x - 1, p.y + 1);
            case 7:
                return new Point(p.x - 1, p.y);
            default:
                return new Point(p.x - 1, p.y - 1);
        }
    }


}