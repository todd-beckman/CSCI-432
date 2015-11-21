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
    public HashMap<Point, Integer> cost;
    private HashMap<Point, Integer> RHS;
    private HashSet<Point> closed_set;

    private final Heap<Point> q = new Heap(8000); //Allocate early.
    private int index;
    private Point last_point;
    private Point first_point;
    public static final int MAX_PATH_LENGTH = 400;
    private final Point[] temp = new Point[MAX_PATH_LENGTH]; //Allocate this one time.
    public int minX, minY, maxX, maxY;
    public static int myMAX = 1000000;
    

    private final Logger callback;

    public DStarLite(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Logger callback) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.obs = obs;
        this.callback = callback;
        RHS = new HashMap<>();
        closed_set = new HashSet<>();
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
        cost = new HashMap<>();
        
        cost.put(finish, myMAX);
        RHS.put(finish, 0);
        cost.put(start, myMAX);
        RHS.put(start, myMAX);
        q.insert(finish);
        Point current;
        last_point = start;
        first_point = finish;
        first_point.pairCost=getKey(first_point);
        first_point.usePair=true;
        
        last_point.pairCost=getKey(last_point);
        last_point.usePair = true;
        
        
        while (q.len() > 0 && (q.peek().compareTo(last_point) < 0 || 
                getRHS(last_point) != cost.get(last_point))) { //change while case
            do {
                current = q.pop();
            } while (closed_set.contains(current));
            
            if (current == null) break;
            int rhs = getRHS(current);
            if (cost.get(current) > rhs) {
                cost.put(current,rhs);
            } else {
                cost.put(current, myMAX);
                
                updateState(current);
            }
            if (callback != null) {
                callback.report("pop", current.x, current.y);
            }
            expand(current);
            //System.out.println(q.peek());
            //System.out.println(q + " " + getKey(first_point) + " ");
        }
        return null;
    }

    private void expand(Point p) {
        if (callback != null) {
            callback.report("expand", p.x, p.y);
        }
        if (!(p.x < maxX && p.y < maxY && p.x >= minX && p.y >= minY)) {
            return;
        }
        for (int i = 0; i < 8; i++) {
            check(p,i);
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
        if (obs.contains(p)) return myMAX;
        if (p.equals(first_point)) {
            return 0;
        }
        int min = myMAX;
        int calc;
        for (int i = 0; i < 8; i++) {
            Point cur = moveTo(p, i);
            if (!cost.containsKey(cur)) {
                calc = myMAX;
            } else {
                int t_cost = cost.get(cur);
                if (t_cost >= myMAX) {
                    calc = t_cost;
                }
                else {
                    calc = cost.get(cur) + p.dist(cur);
                }
            }
            if (calc < min) {
                min = calc;
            }
        }
        return min;
    }

    public Pair<Integer, Integer> getKey(Point p) {
        if (obs.contains(p)) {
            return new Pair<>(myMAX,myMAX);
        }
        return new Pair<>(
                Math.min(cost.get(p), getRHS(p)) + last_point.dist(p),
                Math.min(cost.get(p), getRHS(p)));
    }
    
    public void updateState(Point p) {
        if (!cost.containsKey(p)) {
            cost.put(p,myMAX);
        }
        if (!p.equals(last_point)) {
            RHS.put(p,getRHS(p));
        }
        if (!closed_set.contains(p)) {
            closed_set.add(p);
        } 
        
        if (cost.get(p) != getRHS(p)) {
            p.pairCost = getKey(p);
            p.usePair = true;
            q.insert(p);
            closed_set.remove(p);
        }
    }

}
