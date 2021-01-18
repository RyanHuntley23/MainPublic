import java.util.Random;

/** Main entry point for TTR. Reads from a file, creates a board, creates the
 *  specified number of tickets, (optionally) saves the board, (optionally) prints
 *  the number of tickets into each city, and (optionally) draws all tickets. All
 *  final variables are meant to be modified before running.
 *  @author Ryan Huntley */
public class Main {
    /** Path of text file containing all cities and routes. */
    private static final String MAPFILENAME = "NorthernHemisphereBoard.txt";
    /** Path of file with all tickets to be created (null if tickets
     *  should be generated randomly). */
    private static final String TICKETLISTFILENAME = null;
    /** Number of tickets to be created (if tickets should
     *  be randomly generated). */
    private static final int NUMTICKETS = 200;
    /** Path of jpg file used as default ticket. */
    private static final String TICKETFILENAME = "Pictures\\Route_Template.jpg";
    /** Draws tickets if set to true. */
    private static final boolean DRAWTICKETS = false;
    /** Seed for pseudorandom route generation. */
    private static final Random RAND = new Random(9432759623578L);
    /** Maximum number of board that will be created. */
    private static final int MAXBOARDS = 10000;
    /** Prints number of tickets into each city if true. */
    private static final boolean PRINTNUMTICKETS = false;

    /** Main method.
     *  @param args not used */
    public static void main(String[] args) {
        Board b;
        if (!(TICKETLISTFILENAME == null)) {
            long seed = RAND.nextLong();
            b = new Board(MAPFILENAME, TICKETFILENAME, seed);
            b.ticketsFromFile(TICKETLISTFILENAME);
            b.saveBoard();
        } else {
            int numRuns = 0;
            do {
                long seed = RAND.nextLong();
                //long seed = -7885638303714419083L;
                b = new Board(MAPFILENAME, TICKETFILENAME, seed);
                b.randomTickets(NUMTICKETS);
                numRuns += 1;
            } while (!b.validBoard() && numRuns < MAXBOARDS);
            System.out.println("Number of boards created: " + numRuns);
            if (numRuns < MAXBOARDS) {
                b.saveBoard();
            }

        }
        if (PRINTNUMTICKETS) {
            b.printFullTicketCount();
        }
        if (DRAWTICKETS) {
            b.drawTickets();
        }
        //b.runTests();
    }
}
