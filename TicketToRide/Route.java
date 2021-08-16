/** Representation of a route connecting two cities (NOT a
 *  ticket). Contains all information pertaining to that route.
 *  @author Ryan Huntley */
public class Route {
    /** First endpoint of route. */
    private City city1;
    /** Second endpoint of route. */
    private City city2;
    /** Number of trains required to claim the route. */
    private int length;
    /** Type of route (NORMAL, TUNNEL, or FERRY). */
    private RouteType type;
    /** Color of route (RED, ORANGE, YELLOW, GREEN, BLUE,
     *  PINK, WHITE, BLACK, GRAY). */
    private Color color;
    /** Number of wilds required for FERRY type
     *  (0 if type is NORMAL or TUNNEL). */
    private int numWilds;

    /** Constructor.
     *  @param c1 city1
     *  @param c2 city2
     *  @param c color
     *  @param l length
     *  @param rT type
     *  @param nW numWilds */
    public Route(City c1, City c2, Color c, int l, RouteType rT,  int nW) {
        checkValid(c1, c2, l, rT, c, nW);

        city1 = c1;
        city2 = c2;
        length = l;
        type = rT;
        color = c;
        numWilds = nW;

        c1.addRoute(this);
        c2.addRoute(this);
    }

    /** @return two element array of the cities this route connects */
    public City[] cities() {
        City[] cities = {city1, city2};
        return cities;
    }

    /** @return length */
    public int length() {
        return length;
    }

    /** @return type */
    public RouteType type() {
        return type;
    }

    /** @return color */
    public Color color() {
        return color;
    }

    /** @return numWilds */
    public int numWilds() {
        return numWilds();
    }

    /** Checks if arguments to route constructor are valid.
     *  @param c1 city1
     *  @param c2 city2
     *  @param l length
     *  @param rT type
     *  @param c color
     *  @param nW numWilds */
    private void checkValid(City c1, City c2, int l, RouteType rT, Color c, int nW) {
        if (c1.equals(c2) || l <= 0) {
            throw new IllegalArgumentException();
        }
        if (rT == RouteType.FERRY && c != Color.GRAY) {
            throw new IllegalArgumentException();
        }
        if (rT != RouteType.FERRY && nW != 0) {
            throw new IllegalArgumentException();
        } else if (rT == RouteType.FERRY && (nW < 1 || nW > l)) {
            throw new IllegalArgumentException();
        }
    }

    /** @return one city in a route given the other city (throws an exception if the
     *  provided city is not in the route)
     *  @param c provided city */
    public City getOtherCity(City c) {
        if (c.equals(city1)) {
            return city2;
        } else if (c.equals(city2)) {
            return city1;
        }
        throw new IllegalArgumentException();
    }
}
