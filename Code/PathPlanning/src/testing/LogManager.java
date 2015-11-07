package testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import pathplanning.*;
import pathplanning.util.*;

public class LogManager implements Logger {
    
    /**
     * The farthest left location in the grid
     */
    public static final int MIN_X = 0;
    
    /**
     * The farthest right location in the grid
     */
    public static final int MAX_X = 100;
    
    /**
     * The farthest up location in the grid
     */
    public static final int MIN_Y = 0;
    
    /**
     * The farthest down location in the grid
     */
    public static final int MAX_Y = 100;
    
    @Override
    public void report(String st, int... args) {
        logs.add(new Log(st, args));
    }
    
    private final ArrayList<Log> logs = new ArrayList<Log>();
    
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
    
    public static void main(String[] args) {
        HashSet<Point> obs = new HashSet<Point>();

        //  Construct the paths and their loggers
        Pather aStar = new AStar(MIN_X, MAX_X, MIN_Y, MAX_Y, obs, new LogManager());
        Pather dStarLite = new DStarLite(MIN_X, MAX_X, MIN_Y, MAX_Y, obs, new LogManager());
    }
}
