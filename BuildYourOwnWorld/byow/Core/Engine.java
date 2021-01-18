package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;
import java.util.HashSet;
import java.util.Arrays;

/** Parses input from main and draws a new random world if the input is valid.
 *  @author Leo Xiao @author Ryan Huntley
 *  (based on skeleton written by Josh Hug) */
public class Engine {
    /** Rendering engine. */
    private TERenderer ter = null;
    /** Width of our world. */
    public static final int WIDTH = 70;
    /** Height of our world. */
    public static final int HEIGHT = 50;
    /** Font. */
    private static final Font TITLE = new Font("Berlin Sans FB", Font.BOLD, 60);
    /** Subtitle. */
    private static final Font SUBTITLE = new Font("Berlin Sans FB",
            Font.PLAIN, 20);
    /** Possible characters for a seed. */
    public static final HashSet<Character> NUMCHARS =
        new HashSet<>(Arrays.asList('0', '1', '2', '3',
                '4', '5', '6', '7', '8', '9'));
    /** Text - X. */
    private static final Double TEXTX = 0.5;
    /** Text - Y. */
    private static final Double TEXTY = 0.75;
    /** Scaled 1. */
    private static final Double SCALE1 = 0.1;
    /** Scaled 2. */
    private static final Double SCALE2 = 0.3;
    /** Scaled 3. */
    private static final Double SCALE3 = 0.35;
    /** Scaled 4. */
    private static final Double SCALE4 = 0.4;
    /** Scaled 5. */
    private static final Double SCALE5 = 0.45;

    /**
     * Method used for exploring a fresh world. This method should handle
     * all inputs, including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInput k = new KeyboardInput();
        displayMainMenu();
        WorldGenerator wg = beforeDirections(k);
        if (ter == null) {
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
        }
        if (wg!= null) {
            k.setWorld(wg.getWorld(), ter);
            moveAvatar(k, wg);
        }
        System.exit(0);
    }

    /** Display the main menu. */
    public void displayMainMenu() {

        StdDraw.clear(Color.black);
        StdDraw.setPenColor(Color.white);
        StdDraw.setFont(TITLE);
        StdDraw.text(TEXTX, TEXTY, "THIS IS A GAME");
        StdDraw.setFont(SUBTITLE);
        StdDraw.text(TEXTX, TEXTY - SCALE1,
                "Created by Leo Xiao and Ryan Huntley");
        StdDraw.text(TEXTX, TEXTY - SCALE2, "New Game (N)");
        StdDraw.text(TEXTX, TEXTY - SCALE3, "Load Game (L)");
        StdDraw.text(TEXTX, TEXTY - SCALE4, "Replay Game (R)");
        StdDraw.text(TEXTX, TEXTY - SCALE5, "Quit (Q)");
    }

    /**
     * Method used for autograding and testing your code. The input string
     * will be a series of characters (for example, "n123sswwdasdassadwas",
     * "n123sss:q", "lwww". The engine should behave exactly as if the user
     * typed these characters into the engine using interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite
     * save. For example, if we do interactWithInputString("n123sss:q"), we
     * expect the game to run the first 7 commands (n123sss) and then quit
     * and save. If we then do interactWithInputString("l"), we should be
     * back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        Input s = new StringInput(input);
        WorldGenerator wg = interactWithInputStringHelper(s);
        return wg.getWorld();
    }

    /** Helper function for interactWithInputString (also used when loading).
     *  @return a world generator object based on input
     *  @param i input */
    private WorldGenerator interactWithInputStringHelper(Input i) {
        WorldGenerator wg = beforeDirections(i);
        moveAvatar(i, wg);
        return wg;
    }

    /**
     * Generates menu.
     * @param i input
     * @return WorldGenerator
     */
    private WorldGenerator beforeDirections(Input i) {
        char current;
        boolean validInitialInput = false;
        WorldGenerator wg = null;

        while (!validInitialInput) {
            current = i.getNextKey();
            validInitialInput = true;
            switch (current) {
                case 'N':
                    i.updateMenu();
                    TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
                    long seed = getSeed(i);
                    wg = new WorldGenerator(seed, finalWorldFrame);
                    break;
                case 'L':
                    Load l = new Load();
                    String saveString = l.getString();
                    i.updateFromSave(saveString);
                    wg = interactWithInputStringHelper(new StringInput(saveString));
                    break;
                case 'R':
                    Load l2 = new Load();
                    String saveString2 = l2.getString();
                    i.updateFromSave(saveString2);
                    ReplayInput r = new ReplayInput(saveString2);
                    WorldGenerator newWG = beforeDirections(r);

                    ter = new TERenderer();
                    ter.initialize(WIDTH, HEIGHT);
                    r.setWorld(newWG.getWorld(), ter);

                    moveAvatar(r, newWG);
                    wg = newWG;
                    break;
                case 'Q':
                    break;
                default:
                    validInitialInput = false;
            }
        }
        return wg;
    }

    /**
     * Moves Avatar.
     * @param i input
     * @param wg World Generator
     */
    private void moveAvatar(Input i, WorldGenerator wg) {
        char current;
        while (i.possibleNextInput()) {
            current = i.getNextKey();
            if (current == ':') {
                if (i.getNextKey() == 'Q') {
                    new Save(i.getEnteredChars());
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            switch (current) {
                case 'A':
                    wg.getAvatar().moveLeft();
                    break;
                case 'D':
                    wg.getAvatar().moveRight();
                    break;
                case 'W':
                    wg.getAvatar().moveUp();
                    break;
                case 'S':
                    wg.getAvatar().moveDown();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * @param i StringInput containing the seed plus some movement
     * @return the number contained within input
     */
    public long getSeed(Input i) {
        char current;
        String seedString = "";

        while (true) {
            current = i.getNextKey();
            if (current == 'S') {
                break;
            }
            if (NUMCHARS.contains(current)) {
                seedString = seedString + current;
            }
        }

        long seed = Long.valueOf(seedString).longValue();
        return seed;
    }
}
