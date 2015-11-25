/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package path.algs;

import path.structs.Graph;
import path.Logger;
import path.structs.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 *
 * @author alexhuleatt
 */
public class AStar {
    private Graph<Point> g;
    private final Logger callback;
    public AStar(Graph<Point> g, Logger callback) {
        this.g=g;
        this.callback=callback;
    }
    
    public void addObstacle(Point p) {
        //adding an obstacle means removing all the edges from it to it's 
        //neighbors.
        //and then removing theirs to it.
        
        ArrayList<Point> neigh = g.succ(p);
        for (Point n : neigh) {
            g.removeEdge(p, n);
            g.removeEdge(n, p);
        }
    }
    
    public ArrayList<Point> pathfind(Point start, Point end) {
        PriorityQueue<Point> q = new PriorityQueue();
        HashMap<Point<Double>,Point<Double>> parents = new HashMap<>();
        HashMap<Point<Double>,Double> g_vals= new HashMap<>();
        end.cost = 0;
        g_vals.put(end, 0.0);
        Point<Double> current;
        q.add(end);
        while (q.size() > 0) {
            current = q.poll();
            callback.report("pop", current.x, current.y);
            double current_cost = g_vals.get(current);
            ArrayList<Point> succ = g.succ(current);
            for (Point p : succ) {  
                if (!parents.containsKey(p)) {
                    parents.put(p, current);
                    double p_cost = current_cost + current.dist(p);
                    g_vals.put(p, p_cost);
                    g.updateEdge(current, p, p_cost);
                    p.cost=p_cost+p.dist(start); //set f cost.
                    q.add(p);
                    callback.report("push", p.x,p.y);
                } 
            }
            if (current.equals(start)){
                break;
            }
        }
        current = start;
        ArrayList<Point> path = new ArrayList<>();
        while (!current.equals(end)) {
            path.add(current);
            current = parents.get(current);
        }
        path.add(end);
        return path;
    }
}
