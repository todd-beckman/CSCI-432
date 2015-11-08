package testing;

import java.util.ArrayList;

/**
 * 
 * The Log class represents individual entries in log files.
 * @author Todd
 */
public class Log {
    /**
     * The name of the action which is being logged
     */
    public final String name;
    
    /**
     * The relevant arguments
     */
    public final int[] args;
    
    /**
     * 
     * @param name
     * @param args
     */
    public Log(String name, int ... args) {
        this.name = name;
        this.args = args;
    }
    
    /**
     * Converts a list of logs into a string, with each log separated by a new line character
     * @param logs The list of logs
     * @return The string
     */
    public static String bulkToString(ArrayList<Log> logs) {
        String str = "";
        for (Log l: logs) {
            str += l.toString() + "\n";
        }
        return str;
    }
    
    /**
     * Converts a log into the format: name,arg0,arg1,etc
     */
    @Override
    public String toString() {
        String str = name;
        for (int i = 0; i < args.length; i++) {
            str += "," + args[i];
        }
        return str;
    }
}
