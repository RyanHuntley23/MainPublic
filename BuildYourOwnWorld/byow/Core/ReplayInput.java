package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import java.util.ArrayList;

/** Input is received from a string and drawn to the screen one character
 *  at a time (with real time delays).
 *  @author Ryan Huntley @author Leo Xiao (some code borrowed from Josh Hug) */
public class ReplayInput implements Input {
    /** The world. */
    private TETile[][] world = null;
    /** TERenderer. */
    private TERenderer ter;
    /** List of all characters entered. */
    private ArrayList<Character> enteredChars;
    /** Contains all characters to be returned. */
    private String input;
    /** Index of next character to be returned. */
    private int index;

    /** Constructor.
     *  @param i returned by getNextKey one char at a time */
    public ReplayInput(String i) {
        enteredChars = new ArrayList<>();
        input = i;
        index = 0;
    }

    /** Sets the world (after menu option has been selected).
     *  @param w tiles of world to be drawn
     *  @param t TERenderer */
    public void setWorld(TETile[][] w, TERenderer t) {
        world = w;
        ter = t;
    }

    /** @return the next key typed by the user */
    @Override
    public char getNextKey() {
        if (!(world == null)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                assert true;
            }
            ter.renderFrame(world);
        }
        char returnChar = Character.toUpperCase(input.charAt(index));
        enteredChars.add(returnChar);
        index += 1;
        return returnChar;
    }

    /** @return true (the user could always type more keys) */
    @Override
    public boolean possibleNextInput() {
        return index < input.length();
    }

    /** Prompts the user to enter a seed. */
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
