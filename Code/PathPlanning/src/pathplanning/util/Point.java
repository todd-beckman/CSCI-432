/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning.util;

/**
 *
 * @author alexhuleatt
 */
public class Point implements Comparable {
    public final int x;
    public final int y;
    public int cost;
    public Pair<Integer,Integer> pairCost;
    public boolean usePair = false;
    private static final double sqrt2 = 1.41421356237;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    /**
     * Octile distance rounded down.
     * Euclidean distance squared does not satisfy the triangle inequality and is not a valid metric.
     * This is really meant for path planning, where this is "good enough"
     * Didn't want to calculate square root. 3Expensive5Me.
     * @param p
     * @return 
     */
    public int dist(Point p) {
        int tx = Math.abs(p.x-x);
        int ty = Math.abs(p.y-y);
        int tp = Math.min(tx, ty); //number of diagonals
        double tp2 = sqrt2*tp + (tx+ty) - tp;
        return (int)Math.round(tp2); //close enough for my needs.
        
    }
    
    @Override
    public boolean equals(Object o) {
        final Point t;
        if (o instanceof Point) {
            t = (Point)o;
            return (t.x==x&&t.y==y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
        return hash;
    }
    
    @Override
    public String toString() {
        return "("+x+","+y+"):"+ ((usePair)?pairCost:cost);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Point)) {
            System.out.println("wat");
            return 0;
        }
        if (!usePair) return cost - ((Point)o).cost;
        else {
            Point p = (Point)o;
            int k1 = pairCost.a;
            int k2 = pairCost.b;
            int pk1 = p.pairCost.a;
            int pk2 = p.pairCost.b;
            
            if (k1 < pk1) {
                return -1;
            } else if (k1 > pk1) {
                return 1;
            } else {
                return k2 - pk2;
            }
        }
    }
}
