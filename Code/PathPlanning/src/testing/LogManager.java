package testing;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import grid.Grid;
import grid.WTFGrid;
import java.io.BufferedWriter;
import java.util.Arrays;
import pathplanning.*;

/**
 * An implementation of Logger that tracks an ordered list of reports and allows
 * the logs to be printed to file.
 *
 * @author Todd
 *
 */
public class LogManager implements Logger {

    public static void main(String[] args) {

        //  Settings
        int minX = 0;
        int maxX = 100;
        int minY = 0;
        int maxY = 100;

        Grid g = new WTFGrid(minX, maxX, minY, maxY);

        System.out.println(g.visualize());

        try {
            FileWriter f = new FileWriter("logs.txt");
            f.write(g.visualize());
            f.close();
        } catch (Exception e) {
        }
        LogManager aStarLogger = new LogManager();
        LogManager dStarLogger = new LogManager();

        //  Construct the paths and their loggers
        AStar aStar = new AStar(minX, maxX, minY, maxY, g.getObstacles(), aStarLogger);
        DStarLite dStarLite = new DStarLite(minX, maxX, minY, maxY, g.getObstacles(), dStarLogger);
        
        System.out.println("A*: " + Arrays.toString(aStar.pathfind(g.getStart(), g.getEnd())));
        dStarLite.init_pathfind(g.getStart(), g.getEnd());

        aStarLogger.writeToFile("astar.csv");
        
        //  Uncomment the report method below
        //  because this adds to the runtime
        aStarLogger.writeSizesToFile("astarsizes.csv");
        aStarLogger.writeStatsToFile("astarstats.csv");

        //  D* Lite Pathfind
//        dStarLite.pathfind(g.getStart(), g.getEnd());
//        dStarLogger.writeToFile("dstarlite.csv");
    }

    private final ArrayList<Log> logs = new ArrayList<Log>();
    private int stackSize = 0, logSize = 0;
    private ArrayList<Integer> stackSizes = new ArrayList<Integer>();
    private ArrayList<Integer> logSizes = new ArrayList<Integer>();
    

    private int numberOfPushes = 0;
    private int numberOfPops = 0;
    private int numberOfRemoves = 0;
    
    {   //  shoutout to Jacob
        stackSizes.add(0);
        logSizes.add(0);
    }

    /**
     * Saves a log entry
     */
    @Override
    public void report(String st, int... args) {
        logs.add(new Log(st, args));
        //  adds to runtime; uncomment to use
        logSizes.add(++logSize);
        switch (st) {
        case "push":
            stackSizes.add(++stackSize);
            numberOfPushes++;
            break;
        case "pop":
            stackSizes.add(--stackSize);
            numberOfPops++;
            break;
        case "remove":
            numberOfRemoves++;
        default:
            stackSizes.add(stackSize);
            break;
        }
    }
    
    public void writeStatsToFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("pushes," + numberOfPushes + "\n");
            bw.write("pops," + numberOfPops + "\n");
            bw.write("removes," + numberOfRemoves);
            bw.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to " + filename + ". Printing to console instead.");
            System.out.println("pushes," + numberOfPushes + "\n");
            System.out.println("pops," + numberOfPops + "\n");
            System.out.println("removes," + numberOfRemoves);
        }
    }
    
    public void writeSizesToFile(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("dependent,independent\n");
            for (int i = 0; i < stackSizes.size(); i++) {
                bw.write(stackSizes.get(i) + "," + logSizes.get(i) + "\n");
            }
            bw.close();
        }
        catch (IOException e) {
            System.out.println("Could not write to " + filename + ". Printing to console instead.");
            System.out.println(stackSizes.toString());
            System.out.println("\n");
            System.out.println(logSizes.toString());
        }
    }

    /**
     * Writes the current logs to file
     *
     * @param filename The file to write to
     */
    public void writeToFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(Log.bulkToString(logs));
            bw.close();
        } catch (IOException e) {
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
