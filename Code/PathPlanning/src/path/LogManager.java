package path;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import grid.Grid;
import grid.WTFGrid;
import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * An implementation of Logger that tracks an ordered list of reports and allows
 * the logs to be printed to file.
 *
 * @author Todd
 *
 */
public class LogManager implements Logger {

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
        } catch (IOException e) {
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
        } catch (IOException e) {
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
            Log.bulkWrite(logs, bw);
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
