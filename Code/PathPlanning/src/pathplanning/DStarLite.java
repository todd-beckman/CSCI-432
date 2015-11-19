package pathplanning;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import pathplanning.util.Point;
import java.util.HashMap;
import java.util.HashSet;
import pathplanning.util.Heap;
import pathplanning.util.Pair;

/**
 *
 * @author Alex
 */
public class DStarLite implements Pather {

    //Using hashes instad of arrays for overall memory savings on sparse grids.
    //Minor memory losses on dense grids. <25%.
    //Java hashmap loadfactor to resize = ~.75
    private final HashSet<Point> obs;
    private HashMap<Point, Integer> prev;
    private HashMap<Point, Integer> cost;
    private HashMap<Point, Integer> RHS;
    private HashSet<Point> closed_set;

    private final Heap<Point> q = new Heap(8000); //Allocate early.
    private int index;
    private Point dest;
    public static final int MAX_PATH_LENGTH = 400;
    private final Point[] temp = new Point[MAX_PATH_LENGTH]; //Allocate this one time.
    public int minX, minY, maxX, maxY;
    private Point begin;

    private final Logger callback;

    public DStarLite(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Logger callback) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.callback = callback;
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
        prev = new HashMap<>();
        cost = new HashMap<>();
        cost.put(finish, 0);
        index = 0;
        Point current;
        dest = start;
        final int desx = dest.x;
        final int desy = dest.y;
        for (int i = 7; i != -1; --i) {
            check(finish, i);
        }
        while (index != 0) { //change while case
            
            do {
                current = q.pop();
            } while (closed_set.contains(current));
            int rhs = getRHS(current);
            if (cost.get(current) > rhs) {
                cost.put(current,rhs);
            } else {
                cost.put(current, Integer.MAX_VALUE);
                
                updateState(current);
            }
            if (callback != null) {
                callback.report("pop", current.x, current.y);
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
        } while (prev.get(current) != 0);
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
        if (dir == 0 || !(p.x < maxX && p.y < maxY && p.x >= minX && p.y >= minY)) {
            return;
        }
        check(p, dir);
        if ((dir & 1) == 0) { //is diagonal
            check(p, ((dir + 7) & 7)); //-1
            check(p, ((dir + 1) & 7)); //+1
            check(p, ((dir + 6) & 7));
            check(p, ((dir + 2) & 7));
        } else {
            check(p, ((dir + 7) & 7));
            check(p, ((dir + 1) & 7));
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
        updateState(n);
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
     *
     * @param p
     */
    public void addObs(Point p) {
        obs.add(p);
    }

    public int getRHS(Point p) {
        if (p == begin) {
            return 0;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= 8; i++) {
            Point cur = moveTo(p, i);
            int calc = cost.get(cur) + p.dist(cur);
            if (calc < min) {
                min = calc;
            }
        }
        return min;
    }

    public Pair<Integer, Integer> getKey(Point p) {
        return new Pair<>(
                Math.min(cost.get(p), getRHS(p)) + dest.dist(p),
                Math.min(cost.get(p), getRHS(p)));
    }
    
    public void updateState(Point p) {
        if (!cost.containsKey(p)) {
            cost.put(p,Integer.MAX_VALUE);
        }
        if (!p.equals(dest)) {
            RHS.put(p,getRHS(p));
        }
        if (!closed_set.contains(p)) {
            closed_set.add(p);
        } 
        
        if (cost.get(p) != getRHS(p)) {
            p.pairCost = getKey(p);
            p.usePair = true;
            q.insert(p);
        }
    }

}
