import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

/** Representation of all cities and all connecting routes
 *  on a Ticket to Ride game board.
 *  @author Ryan Huntley */
public class Board {
    /** Set containing all cities on the board. */
    private HashMap<String, City> cities;
    /** Set containing all routes on the board. */
    private HashSet<Route> routes;
    /** Set containing all created tickets. */
    private HashSet<Ticket> tickets;
    /** ArrayList "containing" weights of each city
     *  (each city is represented in the list an amount
     *  of times proportional to its weight). */
    private ArrayList<City> cityWeightList;
    /** JPG for drawing tickets. */
    private String ticketFileJPG;
    /** Seed used to generate rand. */
    private long seed;
    /** Pseudorandom number generator. */
    private Random rand;
    /** Names of all big cities. */
    private HashSet<String> bigCities = new HashSet<>();
    /** Names of special cities to test at the end. */
    private HashSet<String> specialCities = new HashSet<>();

    /** Constructor.
     *  @param mapFilename text file containing all cities and all routes
     *  @param ticketFilename image file that is the basis for all tickets */
    public Board(String mapFilename, String ticketFilename, long s) {
        ticketFileJPG = ticketFilename;
        seed = s;
        rand = new Random(seed);
        cities = new HashMap<>();
        routes = new HashSet<>();
        tickets = new HashSet<>();
        cityWeightList = new ArrayList<>();
        buildCitiesRoutes(mapFilename);
    }

    /** Generates random tickets for this board.
     *  @param numTickets number of tickets to generate */
    public void randomTickets(int numTickets) {
        for (City c : cities.values()) {
            for (int i = 0; i < c.weight(); i += 1) {
                cityWeightList.add(c);
            }
        }
        for (int i = 0; i < numTickets; i += 1) {
            constructTicket();
        }
    }

    /** Creates tickets from a file.
     *  @param filename filename containing all routes to be created */
    public void ticketsFromFile(String filename) {
        seed = 0;
        String currentLine;
        String[] p;
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                currentLine = myReader.nextLine();
                p = currentLine.split(" ");
                tickets.add(new Ticket(this, cities.get(p[0]), cities.get(p[2])));
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
            e.printStackTrace();
        }
    }

    /** @return cities */
    public HashMap<String, City> cities() {
        return cities;
    }

    /** Fills the cities and routes set using mapFilename.
     *  @param filename complete data on the board containing all
     *  cities and all routes */
    private void buildCitiesRoutes(String filename) {
        String currentLine;
        Boolean createCities = true;
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                currentLine = myReader.nextLine();
                if (currentLine.charAt(0) == '-') {
                    createCities = false;
                    continue;
                }
                if (createCities) {
                    constructCityFromLine(currentLine);
                } else {
                    constructRouteFromLine(currentLine);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
            e.printStackTrace();
        }
    }

    /** Helper method for buildCitiesRoutes. Constructs one city based
     *  on a single line read from a file.
     *  @param line line of characters containing all info pertaining
     *  to the city */
    private void constructCityFromLine(String line) {
        String[] p = line.split(" ");
        String n = p[1];
        String x = p[2];
        String y = p[3];
        String t = p[4];
        double X = Double.parseDouble("0." + x);
        double Y = Double.parseDouble("0." + y);

        CityType type;
        switch (t) {
            case "R":
                type = CityType.REGULAR;
                break;
            case "B":
                type = CityType.BIG;
                break;
            case "S":
                type = CityType.SPECIAL;
                break;
            case "X":
                type = CityType.NOTCITY;
                break;
            default:
                throw new IllegalArgumentException();
        }

        City c = new City(n, X, Y, type);
        cities.put(n, c);

        if (t.equals("B")) {
            bigCities.add(n);
        } else if (t.equals("S")) {
            specialCities.add(n);
        }
    }

    /** Helper method for buildCitiesRoutes. Constructs one route based
     *  on a single line read from a file.
     *  @param line line of characters containing all info pertaining
     *  to the route */
    private void constructRouteFromLine(String line) {
        String[] p = line.split(" ");
        String c1 = p[1];
        String c2 = p[2];
        String c = p[3];
        String l = p[4];
        String rT = p[5];
        String nW = p[6];

        if (!cities.containsKey(c1) || !cities.containsKey(c2)) {
            throw new IllegalArgumentException();
        }

        City city1 = cities.get(c1);
        City city2 = cities.get(c2);

        Color color;
        switch (c) {
            case "red":
                color = Color.RED;
                break;
            case "orange":
                color = Color.ORANGE;
                break;
            case "yellow":
                color = Color.YELLOW;
                break;
            case "green":
                color = Color.GREEN;
                break;
            case "blue":
                color = Color.BLUE;
                break;
            case "pink":
                color = Color.PINK;
                break;
            case "white":
                color = Color.WHITE;
                break;
            case "black":
                color = Color.BLACK;
                break;
            case "gray":
                color = Color.GRAY;
                break;
            default:
                throw new IllegalArgumentException();
        }

        int length = Integer.parseInt(l);

        RouteType routeType;
        switch (rT) {
            case "N":
                routeType = RouteType.NORMAL;
                break;
            case "T":
                routeType = RouteType.TUNNEL;
                break;
            case "F":
                routeType = RouteType.FERRY;
                break;
            default:
                throw new IllegalArgumentException();
        }

        int numWilds;
        if (nW.equals("X")) {
            numWilds = 0;
        } else {
            numWilds = Integer.parseInt(nW);
        }

        Route r = new Route(city1, city2, color, length, routeType, numWilds);
        routes.add(r);
    }

    /** Constructs a single ticket (not including the image). */
    private void constructTicket() {
        int size = cityWeightList.size();
        City c1;
        City c2;
        Ticket t;
        while (true) {
            c1 = cityWeightList.get(rand.nextInt(size));
            c2 = cityWeightList.get(rand.nextInt(size));
            if (validCities(c1, c2)) {
                t = new Ticket(this, c1, c2);
                if (!tickets.contains(t)) {
                    break;
                }
            }
        }
        tickets.add(t);
    }

    /** @return true if the two cities constitute a valid ticket
     *  @param c1 the first city
     *  @param c2 the second city */
    private boolean validCities(City c1, City c2) {
        if (c1.type() == CityType.NOTCITY || c2.type() == CityType.NOTCITY) {
            return false;
        }
        if (c1.equals(c2)) {
            return false;
        }
        if (c1.neighborList().contains(c2)) {
            return false;
        }
        return true;
    }

    /** Creates jpgs for all tickets (throws an exception if board was not
     *  constructed with a base image for a ticket). */
    public void drawTickets() {
        TicketDrawer tD = new TicketDrawer(ticketFileJPG);
        for (Ticket t : tickets) {
            tD.drawTicket(t);
        }
    }

    /** Writes all routes (in addition to a few other stats) to a text file. */
    public void saveBoard() {
        ArrayList<String> toBePrinted = new ArrayList<>();
        int counter = 0; //All-purpose

        //General Statistics
        MinMaxObject mm = getMinMax();
        int min = mm.min;
        int max = mm.max;
        City minCity = mm.minCity;
        City maxCity = mm.maxCity;
        toBePrinted.add("Seed: " + seed + "\n");
        toBePrinted.add("\n");
        toBePrinted.add("Minimum number of routes: " + minCity.name() +
            " - " + min + "\n");
        toBePrinted.add("Maximum number of routes: " + maxCity.name() +
            " - " + max + "\n");
        toBePrinted.add("Total number of routes: " + tickets.size() + "\n");
        for (Ticket t : tickets) {
            if (t.length() <= 11) {
                counter += 1;
            }
        }
        toBePrinted.add("Number of routes with length smaller than 12: " + counter + "\n");
        toBePrinted.add("\n");

        //Big Cities
        int[] n;
        counter = 0;
        for (String s : bigCities) {
            n = numSpecificCity(s);
            counter += n[0];
            toBePrinted.add("Number of routes into " + s + ": " + n[0]
                + " " + "(Maximum length: " + n[1] + ")" + "\n");
        }
        toBePrinted.add("\n");
        toBePrinted.add("Total number of routes into big cities: " + counter + "\n");
        counter = 0;
        for (Ticket t : tickets) {
            if (t.c1().type().equals(CityType.BIG) && t.c2().type().equals(CityType.BIG)) {
                counter += 1;
            }
        }
        toBePrinted.add("Total number of routes connecting big cities: " + counter + "\n");
        toBePrinted.add("\n");

        //Special Cities
        for (String s : specialCities) {
            n = numSpecificCity(s);
            toBePrinted.add("Number of routes into " + s + ": " + n[0]
                + " " + "(Maximum length: " + n[1] + ")" + "\n");
        }
        toBePrinted.add("\n");

        //Tickets
        int i = 1;
        for (Ticket t : tickets) {
            String n1 = t.c1().name();
            String n2 = t.c2().name();
            String s = i + ". " + n1 + " to " + n2 + ": " + t.length();
            if (bigCities.contains(n1) || bigCities.contains(n2)) {
                s = s + "          (big city)";
            }
            toBePrinted.add(s + "\n");
            i += 1;
        }

        String filename =  seed + ".txt";
        File f = new File(filename);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter myWriter = new FileWriter(f);
            for (String s : toBePrinted) {
                myWriter.write(s);
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /** Prints the number of tickets (and the length of
     *  the longest ticket) into each city. */
    public void printFullTicketCount() {
        int[] n;
        for (String c : cities.keySet()) {
            n = numSpecificCity(c);
            System.out.println(c + ": " + n[0] + ", " + n[1]);
        }
    }

    /** @return true if board is valid, false otherwise
     *  NOTE: This function should be modified based on user needs. */
    public boolean validBoard() {
        //Checks for (relatively) even distribution
        MinMaxObject mm = getMinMax();
        int min = mm.min;
        int max = mm.max;
        if (min == 0 || max > tickets.size() / 8) {
            return false;
        }

        //Checks big cities
        int[] n;
        int numBigCities = 0;
        for (String s : bigCities) {
            n = numSpecificCity(s);
            if (n[0] < 2) {
                return false;
            }
            numBigCities += n[0];
        }
        if (numBigCities > 120) {
            return false;
        }

        //Checks special cities
        for (String s : specialCities) {
            n = numSpecificCity(s);
            if (n[1] < 15) {
                return false;
            }
        }

        //Checks lengths
        int counter = 0;
        for (Ticket t : tickets) {
            if (t.length() > 14) {
                counter += 1;
            }
        }
        if (counter > 16) {
            return false;
        }

        return true;
    }

    /** Stores cities with most/least routes generated. */
    private class MinMaxObject {
        /** Minimum number of routes stemming from any city. */
        int min;
        /** Maximum number of routes stemming from any city. */
        int max;
        /** City with the least routes. */
        City minCity;
        /** City with the most routes. */
        City maxCity;

        /** Constructor.
         *  @param mIN min
         *  @param mAX max
         *  @param mINCITY minCity
         *  @param mAXCITY maxCity */
        public MinMaxObject(int mIN, int mAX, City mINCITY, City mAXCITY) {
            min = mIN;
            max = mAX;
            minCity = mINCITY;
            maxCity = mAXCITY;
        }
    }

    /** @return the number of routes stemming from a particular city AND
     *  the maximum length route into a particular city
     *  @param name name of city */
    private int[] numSpecificCity(String name) {
        int counter = 0;
        int max = 0;
        for (Ticket t : tickets) {
            if (name.equals(t.c1().name()) || name.equals(t.c2().name())) {
                counter += 1;
                if (t.length() > max) {
                    max = t.length();
                }
            }
        }
        int[] rv = {counter, max};
        return rv;
    }

    /** @return HashMap mapping the maxCity to the its number of tickets and the
     *  minCity to its number of tickets */
    private MinMaxObject getMinMax() {
        MinMaxObject mm;
        int max = 0;
        int min = Integer.MAX_VALUE;
        City maxCity = null;
        City minCity = null;
        int counter;
        for (City c : cities.values()) {
            if (c.type() == CityType.NOTCITY) {
                continue;
            }
            counter = 0;
            for (Ticket t : tickets) {
                if (c.equals(t.c1()) || c.equals(t.c2())) {
                    counter += 1;
                }
            }
            if (counter < min) {
                minCity = c;
                min = counter;
            }
            if (counter > max) {
                maxCity = c;
                max = counter;
            }
        }
        mm = new MinMaxObject(min, max, minCity, maxCity);
        return mm;
    }

    /** For testing. */
    public void runTests() {
        TicketDrawer r = new TicketDrawer("Pictures\\Route_Template.jpg");
        City c1 = cities.get("Sochi");
        City c2 = cities.get("Edinburgh");
        Ticket t = new Ticket(this, c1, c2);
        r.drawTicket(t);
    }
}
