package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.util.ArrayList;


/** Input is received from the keyboard one character at a time.
 *  @author Ryan Huntley @author Leo Xiao (some code borrowed from
 */
public class KeyboardInput implements Input {

    /** The world. */
    private TETile[][] world = null;
    /** HUD user interface. */
    private HUD hud = null;
    /** TeRenderer. */
    private TERenderer ter;
    /** List of all characters entered. */
    private ArrayList<Character> enteredChars;
    /** Text X coord. */
    private static final Double TEXTX = 0.5;
    /** Text Y coord. */
    private static final Double TEXTY = 0.15;

    /** Constructor. */
    public KeyboardInput() {
        enteredChars = new ArrayList<>();
    }

    /** Sets the world (after menu option has been selected).
     *  @param w tiles of world to be drawn
     *  @param t TERenderer */
    public void setWorld(TETile[][] w, TERenderer t) {
        world = w;
        hud = new HUD(world);
        ter = t;
    }

    /** @return the next key typed by the user */
    @Override
    public char getNextKey() {
        while (true) {
            if (!(world == null)) {
                int mouseX = (int) Math.floor(StdDraw.mouseX());
                int mouseY = (int) Math.floor(StdDraw.mouseY());

                if (mouseY > world[0].length - 1) {
                    mouseY = world[0].length - 1;
                }

                if (mouseX > world.length - 1) {
                    mouseX = world.length - 1;
                }

                hud.updateHUD(world[mouseX][mouseY]);
            }

            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                enteredChars.add(c);
                return c;
            }

            if (!(world == null)) {
                ter.renderFrame(world);
            }
        }
    }

    /** @return true (the user could always type more keys) */
    @Override
    public boolean possibleNextInput() {
        return true;
    }

    /** Prompts the user to enter a seed. */
    @Override
    public void updateMenu() {
        Font f = new Font("Berlin Sans FB", Font.PLAIN, 15);
        StdDraw.setFont(f);
        String message = "Enter a number less than "
                + "9,223,372,036,854,775,807. Then press S.";
        StdDraw.text(TEXTX, TEXTY, message);
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
