package byow.Core;

import java.util.ArrayList;

/** Input is received from an input string one character at a time.
 *  @author Ryan Huntley @author Leo Xiao (some code borrowed from Josh Hug) */
public class StringInput implements Input {
    /** Contains all characters to be returned. */
    private String input;
    /** Index of next character to be returned. */
    private int index;
    /** List of all characters entered. */
    private ArrayList<Character> enteredChars;

    /** Constructor.
     *  @param s string to get characters from*/
    public StringInput(String s) {
        index = 0;
        input = s;
        enteredChars = new ArrayList<>();
    }

    /** @returns next character in input */
    @Override
    public char getNextKey() {
        char returnChar = Character.toUpperCase(input.charAt(index));
        enteredChars.add(returnChar);
        index += 1;
        return returnChar;
    }

    /** @returns false if all characters from input have been returned
     *  (true otherwise) */
    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }

    /** Does nothing. */
    @Override
    public void updateMenu() {
        return;
    }

    /** @return list of all characters returned by getNextKey */
    @Override
    public ArrayList<Character> getEnteredChars() {
        return enteredChars;
    }

    /** Updates enteredChars by adding elements to front documenting the
     *  previous save.
     *  @param saveString list of characters */
    @Override
    public void updateFromSave(String saveString) {
        enteredChars.remove(enteredChars.size() - 1);
        ArrayList<Character> newEnteredChars = new ArrayList<>();
        for (int i = 0; i < saveString.length(); i += 1) {
            newEnteredChars.add(saveString.charAt(i));
        }
        for (char c : enteredChars) {
            newEnteredChars.add(c);
        }
        enteredChars = newEnteredChars;
    }
}
