/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import path2.structs.Point;

/**
 *
 * @author alexhuleatt
 */
public class WTFGrid extends Grid {

    public WTFGrid(int minX, int maxX, int minY, int maxY) {
        super(minX, maxX, minY, maxY);
        int dx = maxX-minX;
        int dy = maxY-minY;
        boolean[][] maze = new boolean[dx][dy]; //all false.
        Point s = new Point((int) (Math.random() * (dx)),
                (int) (Math.random() * (dy)));
        ArrayList<Point> walls = new ArrayList<>();
        maze[s.x][s.y] = true;
        walls.addAll(Arrays.asList(neighbors(s)));
        Point r;
        lbl:
        while (!walls.isEmpty()) {
            r = walls.remove((int) (Math.random() * walls.size()));
            while (!(r.x >= 0 && r.x < dx && r.y >= 0 && r.y < dy) || maze[r.x][r.y]) {
                if (walls.isEmpty()) break lbl;
                r = walls.remove((int) (Math.random() * walls.size()));
            }
            ArrayList<Point> adj = new ArrayList<>(Arrays.asList(neighbors(r)));
            Collections.shuffle(adj);
            Point t = null;
            for (Point p : adj) {
                if (p.x >= 0 && p.x < dx && p.y >= 0 && p.y < dy) {
                    t = p;
                    break;
                }
            }
            if (t != null && !maze[t.x][t.y] && (r.x >= 0 && r.x < dx && r.y >= 0 && r.y < dy)) {
                maze[r.x][r.y] = true;
                int d2 = (dirBet(r, t) + 2) % 4;
                Point t2 = moveTo(r, d2);
                if ((t2.x >= 0 && t2.x < dx && t2.y >= 0 && t2.y < dy)) {
                    maze[t2.x][t2.y] = true;
                }
                walls.addAll(Arrays.asList(neighbors(r)));
            }
        }
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (!maze[i][j]) {
                    addObstacle(new Point(i+minX,j+minY));
                }
            }
        }
        
        start = randomLocation();
        HashSet<Point> obs = getObstacles();
        while (obs.contains(start) ) {
            start = randomLocation();
        }
        Point maxEnd = randomLocation();
        int count = 0;
        Point temp;
        while (count < 30) {
            temp = randomLocation();
            if (obs.contains(temp)) {
                continue;
            }
            if (temp.dist(start) > maxEnd.dist(start)) {
                maxEnd = temp;
                count = 0;
            } else {
                count++;
            }
        }
        end = maxEnd;
        

    }

    private static Point[] neighbors(Point p) {
        return new Point[]{new Point(p.x, p.y - 1),
            new Point(p.x + 1, p.y),
            new Point(p.x, p.y + 1),
            new Point(p.x - 1, p.y)};
    }

    private static Point moveTo(Point p, int d) {
        switch (d) {
            case 0:
                return new Point(p.x, p.y - 1);
            case 1:
                return new Point(p.x + 1, p.y);
            case 2:
                return new Point(p.x, p.y + 1);
            case 3:
                return new Point(p.x - 1, p.y);
        }
        return null;
    }

    private static int dirBet(Point p1, Point p2) {
        Point[] arr = neighbors(p1);
        for (int i = 0; i < 4; i++) {
            if (arr[i].equals(p2)) {
                return i;
            }
        }
        return -1;
    }

}
