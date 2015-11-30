/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package display;

import grid.WTFGrid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import path.Logger;
import path.StreamLogger;
import path.algs.AStar;
import path.algs.DSLite;
import path.structs.Graph;
import path.structs.Point;

/**
 *
 * @author alexhuleatt
 */
public class DisplayMain {

    public static void main(String[] args) throws IOException, InterruptedException {

        int sz = 200;
        WTFGrid wtf = new WTFGrid(0, sz, 0, sz);
        Frame f = new Frame();
        f.setObs(wtf.getObstacles());
        f.setStart(wtf.getStart().x, wtf.getStart().y);
        f.setFinish(wtf.getEnd().x, wtf.getEnd().y);
        f.run();

        Graph<Point> g = Graph.gridToGraph(new boolean[sz][sz]);

        System.out.println("Graph constructed.");

        System.out.println("Grid constructed");
        runDSLite(g, wtf, f);
        Thread.sleep(5000);
        f.clear();
        runAStar(g, wtf, f);
    }

    static class DispLogger implements Logger {

        public Frame frame;

        public DispLogger(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void report(String st, int... args) {
            if (args.length > 0) {
                int x = args[0];
                int y = args[1];
                try {
                    frame.activate(x, y);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(DisplayMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private static void runDSLite(Graph g, WTFGrid wtf, Frame f) throws IOException {
        DispLogger strm = new DispLogger(f);
        DSLite d = new DSLite(g, strm);
        d.initialize(wtf.getStart(), wtf.getEnd());
        ArrayList<Point> cur_path = d.pathfind(wtf.getStart());
        HashSet<Point> unknown = wtf.getObstacles();
        int path_index = 0;
        while (true) {
            while (path_index < cur_path.size()
                    && !unknown.contains(cur_path.get(path_index))) {
                path_index++;
            }

            if (path_index >= cur_path.size()) {
                break;
            }

            d.addObstacle(cur_path.get(path_index));
            cur_path = d.pathfind(wtf.getStart());
            f.givePath(cur_path);
            strm.report("new path");
            path_index = 0;
        }
        f.givePath(d.pathfind(wtf.getStart()));
        f.repaint();
    }

    private static void runAStar(Graph g, WTFGrid wtf, Frame f) throws IOException {
        DispLogger strm = new DispLogger(f);
        AStar a = new AStar(g, strm);
        ArrayList<Point> cur_path = a.pathfind(wtf.getStart(), wtf.getEnd());
        HashSet<Point> unknown = wtf.getObstacles();
        int path_index = 0;
        while (true) {
            while (path_index < cur_path.size()
                    && !unknown.contains(cur_path.get(path_index))) {
                path_index++;
            }

            if (path_index >= cur_path.size()) {
                break;
            }

            a.addObstacle(cur_path.get(path_index));
            cur_path = a.pathfind(wtf.getStart(), wtf.getEnd());
            f.givePath(cur_path);
            strm.report("new path");
            path_index = 0;
        }
        f.givePath(a.pathfind(wtf.getStart(), wtf.getEnd()));
        f.repaint();
    }

}
