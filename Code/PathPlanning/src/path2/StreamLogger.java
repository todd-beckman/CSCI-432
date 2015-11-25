/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package path2;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;

/**
 *
 * @author alexhuleatt
 */
public class StreamLogger implements Logger {
    private final String fname;
    
    private BufferedWriter bw;
    public StreamLogger(String fname) throws FileNotFoundException {
        this.fname=fname;
        bw = new BufferedWriter(new PrintWriter(fname));
    }

    @Override
    public void report(String st, int... args) {
        try {
            bw.write(st + " " + Arrays.toString(args) + "\n");
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(StreamLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close() throws IOException {
        bw.close();
    }
    
}
