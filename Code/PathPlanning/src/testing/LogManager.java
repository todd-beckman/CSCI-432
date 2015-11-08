package testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import grid.Grid;
import grid.RandomGrid;
import java.util.Arrays;
import pathplanning.*;

/**
 * An implementation of Logger that tracks an ordered list of reports and allows the logs to
 * be printed to file.
 * @author Todd
 *
 */
public class LogManager implements Logger {
    public static void main(String[] args) {
        
        //  Settings
        int minX = 0;
        int maxX = 10;
        int minY = 0;
        int maxY = 10;
        int obstacles = 20;
        
        Grid g = new RandomGrid(minX, maxX, minY, maxY, obstacles);
        
        System.out.println(g.visualize());

        LogManager aStarLogger = new LogManager();
        LogManager dStarLogger = new LogManager();
        
        //  Construct the paths and their loggers
        Pather aStar = new AStar(minX, maxX, minY, maxY, g.getObstacles(), aStarLogger);
        Pather dStarLite = new DStarLite(minX, maxX, minY, maxY, g.getObstacles(), dStarLogger);
        
        //  A* Pathfind
        System.out.println(Arrays.toString(aStar.pathfind(g.getStart(), g.getEnd())));
        aStarLogger.writeToFile("astar.csv");
        
        //  D* Lite Pathfind
        dStarLite.pathfind(g.getStart(), g.getEnd());
        dStarLogger.writeToFile("dstarlite.csv");
    }

    
    
    private final ArrayList<Log> logs = new ArrayList<Log>();
    
    /**
     * Saves a log entry
     */
    @Override
    public void report(String st, int... args) {
        logs.add(new Log(st, args));
    }
    
    
    /**
     * Writes the current logs to file
     * @param filename The file to write to
     */
    public void writeToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            writer.write(Log.bulkToString(logs));
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to " + filename + ". Printing to console instead.");
            System.out.println(Log.bulkToString(logs));
        }
    }
    
    /**
     * A log manager that also prints to the console for each log entry.
     */
    class PrintLogManager extends LogManager {
        @Override
        public void report(String st, int... args) {
            super.report(st, args);
            System.out.println(logs.get(logs.size() - 1).toString());
        }
    }
}
