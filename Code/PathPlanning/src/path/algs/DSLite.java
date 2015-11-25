/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package path.algs;

import path.Logger;
import path.structs.Pair;
import path.structs.Point;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import path.structs.Graph;

/**
 *
 * @author alexhuleatt
 */
public class DSLite {

    private final Graph<Point> g;
    private final HashMap<Point, Double> g_costs;
    private final HashMap<Point, Double> rhs_costs;
    public static final double INFINITY = Double.MAX_VALUE;
    public final PriorityQueue<Point> q;
    private Point sgoal;
    private Point sstart;
    private Logger callback;

    public DSLite(Graph<Point> g, Logger callback) {
        this.g = g;
        g_costs = new HashMap<>();
        rhs_costs = new HashMap<>();
        q = new PriorityQueue<>();
        this.callback = callback;
    }

    public void addObstacle(Point p) {
        //idk how D* Lite handles removal of edges, so we're just going
        //to increase their value. *shrug*
        ArrayList<Point> neigh = g.succ(p);

        for (Point n : neigh) {
            updateEdgeCost(p, n, INFINITY);
            updateEdgeCost(n, p, INFINITY);
            updateState(n);
        }
        updateState(p);
    }

    private void updateEdgeCost(Point from, Point to, double newVal) {
        g.updateEdge(from, to, newVal);
    }

    public void initialize(Point start, Point end) {
        this.sgoal = new Point(end.x, end.y);

        this.sstart = new Point(start.x, start.y);
        //starting vals.
        g_costs.put(sstart, INFINITY);
        rhs_costs.put(sstart, INFINITY);
        g_costs.put(sgoal, INFINITY);
        rhs_costs.put(sgoal, 0.0);

        sgoal.cost = key(sgoal);
        q.add(sgoal);
        resolve(sstart);
    }

    public void resolve(Point ss) {
        Point current;
        while ((key(q.peek()).compareTo(key(ss)) < 0
                || !rhs_costs.get(ss).equals(g_costs.get(ss)))) {
            current = q.poll();
            callback.report("pop", current.x, current.y);
            double current_g = g_costs.get(current);
            double current_rhs = rhs_costs.get(current);

            if (current_g > current_rhs) {

                g_costs.put(current, current_rhs);
                updatePredRHS(current);
                for (Point p : g.pred(current)) {
                    updateState(p);
                }

            } else {
                g_costs.put(current, INFINITY);
                updatePredRHS(current);

                for (Point p : g.pred(current)) {
                    updateState(p);
                }
                updateState(current);
            }
        }

    }

    public ArrayList<Point> pathfind(Point start) {
        resolve(start);
        Point current = start;
        Point<Pair<Double, Double>> temp;
        ArrayList<Point> path = new ArrayList<>();
        while (!current.equals(sgoal)) {
            path.add(current);
            ArrayList<Point> pred = g.pred(current);
            temp = null;
            //System.out.println(current + " " + pred);
            for (Point p : pred) {
                if (g_costs.containsKey(p) && (temp == null || g_costs.get(p) < g_costs.get(temp))) {

                    temp = p;
                }
            }
            current = temp;
        }
        return path;
    }

    private Pair<Double, Double> key(Point<Pair<Double, Double>> p) {
        double first = Math.min(
                g_costs.get(p),
                rhs_costs.get(p));

        return new Pair<>(first + p.dist(sstart), first);
    }

    private double getRHS(Point p) {
        if (p.equals(sgoal)) {
            return 0;
        }
        ArrayList<Point> succ = g.succ(p);
        double min = INFINITY;
        double cost;
        for (Point n : succ) {

            if (g_costs.containsKey(n)) {

                cost = g_costs.get(n) + g.edgeCost(p, n);
                min = Math.min(min, cost);
            }
        }
        return min;
    }

    private void updatePredRHS(Point p) {
        ArrayList<Point> neigh = g.pred(p);
        for (Point n : neigh) {
            rhs_costs.put(n, getRHS(n));
        }
    }

    private void updateState(Point p) {
        if (!g_costs.containsKey(p)) {
            g_costs.put(p, INFINITY);
            updatePredRHS(p);
        }
        if (!p.equals(sgoal)) {
            rhs_costs.put(p, getRHS(p));
        }
        if (q.contains(p)) {
            q.remove(p);
            callback.report("remove", p.x, p.y);
        }
        //System.out.println(p + " " + g_costs.get(p) + " " + rhs_costs.get(p));
        if (!g_costs.get(p).equals(rhs_costs.get(p))) {
            p.cost = key(p);
            q.add(p);
            callback.report("push", p.x, p.y);
        }

    }
}
