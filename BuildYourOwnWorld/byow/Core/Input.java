package byow.Core;

import java.util.ArrayList;

/**
 * Created by hug. Modified by Leo Xiao and Ryan Huntley.
 * @author Josh Hug */
public interface Input {
    /** @return the next key from the Input */
    char getNextKey();

    /** @return true if there could be additional character
     *  inputs (false otherwise) */
    boolean possibleNextInput();

    /** Updates menu if input is meant to help navigate a menu (otherwise
     *  returns instantly). */
    void updateMenu();

    /** @return list of all characters returned by getNextKey */
    ArrayList<Character> getEnteredChars();

    /** Updates enteredChars by adding elements to front documenting the
     *  previous save.
     *  @param saveString string of characters */
    void updateFromSave(String saveString);
}
