package byow.Core;

/** 2d position in our world.
 *  @author Leo Xiao (adapated from CS61B course content) */
public class Position {
    /** X-coordinate. */
    private int x;
    /** Y-coordinate. */
    private int y;

    /** Constructor (from two coordinates).
     *  @param X x-coordinate
     *  @param Y y-coordinate */
    public Position(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    /** Constructor (from a Position and two offsets).
     *  @param ref reference position
     *  @param xOffset x offset
     *  @param yOffset y offset */
    public Position(Position ref, int xOffset, int yOffset) {
        this.x = ref.getX() + xOffset;
        this.y = ref.getY() + yOffset;
    }

    /** @return x-coordinate of the position */
    public int getX() {
        return x;
    }

    /** @return y-coordinate of the position */
    public int getY() {
        return y;
    }
}
