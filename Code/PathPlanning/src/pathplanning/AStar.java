package pathplanning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import pathplanning.util.Point;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author Alex
 */
public class AStar implements Pather {

    //Using hashes instad of arrays for overall memory savings on sparse grids.
    //Minor memory losses on dense grids. <25%.
    //Java hashmap loadfactor to resize = ~.75
    private final HashSet<Point> obs;
    private final HashMap<Point, Integer> prev;
    private final HashMap<Point, Integer> cost;

    private final PointIntHeap q = new PointIntHeap(16000); //Allocate early.
    private Point dest;
    public static final int MAX_PATH_LENGTH = 10000;
    private final Point[] temp = new Point[MAX_PATH_LENGTH]; //Allocate this one time.
    public int minX, minY, maxX, maxY;

    private final Logger callback;

    public AStar(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Logger callback) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.callback = callback;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
    }

    public AStar(int minX, int maxX, int minY, int maxY, HashSet<Point> obs) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.callback = null;
    }

    /**
     * Finds a path from some start to some finish.
     *
     * @param start
     * @param finish
     * @return An array of Points representing a path that does not intersect
     * any obstacles.
     */
    @Override
    public Point[] pathfind(Point start, Point finish) {
        prev.clear();
        cost.clear();

        cost.put(finish, 0);
        Point current;
        dest = start;
        final int desx = dest.x;
        final int desy = dest.y;
        for (int i = 7; i != -1; --i) {
            check(finish, i);
        }
        while (!q.isEmpty()) {
            current = q.pop();

            if (callback != null) {
                callback.report("pop", current.x, current.y);
            }

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
        if (callback != null) {
            callback.report("reconstruct");
        }
        Point current = dest;
        int count = 0;
        int dir = 0;
        do {
            int next = ((prev.get(current) + 4) & 7);
            if (dir == 0 || next != dir) { //this minimizes the path.
                temp[count++] = current;
                dir = next;
            }
            current = moveTo(current, next);
        } while (prev.containsKey(current));
        temp[count++] = current;
        final Point[] path = new Point[count];
        System.arraycopy(temp, 0, path, 0, count);
        return path;
    }

    //Optimization inspired by JPS.
    //Branching factor of ~3, instead of ~7.
    private void expand(Point p) {
        if (callback != null) {
            callback.report("expand", p.x, p.y);
        }
        final int dir = prev.get(p);
        if (!(p.x < maxX && p.y < maxY && p.x >= minX && p.y >= minY)) {
            return;
        }
        check(p, dir); //+0
        if ((dir & 1) == 1) { //is diagonal
            check(p, ((dir + 6) & 7)); //-2
            check(p, ((dir + 7) & 7)); //-1
            check(p, ((dir + 1) & 7)); //+1
            check(p, ((dir + 2) & 7)); //+2
        } else {
            check(p, ((dir + 7) & 7)); //-1
            check(p, ((dir + 1) & 7)); //+1
        }
    }

    /**
     * Uses Euclidean distance squared as heuristic, allowing us to keep
     * everything as ints.
     *
     * @param parent
     * @param dir
     */
    private void check(Point parent, int dir) {
        if (callback != null) {
            callback.report("eval", parent.x, parent.y, dir);
        }
        final Point n = moveTo(parent, dir);
        if (obs.contains(n)) {
            return;
        }
        int potentialCost = cost.get(parent) + parent.dist(n);
        if (!prev.containsKey(n)) { //has no parent, has not been considered
            int dis = n.dist(dest);
            if (callback != null) {
                callback.report("set", n.x, n.y, parent.x, parent.y, potentialCost, dis);
            }
            if (callback != null) {
                callback.report("push", n.x, n.y);
            }
            q.add(n, dis + potentialCost);
            prev.put(n, dir);
            cost.put(n, potentialCost);
        }
    }

    /**
     * Moves a Point one cell along direction d. Ugly but fast.
     *
     * @param p
     * @param d
     * @return
     */
    private static Point moveTo(Point p, int d) {
        switch (d) {
            case 0:
                return new Point(p.x, p.y - 1);
            case 1:
                return new Point(p.x + 1, p.y - 1);
            case 2:
                return new Point(p.x + 1, p.y);
            case 3:
                return new Point(p.x + 1, p.y + 1);
            case 4:
                return new Point(p.x, p.y + 1);
            case 5:
                return new Point(p.x - 1, p.y + 1);
            case 6:
                return new Point(p.x - 1, p.y);
            default:
                return new Point(p.x - 1, p.y - 1);
        }
    }

    /**
     * New obstacle will be considered on next run.
     * If you modify the original obs HashSet, that also works. 
     * This allows you to not maintain a reference to it.
     * @param p
     */
<<<<<<< HEAD
    public void addObs(Point p) {obs.add(p);}
=======
    public void addObs(Point p) {
        obs.add(p);
    }
>>>>>>> 695dec1113618bc1e622f73a2c10783e276b6172

}
