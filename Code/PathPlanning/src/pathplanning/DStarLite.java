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
public class DStarLite {

    //Using hashes instad of arrays for overall memory savings on sparse grids.
    //Minor memory losses on dense grids. <25%.
    //Java hashmap loadfactor to resize = ~.75
    public final HashMap<Point, Integer> cost;
    private final HashMap<Point, Integer> rhs;

    private final Heap<Point> q = new Heap(800000); //Allocate early.
    private Point last_point;
    private Point first_point;
    public int minX, minY, maxX, maxY;
    public static int myMAX = 1000000;

    private final Logger callback;

    public DStarLite(int minX, int maxX, int minY, int maxY, HashSet<Point> obs, Logger callback) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.callback = callback;
        rhs = new HashMap<>();
        cost = new HashMap<>();
        for (Point o : obs) {
            cost.put(o, -1);
            rhs.put(o, -1);
        }
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

        Point current;
        last_point = start;
        first_point = finish;
        setCost(first_point, myMAX);
        rhs.put(first_point, 0);
        setCost(last_point, myMAX);
        rhs.put(last_point, myMAX);
        q.insert(first_point);
        first_point.pairCost = getKey(first_point,myMAX,0);
        first_point.usePair = true;

        last_point.pairCost = getKey(last_point,myMAX,myMAX);
        last_point.usePair = true;

        while (q.len() > 0 && (q.peek().compareTo(last_point) < 0
                || ((int)rhs.get(last_point)) != cost.get(last_point))) {

            current = q.pop();

            int temp_rhs = getRHS(current);
            if (temp_rhs == -1 || !(current.x >= minX && current.y >= minY
                    && current.x < maxX && current.y < maxY)) {
                continue;
            }
            int temp_cost = cost.get(current);
            if (temp_cost > temp_rhs) {
                setCost(current, temp_rhs);
            } else {
                setCost(current, myMAX);
                updateState(current, temp_cost, temp_rhs);
            }
            for (int i = 0; i < 8; ++i) {
                updateState(moveTo(current, i));
            }
            if (callback != null) {
                callback.report("pop", current.x, current.y);
            }
        }
        return null;
    }

    private void setCost(Point p, int n) {
        cost.put(p, n);
        updateNeighborRHS(p, n);
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

    public int getRHS(Point p) {
        if (p.equals(first_point)) {
            return 0;
        }
        return rhs.get(p);

    }

    public Pair<Integer, Integer> getKey(Point p, int temp_cost, int temp_rhs) {
        int ncost = Math.min(temp_cost, temp_rhs);
        return new Pair<>(ncost + last_point.dist(p), ncost);
    }
    private void updateNeighborRHS(Point p, int ncost) {
        for (int i = 0; i < 8; ++i) {
            Point n = moveTo(p, i);
            int dis = p.dist(n);
            Integer cos = cost.get(n);
            if (cos == null) {
                rhs.put(n, ncost + dis);
                cost.put(n, myMAX);
            } else if (cos > ncost + dis) {
                rhs.put(n, ncost + dis);
            }
        }
    }

    public void updateState(Point p) {
        updateState(p, cost.get(p), getRHS(p));
    }

    public void updateState(Point p, int temp_cost, int temp_rhs) {
        if (!p.equals(last_point)) {
            rhs.put(p, temp_rhs);
        }
        if (q.contains(p)) {
            q.remove(p);
        }
        if (temp_cost != temp_rhs) {
            p.pairCost = getKey(p, temp_cost, temp_rhs);
            p.usePair = true;
            q.insert(p);
        }
    }

}
