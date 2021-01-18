package byow.Core;

/** Representation of a door (note that the two tile space between
 *  two rooms consists of two doors).
 *  @author Leo Xiao @author Ryan Huntley */
public class Door {
    /** Position of door. */
    private Position p;
    /** Room that this door belongs to. */
    private Room r;
    /** Room that this door leads to. */
    private Room rTo;
    /** Position adjacent to this door (Position of future door). */
    private Position match;
    /** Orientation of door relative to "from". */
    private Orientation o;

    /** Constructor.
     *  @param pos position of door
     *  @param room room that this door is from */
    public Door(Position pos, Room room) {
        p = pos;
        r = room;

        if (pos.getX() == room.getBottomLeft().getX()) {
            match = new Position(pos.getX() - 1, pos.getY());
            o = Orientation.LEFT;
        } else if (pos.getX() == room.getTopRight().getX()) {
            match = new Position(pos.getX() + 1, pos.getY());
            o = Orientation.RIGHT;
        } else if (pos.getY() == room.getBottomLeft().getY()) {
            match = new Position(pos.getX(), pos.getY() - 1);
            o = Orientation.DOWN;
        } else if (pos.getY() == room.getTopRight().getY()) {
            match = new Position(pos.getX(), pos.getY() + 1);
            o = Orientation.UP;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /** @return the position of this door */
    public Position getP() {
        return p;
    }

    /** @return the room where this door is located */
    public Room getR() {
        return r;
    }

    /** @return the room where this door leads*/
    public Room getRTo() {
        return rTo;
    }

    /** @return the position adjacent to this door */
    public Position getMatch() {
        return match;
    }

    /** @return the orientation of this door */
    public Orientation getO() {
        return o;
    }

    /** Sets the room where this door leads.
     *  @param newRTo the new room*/
    public void setRTo(Room newRTo) {
        rTo = newRTo;
    }
}
