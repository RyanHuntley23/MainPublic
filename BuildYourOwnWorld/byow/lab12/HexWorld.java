package byow.lab12;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions, each with
 * side length given by SIZE. Resizes world to match SIZE.
 */
public class HexWorld {
    private static final int HEXSIZE = 4;
    /** One hex is 0 rings. */
    private static final int NUMRINGS = 2;

    /*private class Coordinate {
        int x;
        int y;

        public Coordinate(int X, int Y) {
            x = X;
            y = Y;
        }
    }*/

    /*private class Hexagon {
        Coordinate bottomLeft;
        TETile type;
        int ring;
        /** Order: top, topRight, bottomRight, bottom, bottomLeft, topLeft
        Hexagon[] neighbors;

        public Hexagon(Coordinate c, TETile t) {
            bottomLeft = c;
            type = t;
            neighbors = new Hexagon[6];
        }
    }*/

    public static int getWidthHeight() {
        return HEXSIZE * ((2 * NUMRINGS) + 1) * 2;
    }

    public static int matchingY(int length) {
        int diff = length - HEXSIZE;
        return (2 * HEXSIZE) - 1 - diff;
    }

    /** Helper method for drawHexagon. */
    public static void drawLine(int length, int x, int y, TETile t, TETile[][] tiles) {
        if (length > HEXSIZE + (2 * (HEXSIZE - 1))) {
            return;
        }
        for (int i = x; i < x + length; i += 1) {
            tiles[i][y] = t;
        }
        drawLine(length + 2, x - 1, y + 1, t, tiles);
        for (int i = x; i < x + length; i += 1) {
            tiles[i][y + matchingY(length)] = t;
        }
    }

    public static void drawHexagon(int x, int y, TETile[][] tiles) {
        drawLine(HEXSIZE, x, y, Tileset.MOUNTAIN, tiles);
    }

    public static void main(String[] args) {
        int wh = getWidthHeight();
        TERenderer ter = new TERenderer();
        ter.initialize(wh, wh);

        TETile[][] world = new TETile[wh][wh];
        for (int x = 0; x < wh; x += 1) {
            for (int y = 0; y < wh; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        drawHexagon(wh / 2, wh / 2, world);
        ter.renderFrame(world);
    }
}
