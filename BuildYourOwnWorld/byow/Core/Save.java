package byow.Core;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

/** Object that saves a string to a file when created.
 *  @author Leo Xiao */
public class Save {

    /** List of WASD inputs. */
    private ArrayList<Character> inputs;
    /** String-builder. */
    private StringBuilder savedText = new StringBuilder();
    /** Converted string builder text. */
    private String savedTextString;
    /** Saved World. */
    private static final File SAVEDWORLD = new File("byow\\Core\\World.txt");

    /** Constructor.
     *  @param i list of characters to be written to a file */
    public Save(ArrayList<Character> i) {
        inputs = i;
        saveFile();
    }

    /** Saves characters. */
    public void saveFile() {
        inputs.remove(inputs.size() - 1);
        inputs.remove(inputs.size() - 1);

        for (Character c : inputs) {
            savedText.append(c);
        }

        savedTextString = savedText.toString();


        if (!SAVEDWORLD.exists()) {
            try {
                SAVEDWORLD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter myWriter = new FileWriter(SAVEDWORLD);
            myWriter.write(savedTextString);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
