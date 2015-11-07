/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning;

/**
 *
 * @author alexhuleatt
 */
public class Point {
    public final int x;
    public final int y;
    
    public Point(int x, int y) {
        this.x=x;
        this.y=y;
    }
    
    /**
     * @param p
     * @return Euclidean distance squared
     */
    public int dist(Point p) {
        final int tx = p.x-x;
        final int ty = p.y-y;
        return tx*tx+ty*ty;
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
