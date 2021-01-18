package byow.Core;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** Representation of a room, consisting of floors, walls, and doors
 *  (could be a hallway).
 *  @author Leo Xiao @author Ryan Hunltey */
public class Room {
    /** Bottom left position of a room (includes walls). */
    private Position bottomLeft;
    /** Width of the room (includes walls). */
    private int width;
    /** Height of the room (includes walls). */
    private int height;
    /** Pseudorandom number generator. */
    private Random rANDOM;
    /** List of the positions of all doors in a room. */
    private ArrayList<Door> doorList;

    /** Constructor. Generates parameters of the first room randomly
     *  using the center of the world as a starting point.
     *  @param R pseudorandom number generator
     *  @param world array of tiles */
    public Room(Random R, TETile[][] world) {
        bottomLeft = new Position(world.length / 2, world[0].length / 2);
        width = R.nextInt(4) + 5;
        height = R.nextInt(4) + 5;
        rANDOM = R;
        fillDoorList();
    }

    /** Constructor. Generates parameters of the room randomly using
     *  another door as a starting point.
     *  @param R pseudorandom number generator
     *  @param d the door used as a starting point */
    public Room(Random R, Door d) {
        d.setRTo(this);
        rANDOM = R;

        int hall = rANDOM.nextInt(10);
        if (hall < 4) {
            int temp = rANDOM.nextInt(2);
            if (temp == 0) {
                width = 3;
                height = rANDOM.nextInt(5) + 5;
            } else {
                width = rANDOM.nextInt(5) + 5;
                height = 3;
            }
        } else {
            width = rANDOM.nextInt(7) + 4;
            height = rANDOM.nextInt(7) + 4;
        }

        if (d.getO().equals(Orientation.UP)) {
            int x = d.getMatch().getX() - rANDOM.nextInt(width - 2) - 1;
            bottomLeft = new Position(x, d.getMatch().getY());
        } else if (d.getO().equals(Orientation.DOWN)) {
            int x = d.getMatch().getX() - rANDOM.nextInt(width - 2) - 1;
            bottomLeft = new Position(x, d.getMatch().getY() - height + 1);
        } else if (d.getO().equals(Orientation.LEFT)) {
            int y = d.getMatch().getY() - rANDOM.nextInt(height - 2) - 1;
            bottomLeft = new Position(d.getMatch().getX() - width + 1, y);
        } else {
            int y = d.getMatch().getY() - rANDOM.nextInt(height - 2) - 1;
            bottomLeft = new Position(d.getMatch().getX(), y);
        }

        doorList = new ArrayList<>();
        Door nD = new Door(d.getMatch(), this);
        nD.setRTo(d.getR());
        doorList.add(nD);
        fillDoorList();
    }

    /** @return the bottom left position of a room */
    public Position getBottomLeft() {
        return bottomLeft;
    }

    /** @return the top right position of a room */
    public Position getTopRight() {
        int x = getBottomLeft().getX();
        int y = getBottomLeft().getY();
        return new Position(x + width - 1, y + height - 1);
    }

    /** @return the width of the room */
    public int getWidth() {
        return width;
    }

    /** @return the height of the room */
    public int getHeight() {
        return height;
    }

    /** @return the list of the door positions */
    public ArrayList<Door> getDoorList() {
        return doorList;
    }

    /** Draws the room to the canvas.
     *  @param tiles 2-D array */
    public void draw(TETile[][] tiles) {
        for (int x = bottomLeft.getX() + 1; x < bottomLeft.getX()
            + width - 1; x += 1) {

            for (int y = bottomLeft.getY() + 1; y < bottomLeft.getY()
                + height - 1; y += 1) {

                tiles[x][y] = Tileset.FLOOR;
            }
        }

        for (int y = bottomLeft.getY(); y < bottomLeft.getY()
            + height; y += 1) {

            tiles[bottomLeft.getX()][y] = Tileset.WALL;
            tiles[bottomLeft.getX() + width - 1][y] = Tileset.WALL;
        }

        for (int x = bottomLeft.getX(); x < bottomLeft.getX()
            + width; x += 1) {

            tiles[x][bottomLeft.getY()] = Tileset.WALL;
            tiles[x][bottomLeft.getY() + height - 1] = Tileset.WALL;
        }

        for (Door d : doorList) {
            tiles[d.getP().getX()][d.getP().getY()] = Tileset.FLOOR;
        }
    }

    /** Closes a door by drawing a wall in its place and removing it
     *  from the doorList of the room it was in.
     *  @param d the door being removed
     *  @param tiles the world being generated */
    public void closeDoor(Door d, TETile[][] tiles) {
        doorList.remove(d);
        tiles[d.getP().getX()][d.getP().getY()] = Tileset.WALL;
    }

    /** Generates 1-3 random doors in a room (or 4 for the first room).*/
    private void fillDoorList() {
        Integer doorNumber;
        if (doorList == null) {
            doorList = new ArrayList<>();
            doorNumber = 4;
        } else {
            doorNumber = rANDOM.nextInt(3) + 1;
        }

        ArrayList<Orientation> locations = new ArrayList<>(Arrays.asList
            (Orientation.UP, Orientation.DOWN,
            Orientation.LEFT, Orientation.RIGHT));

        for (Door d : doorList) {
            locations.remove(d.getO());
        }

        for (int i = 0; i < doorNumber; i++) {
            Orientation l = locations.get(rANDOM.nextInt(locations.size()));
            doorList.add(newRandomDoor(l));
            locations.remove(l);
        }
    }

    /** Creates a random door in a room given a specified orientation.
     *  @return the created door
     *  @param o the orientation of the created door */
    private Door newRandomDoor(Orientation o) {
        Integer lowX = getBottomLeft().getX() + 1;
        Integer highX = getTopRight().getX() - 1;
        Integer lowY = getBottomLeft().getY() + 1;
        Integer highY = getTopRight().getY() - 1;
        Position p;

        if (o.equals(Orientation.UP)) {
            p = new Position(rANDOM.nextInt(highX - lowX + 1)
                + lowX, highY + 1);
        } else if (o.equals(Orientation.DOWN)) {
            p = new Position(rANDOM.nextInt(highX - lowX + 1)
                + lowX, lowY - 1);
        } else if (o.equals(Orientation.LEFT)) {
            p = new Position(lowX - 1, rANDOM.nextInt(highY
                - lowY + 1) + lowY);
        } else {
            p = new Position(highX + 1, rANDOM.nextInt(highY
                - lowY + 1) + lowY);
        }

        return new Door(p, this);
    }
}
