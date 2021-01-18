package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/** Representation of avatar. Handles movement and drawing to canvas.
 *  @author Leo Xiao */
public class Avatar {
    /** Initial position of avatar. */
    private Position coords;
    /** World that avatar moves through. */
    private TETile[][] world;
    /** Avatar's x-coordinate. */
    private int avatarX;
    /** Avatar's y-coordinate. */
    private int avatarY;

    /** Constructor.
     *  @param p starting Position of the avatar
     *  @param w world containing the avatar */
    public Avatar(Position p, TETile[][] w) {
        coords = p;
        world = w;
        avatarX = coords.getX();
        avatarY = coords.getY();

        world[avatarX][avatarY] = Tileset.AVATAR;
    }

    /** Moves the avatar one square to the right (does nothing if
     *  this would move the avatar into a wall). */
    public void moveRight() {
        if (!(world[avatarX + 1][avatarY]).equals(Tileset.WALL)) {
            world[avatarX][avatarY] = Tileset.FLOOR;
            world[avatarX + 1][avatarY] = Tileset.AVATAR;
            avatarX += 1;
        }
    }

    /** Moves the avatar one square to the left (does nothing if
     *  this would move the avatar into a wall). */
    public void moveLeft() {
        if (!(world[avatarX - 1][avatarY]).equals(Tileset.WALL)) {
            world[avatarX][avatarY] = Tileset.FLOOR;
            world[avatarX - 1][avatarY] = Tileset.AVATAR;
            avatarX -= 1;
        }
    }

    /** Moves the avatar one square up (does nothing if
     *  this would move the avatar into a wall). */
    public void moveUp() {
        if (!(world[avatarX][avatarY + 1]).equals(Tileset.WALL)) {
            world[avatarX][avatarY] = Tileset.FLOOR;
            world[avatarX][avatarY + 1] = Tileset.AVATAR;
            avatarY += 1;
        }
    }

    /** Moves the avatar one square down (does nothing if
     *  this would move the avatar into a wall). */
    public void moveDown() {
        if (!(world[avatarX][avatarY - 1]).equals(Tileset.WALL)) {
            world[avatarX][avatarY] = Tileset.FLOOR;
            world[avatarX][avatarY - 1] = Tileset.AVATAR;
            avatarY -= 1;
        }
    }
}
