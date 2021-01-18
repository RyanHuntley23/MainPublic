package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;
import java.util.ArrayList;

/** Object used to generate a new world with random rooms from scratch.
 *  @author Leo Xiao @author Ryan Huntley*/
public class WorldGenerator {
    /** Height of world. */
    private int hEIGHT;
    /** Width of world. */
    private int wIDTH;
    /** Random Generator. */
    private Random rANDOM;
    /** The world on which rooms will be drawn. */
    private TETile[][] world;
    /** Creates a new priority queue. */
    private ArrayHeapMinPQ<Room> priorityQueue = new ArrayHeapMinPQ<>();
    /** Priority Counter. */
    private double counter = 1.0;
    /** Max of total rooms and hallways. */
    private static final int MAXIMUM = 150;
    /** Initial position of avatar. */
    private Position start;
    /** Avatar. */
    private Avatar a;

    /** Constructor. Generates a world from a seed and a blank TETile array.
     *  @param seed used to create pseudorandom number generator
     *  @param w blank 2D tile array (all items are null) */
    public WorldGenerator(long seed, TETile[][] w) {
        world = w;
        wIDTH = w.length;
        hEIGHT = w[0].length;
        start = new Position((world.length / 2) + 1, (world[0].length / 2) + 1);
        rANDOM = new Random(seed);
        for (int x = 0; x < wIDTH; x += 1) {
            for (int y = 0; y < hEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Room startingRoom = new Room(rANDOM, world);
        startingRoom.draw(world);
        priorityQueue.add(startingRoom, counter);
        recursiveRoom(startingRoom);
        closeFinalDoors();
        a = new Avatar(start, world);
    }

    /** @return the world generated */
    public TETile[][] getWorld() {
        return world;
    }

    /** @return start */
    public Position getStart() {
        return start;
    }

    /** @return avatar */
    public Avatar getAvatar() {
        return a;
    }

    /** Generates and draws all rooms other than the starting room.
     *  @param s the starting room */
    public void recursiveRoom(Room s) {
        if (counter > MAXIMUM) {
            return;
        }
        ArrayList<Door> badDoors = new ArrayList<>();
        for (Door d : s.getDoorList()) {
            if (d.getRTo() != null) {
                continue;
            }
            Room newRoom = null;
            for (int i = 0; i < 10; i++) {
                newRoom = new Room(rANDOM, d);
                if (checkValid(world, newRoom)) {
                    break;
                }
                newRoom = null;
            }
            if (newRoom == null) {
                badDoors.add(d);
                continue;
            }
            counter += 1;
            priorityQueue.add(newRoom, counter);
            newRoom.draw(world);
        }
        for (Door d : badDoors) {
            s.closeDoor(d, world);
        }
        priorityQueue.removeSmallest();
        if (priorityQueue.size() == 0) {
            return;
        }
        recursiveRoom(priorityQueue.getSmallest());
    }

    /** Closes all doors once maximum has been reached. */
    private void closeFinalDoors() {
        while (priorityQueue.size() > 0) {
            Room r = priorityQueue.removeSmallest();
            ArrayList<Door> badDoors = new ArrayList<>();
            for (Door d : r.getDoorList()) {
                if (d.getRTo() == null) {
                    badDoors.add(d);
                }
            }
            for (Door d : badDoors) {
                r.closeDoor(d, world);
            }
        }
    }

    /** @return true if a room can fit in a certain location
     *  @param tiles the world we are drawing on
     *  @param room the room in question*/
    private boolean checkValid(TETile[][] tiles, Room room) {
        boolean check1 = room.getBottomLeft().getX() >= 0
            && room.getBottomLeft().getY() >= 0;
        boolean check2 = room.getTopRight().getX() < wIDTH
            && room.getTopRight().getY() < hEIGHT;
        boolean check3 = room.getBottomLeft().getX() < 7
            && room.getTopRight().getY() >= hEIGHT - 5;
        if (!check1 || !check2 || check3) {
            return false;
        }

        for (int x = room.getBottomLeft().getX();
             x < room.getBottomLeft().getX() + room.getWidth(); x += 1) {

            for (int y = room.getBottomLeft().getY();
                 y < room.getBottomLeft().getY() + room.getHeight(); y += 1) {

                if (tiles[x][y] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }
}
