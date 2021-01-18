import java.util.ArrayList;

/** Representation of a city. Contains information about
 *  the routes that stem from that city.
 *  @author Ryan Huntley */
public class City {
    /** Name of city. */
    private String name;
    /** List of all routes connected to the city. */
    private ArrayList<Route> routeList;
    /** List of all cities that share a route with this city. */
    private ArrayList<City> neighborList;
    /** X-coordinate of city on a Ticket. */
    private double x;
    /** Y-coordinate of city on a Ticket. */
    private double y;
    /** Type of city. */
    private CityType type;

    /** Constructor.
     *  @param n name
     *  @param X X-coordinate
     *  @param Y Y-coordinate
     *  @param t type of city */
    public City(String n, double X, double Y, CityType t) {
        name = n;
        routeList = new ArrayList<>();
        neighborList = new ArrayList<>();
        x = X;
        y = Y;
        type = t;
    }

    /** @return name */
    public String name() {
        return name;
    }

    /** @return routeList */
    public ArrayList<Route> routeList() {
        return routeList;
    }

    /** @return neighborList */
    public ArrayList<City> neighborList() {
        return neighborList;
    }

    /** @return X-coordinate */
    public double X() {
        return x;
    }

    /** @return Y-coordinate */
    public double Y() {
        return y;
    }

    /** @return type */
    public CityType type() {
        return type;
    }

    /** Adds a route to routeList.
     *  @param r the route being added */
    public void addRoute(Route r) {
        routeList.add(r);
        for (City c : r.cities()) {
            if (!c.equals(this)) {
                if (!neighborList.contains(c)) {
                    neighborList.add(c);
                }
            }
        }

    }

    /** @return the number of routes connected to the city */
    public int numRoutes() {
        return routeList().size();
    }

    /** @return the number of neighbors that this city has */
    public int numNeighbors() {
        return neighborList.size();
    }

    /** @return the weight of this city. */
    public int weight() {
        int numDouble = numRoutes() - numNeighbors();
        int numSingle = numRoutes() - (2 * numDouble);
        int weight = (3 * numDouble) + (2 * numSingle);
        if (type.equals(CityType.BIG)) {
            weight *= 2;
        }
        return weight;
    }

    /** @return true if this equals o, false otherwise
     *  @param o city being compared to this */
    @Override
    public boolean equals(Object o) {
        City c = (City) o;
        return name.equals(c.name());
    }

    /** @return a simple hashCode for a city (note, this is in place
     *  so that identical cities will be marked as equal for the purpose
     *  of the HashSet.contains method.*/
    @Override
    public int hashCode() {
        return name.length();
    }
}
