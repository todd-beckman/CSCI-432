/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning;

import grid.WTFGrid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import pathplanning.util.Point;

/**
 *
 * @author alexhuleatt
 */
public class Test {
    
    public static void main(String[] args) {
//        Logger stdoutLogger = new Logger() {
//            @Override
//            public void report(String st, int... args) {
//                System.out.println(st + " " + Arrays.toString(args));
//            }
//        };
//        
//        AStar a = new AStar(0, 100, 0, 100, new HashSet<Point>(), stdoutLogger);
//        
//        a.pathfind(new Point(0,0), new Point(75,75));
        int sz = 30;
        WTFGrid msg = new WTFGrid(0,sz,0,sz);
        DStarLite dstrlt = new DStarLite(0, sz, 0, sz, msg.getObstacles(), null);
        dstrlt.pathfind(msg.getStart(), msg.getEnd());
        System.out.println(msg.visualize());
        HashMap<Point, Integer> g = dstrlt.cost;
        String[][] arrrrrrrr = new String[sz][sz];
        for (Point p : g.keySet()) {
            if ((p.x < sz && p.y < sz && p.x >= 0 && p.y >= 0)) {
                int val = g.get(p);
                if (p.equals(msg.getStart())) {
                    arrrrrrrr[p.x][p.y] = "S";
                }
                else if (p.equals(msg.getEnd())) {
                    arrrrrrrr[p.x][p.y] = "E";
                }
                else {
                    arrrrrrrr[p.x][p.y] = ""+val;
                }
            }
        }
        
        PrettyPrinter pp = new PrettyPrinter(System.out);
        pp.print(arrrrrrrr);
        
    }
    
}
