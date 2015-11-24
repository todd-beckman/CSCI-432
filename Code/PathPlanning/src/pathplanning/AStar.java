package pathplanning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Arrays;
import pathplanning.util.Point;
import java.util.HashMap;
import java.util.HashSet;
import pathplanning.util.Heap;

/**
 *
 * @author Alex
 */
public class AStar {

    //Using hashes instad of arrays for overall memory savings on sparse grids.
    //Minor memory losses on dense grids. <25%.
    //Java hashmap loadfactor to resize = ~.75
    private final HashSet<Point> obs; //set of obstacles
    private final HashMap<Point, Integer> prev; //Map for pointing to parent
    private final HashMap<Point, Integer> cost; //Map holding cost of point

    private final Heap<Point> q = new Heap<>(80000); //Allocate early.
    private Point dest;
    public static final int MAX_PATH_LENGTH = 10000;
    private final Point[] temp = new Point[MAX_PATH_LENGTH]; //Allocate this one time.
    public int minX, minY, maxX, maxY;

    private final Logger callback;
    public final Point[] path_buffer;

    /**
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param obs
     * @param callback Logger to receive logging information. This info is
     * probably not useful to most people.
     */
    public AStar(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Logger callback) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.callback = callback;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.path_buffer = null;
    }

    /**
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param obs HashSet of obstacles.
     */
    public AStar(int minX, int maxX, int minY, int maxY, HashSet<Point> obs) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.callback = null;
        this.path_buffer = null;
    }

    /**
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     * @param obs HashSet of obstacles
     * @param path_buffer Array to hold path after reconstruction. 
     */
    public AStar(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Point[] path_buffer) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.callback = null;
        this.path_buffer = path_buffer;
    }

    public AStar(HashSet<Point> obs) {
        this.obs = obs;
        this.minX = Integer.MIN_VALUE;
        this.minY = Integer.MIN_VALUE;
        this.maxX = Integer.MAX_VALUE;
        this.maxY = Integer.MAX_VALUE;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.callback = null;
        this.path_buffer = null;
    }

    public AStar() {
        this.obs = new HashSet<>();
        this.minX = Integer.MIN_VALUE;
        this.minY = Integer.MIN_VALUE;
        this.maxX = Integer.MAX_VALUE;
        this.maxY = Integer.MAX_VALUE;
        this.prev = new HashMap<>();
        this.cost = new HashMap<>();
        this.callback = null;
        this.path_buffer = null;
    }

    /**
     * Finds a path from some start to some finish.
     *
     * @param start
     * @param finish
     * @return An array of Points representing a path that does not intersect
     * any obstacles.
     */
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
        while (q.len() != 0) {
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
        int next;
        do {
            next = ((prev.get(current) + 4) & 7);
            if (dir == 0 || next != dir) { //this minimizes the path.
                temp[count++] = current;
                dir = next;
            }
            current = moveTo(current, next);
        } while (prev.containsKey(current));
        temp[count++] = current;
        if (path_buffer != null && path_buffer.length > count) {
            System.arraycopy(temp, 0, path_buffer, 0, count);
            Arrays.fill(path_buffer, count, path_buffer.length, null);
            return path_buffer;
        }
        final Point[] path = new Point[count];
        System.arraycopy(temp, 0, path, 0, count);
        return path;
    }

    /**
     * This takes a single point and checks all it's neighbors for viability.
     *
     * @param p Point (Vertex) to expand.
     */
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
     * Checks a child vertex to potentially set it's value and add to queue.
     * Uses distance (see point for definition of "distance" from point to goal
     * as heuristic.
     *
     * @param parent Parent vertex
     * @param dir direction to some child of parent.
     */
    private void check(Point parent, int dir) {
        if (callback != null) {
            callback.report("eval", parent.x, parent.y, dir);
        }
        final Point n = moveTo(parent, dir);
        if (obs.contains(n)) {
            callback.report("obs", n.x,n.y);
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
            n.cost = dis + potentialCost;
            q.insert(n);
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
     * New obstacle will be considered on next run. If you modify the original
     * obs HashSet, that also works. This allows you to not maintain a reference
     * to it.
     *
     * @param p
     */
    public void addObs(Point p) {obs.add(p);}

    /**
     * int dx1 = current.x - goal.x;
     *
     * int dy1 = current.y - goal.y;
     *
     * int dx2 = start.x - goal.x;
     *
     * int dy2 = start.y - goal.y;
     *
     * int cross = abs(dx1*dy2 - dx2*dy1);
     *
     * int heuristic += cross*0.001;
     */
}
