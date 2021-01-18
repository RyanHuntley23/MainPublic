package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Creates the HUD.
 * @author ryanhuntley, leoxiao
 */
public class HUD {
    /** Starting x-point of the HUD. */
    private int hUDX;
    /** Starting y-point of the HUD. */
    private int hUDY;
    /** World. */
    private TETile[][] world;
    /** Local date. */
    private LocalDate date;
    /** Local time. */
    private LocalTime time;
    /** Formatter. */
    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    /** Formatted string. */
    private String formatted;

    /**
     * Constructor.
     * @param w world
     */
    public HUD(TETile[][] w) {
        hUDX = 0;
        world = w;
        hUDY = world[0].length - 1;
    }

    /**
     * Updates the HUD with time.
     * @param current time, date
     */
    public void updateHUD(TETile current) {
        date = LocalDate.now();
        time = LocalTime.now();
        formatted = time.format(formatter);

        StdDraw.setPenColor(StdDraw.WHITE);
        if (current.equals(Tileset.WALL)) {
            StdDraw.textLeft(hUDX, hUDY, "wall");
        } else if (current.equals(Tileset.FLOOR)) {
            StdDraw.textLeft(hUDX, hUDY, "floor");
        } else if (current.equals(Tileset.AVATAR)) {
            StdDraw.textLeft(hUDX, hUDY, "avatar");
        } else {
            StdDraw.textLeft(hUDX, hUDY, "da void");
        }
        StdDraw.textLeft(hUDX, hUDY - 1, date.toString());
        StdDraw.textLeft(hUDX, hUDY - 2, formatted);
        StdDraw.show();
    }
}
