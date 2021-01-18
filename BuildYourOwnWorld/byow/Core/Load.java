package byow.Core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** Object that loads a string from a file when created.
 *  @author Leo Xiao */
public class Load {
    /** Returned String. */
    private String returned;

    /** Constructor. */
    public Load() {
        read();
    }

    /** Reads string and sets returned. */
    private void read() {
        try {
            File file = new File("byow\\Core\\World.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                returned = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    /** @return returned (read string) */
    public String getString() {
        return returned;
    }
}
