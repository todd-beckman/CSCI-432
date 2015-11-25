package grid;

import java.io.*;

/**
 * Created by lwelna on 11/24/15.
 * STOLEN FROM http://www.tutorialspoint.com/java/java_serialization.htm
 */
public class Serializer {

    public boolean serialize(Grid g) {
        try {
            // change to Code/PathPlanning/tmp/
            System.out.println("Trying to write to Code/PathPlanning/tmp/");
            FileOutputStream fileOut = new FileOutputStream("Code/PathPlanning/tmp/");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(g);
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in Code/PathPlanning/tmp/");
            return true;
        } catch (IOException i) {
            i.printStackTrace();
            return false;
        }
    }

    public Grid deserializer() {
        try {
            FileInputStream fileIn = new FileInputStream("Code/PathPlanning/tmp/");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Grid g = (Grid)in.readObject();
            in.close();
            fileIn.close();
            return g;
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Grid class not found");
            c.printStackTrace();
            return null;
        }
    }
}
