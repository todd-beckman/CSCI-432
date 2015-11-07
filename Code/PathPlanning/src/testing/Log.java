package testing;

/**
 * The Log class represents individual entries in log files.
 * 
 */
public class Log {
    public final String name;
    public final int[] args;
    public Log(String name, int ... args) {
        this.name = name;
        this.args = args;
    }
    
    @Override
    public String toString() {
        String str = name;
        for (int i = 0; i < args.length; i++) {
            str += "," + args[i];
        }
        return str;
    }
}
