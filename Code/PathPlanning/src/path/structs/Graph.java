/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package path.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author alexhuleatt
 */
public class Graph<E> {

    private final HashMap<E, Vertex> map;

    private class Vertex {

        public final HashMap<Vertex, Edge> succ;
        public final HashMap<Vertex, Edge> pred;

        public final E key;

        public Vertex(E key) {
            this.succ = new HashMap<>();
            this.pred = new HashMap<>();
            this.key = key;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.key);
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Graph<?>.Vertex) {
                return ((Vertex) o).key.equals(key);
            }
            return false;
        }

    }

    private class Edge {

        public final Vertex from;
        public final Vertex to;
        public double cost;

        public Edge(Vertex from, Vertex to, double cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    public Graph() {
        this.map = new HashMap<>();
    }

    public void addVertex(E e) {
        Vertex v = new Vertex(e);
        map.put(e, v);
    }

    public void addEdge(E from, E to, double init_cost) {
        Vertex vfrom = map.get(from);
        Vertex vto = map.get(to);
        Edge e = new Edge(vfrom, vto, init_cost);
        vfrom.succ.put(vto, e);
        vto.pred.put(vfrom, e);
    }

    public void updateEdge(E from, E to, double new_cost) {
        map.get(from).succ.get(map.get(to)).cost = new_cost; //lol
    }

    public ArrayList<E> succ(E e) {
        Vertex v = map.get(e);
        if (v == null) {
            return null;
        }
        ArrayList<Edge> edges = new ArrayList<>(v.succ.values());
        ArrayList<E> toRet = new ArrayList<>();
        for (Edge edg : edges) {
            toRet.add(edg.to.key);
        }
        return toRet;
    }

    public ArrayList<E> pred(E e) {
        Vertex v = map.get(e);
        if (v == null) {
            return null;
        }
        ArrayList<Edge> edges = new ArrayList<>(v.pred.values());
        ArrayList<E> toRet = new ArrayList<>();
        for (Edge edg : edges) {
            toRet.add(edg.from.key);
        }
        return toRet;
    }

    public double edgeCost(E from, E to) {
        Vertex vfrom = map.get(from);
        Vertex vto = map.get(to);
        Edge e = vfrom.succ.get(vto);
        return e.cost;

    }

    public void removeEdge(E from, E to) {
        Vertex v = map.get(from);
        Vertex toRem = map.get(to);
        if (v.succ.containsKey(toRem)) {
            v.succ.remove(toRem);

        }
    }

    public static Graph<Point> gridToGraph(boolean[][] grid) {
        Graph<Point> g = new Graph<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j]) {
                    g.addVertex(new Point(i, j));
                }
            }
        }
        int sx, sy;
        Point temp, s;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (!grid[i][j]) {
                    temp = new Point(i, j);
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            if (k != 0 || l != 0) {
                                sx = i + k;
                                sy = j + l;
                                s = new Point(sx, sy);
                                if (sx >= 0 && sx < grid.length && sy >= 0 && sy < grid[sx].length && !grid[sx][sy]) {
                                    g.addEdge(temp, s, temp.dist(s));
                                }
                            }
                        }
                    }
                }
            }
        }
        return g;
    }
}
