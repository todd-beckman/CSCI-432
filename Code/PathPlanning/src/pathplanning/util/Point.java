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
public class Point {
    public final int x;
    public final int y;
    private static final double sqrt2 = 1.41421356237;
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    /**
     * @param p
     * @return Euclidean distance squared
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
    
}
