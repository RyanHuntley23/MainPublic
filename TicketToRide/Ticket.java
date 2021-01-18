import java.util.HashMap;

/** Destination ticket connecting two cities.
 *  @author Ryan Huntley */
public class Ticket {
    /** First city (western-most). */
    private City c1;
    /** Second city (eastern-most). */
    private City c2;
    /** Length (point value) of ticket. */
    private int length;
    /** Board that this ticket belongs to. */
    private Board board;

    /** Constructor.
     * @param b board containing ticket
     * @param cityA one city
     * @param cityB another city*/
    public Ticket(Board b, City cityA, City cityB) {
        board = b;
        c1 = cityA;
        c2 = cityB;
        if (cityA.X() < cityB.X()) {
            c1 = cityA;
            c2 = cityB;
        } else {
            c1 = cityB;
            c2 = cityA;
        }
        length = findTicketLength();
    }

    /** @return first city on ticket */
    public City c1() {
        return c1;
    }

    /** @return first city on ticket */
    public City c2() {
        return c2;
    }

    /** @return length of ticket */
    public int length() {
        return length;
    }

    /** @return the length of the shortest path connecting
     *  the two cities on the ticket */
    private int findTicketLength() {
        City current;
        HashMap<City, Integer> distTo = new HashMap<>();
        ArrayHeapMinPQ<City> pq = new ArrayHeapMinPQ<>();
        for (City c : board.cities().values()) {
            if (c.equals(c1)) {
                pq.add(c, 0);
                distTo.put(c, 0);
            } else {
                pq.add(c, Integer.MAX_VALUE);
                distTo.put(c, Integer.MAX_VALUE);
            }
        }
        while (!pq.getSmallest().equals(c2)) {
            current = pq.removeSmallest();
            relax(current, distTo, pq);
        }
        return distTo.get(c2);
    }

    /** For each edge stemming from a city, that edge is relaxed and the best
     *  known path to the new city is updated.
     *  @param c all edges from c are relaxed
     *  @param distTo map from city to length of best known path to that city */
    private void relax(City c, HashMap<City, Integer> distTo, ArrayHeapMinPQ<City> pq) {
        City other;
        int newLength;
        for (Route r: c.routeList()) {
            other = r.getOtherCity(c);
            newLength = distTo.get(c) + r.length();
            if (newLength < distTo.get(other)) {
                distTo.put(other, newLength);
                pq.changePriority(other, newLength);
            }
        }
    }

    /** @return true if this equals o, false otherwise
     *  @param o city being compared to this */
    @Override
    public boolean equals(Object o) {
        Ticket t = (Ticket) o;
        if (c1.equals(t.c1) && c2.equals(t.c2)) {
            return true;
        }
        if (c1.equals(t.c2) && c2.equals(t.c1)) {
            return true;
        }
        return false;
    }

    /** @return a simple hashCode for a ticket (note, this is in place
     *  so that identical tickets will be marked as equal for the purpose
     *  of the HashSet.contains method.*/
    @Override
    public int hashCode() {
        return length;
    }
}