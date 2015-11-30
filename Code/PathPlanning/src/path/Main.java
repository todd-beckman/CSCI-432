/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package path;

import path.structs.Graph;
import path.structs.Point;
import path.algs.AStar;

import grid.WTFGrid;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import path.algs.DSLite;

/**
 *
 * @author alexhuleatt
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        int sz = 2000;
        Graph<Point> g = Graph.gridToGraph(new boolean[sz][sz]);
        System.out.println("Graph constructed.");
        WTFGrid wtf = new WTFGrid(0, sz, 0, sz);
        System.out.println("Grid constructed");
        runAStar(g, wtf);
        System.out.println("A* completed");
        runDSLite(g, wtf);
    }

    private static void runDSLite(Graph g, WTFGrid wtf) throws IOException {
        StreamLogger strm = new StreamLogger("dstrLite"+wtf.maxX+".csv");
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
            cur_path = d.pathfind(cur_path.get(path_index - 1));
            strm.report("new path");
            path_index = 0;
        }

        strm.close();
    }

    private static void runAStar(Graph g, WTFGrid wtf) throws IOException {
        StreamLogger strm = new StreamLogger("astr"+wtf.maxX+".csv");
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
            cur_path = a.pathfind(cur_path.get(path_index - 1), wtf.getEnd());
            path_index = 0;
        }

        strm.close();
    }

}
