import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.*;

/** Collection of functions for drawing routes and saving them to file.
 *  @author Ryan Huntley
 *  @source Nick's Fonts */
public class TicketDrawer {
    /** Filename of route template*/
    private String filename;
    /** Filename of big city dot. */
    private static final String bigCityFilename = "Pictures\\City_Big.png";
    /** Filename of regular city dot. */
    private static final String regularCityFilename = "Pictures\\City_Regular.png";
    /** Width of canvas in pixels. */
    private static final int WIDTH = 594;
    /** Height of canvas in pixels. */
    private static final int HEIGHT = 374;
    /** Width of line connecting the two cities. */
    private static final double LINEWIDTH = 0.012;
    /** Scaled width of city dot. */
    private static final double CITYWIDTHSCALE = 0.085;
    /** Scaled height of city dot. */
    private static final double CITYHEIGHTSCALE = 0.128;
    /** Font used for number (route length). */
    private static final Font NUMFONT = new Font("ShangriLaNFSmallCaps", Font.PLAIN, 68);
    /** X-coordinate of center of number. */
    private static final double NUMBERX = 0.862;
    /** Y-coordinate of center of number. */
    private static final double NUMBERY = 0.187;
    /** Font used for name of ticket. */
    private static final Font NAMEFONT = new Font("ShangriLaNFSmallCaps", Font.BOLD, 19);
    /** X-coordinate of center of name. */
    private static final double NAMEX = 0.43;
    /** Y-coordinate of center of number. */
    private static final double NAMEY = 0.19;
    /** Path of source folder for TTR. */
    private static final String SOURCE = "C:\\Users\\Ryan\\Desktop\\Ryan_Files\\GitHub\\TicketToRide\\";
    /** Index of current ticket. */
    private int ticketIndex;

    /** Constructor.
     *  @param f filename of route template */
    public TicketDrawer(String f) {
        filename = f;
        ticketIndex = 0;
    }

    /** Draws a route in Standard Draw
     *  @param t ticket to be drawn */
    public void drawTicket(Ticket t) {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.picture(0.5, 0.5, filename, 1, 1);
        drawLine(t);
        drawCities(t);
        drawNumber(t);
        drawName(t);
        String s = "Ticket" + ticketIndex + ".jpg";
        StdDraw.save(s);
        try {
            Files.move(Paths.get(SOURCE + s), Paths.get(SOURCE + "Tickets\\" + s),
                StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        ticketIndex += 1;
    }

    /** Draws the line connecting the two cities on a ticket.
     *  @param t ticket to be drawn */
    private void drawLine(Ticket t) {
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.setPenRadius(LINEWIDTH + 0.004);
        StdDraw.line(t.c1().X(), t.c1().Y(), t.c2().X(), t.c2().Y());
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.setPenRadius(LINEWIDTH);
        StdDraw.line(t.c1().X(), t.c1().Y(), t.c2().X(), t.c2().Y());
    }

    /** Draws both city dots.
     *  @param t ticket to be drawn */
    private void drawCities(Ticket t) {
        String f;
        if (t.c1().type().equals(CityType.BIG)) {
            f = bigCityFilename;
        } else {
            f = regularCityFilename;
        }
        StdDraw.picture(t.c1().X(), t.c1().Y(), f, CITYWIDTHSCALE, CITYHEIGHTSCALE);
        if (t.c2().type().equals(CityType.BIG)) {
            f = bigCityFilename;
        } else {
            f = regularCityFilename;
        }
        StdDraw.picture(t.c2().X(), t.c2().Y(), f, CITYWIDTHSCALE, CITYHEIGHTSCALE);
    }

    /** Draws the number (length) of the ticket.
     *  @param t ticket to be drawn */
    private void drawNumber(Ticket t) {
        StdDraw.setFont(NUMFONT);
        StdDraw.setPenColor(StdDraw.DARK_GRAY);
        StdDraw.text(NUMBERX + 0.002, NUMBERY - 0.002, String.valueOf(t.length()));
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
        StdDraw.text(NUMBERX, NUMBERY, String.valueOf(t.length()));
    }

    /** Draws the name of the ticket (the two cities).
     *  @param t ticket to be drawn */
    private void drawName(Ticket t) {
        StdDraw.setFont(NAMEFONT);
        StdDraw.setPenColor(StdDraw.RED.darker());
        String s = t.c1().name() + " - " + t.c2().name();
        StdDraw.text(NAMEX, NAMEY, s);
    }

    /** For Testing */
    public static void main(String[] args) {
        StdDraw.setCanvasSize(WIDTH, HEIGHT);
        StdDraw.picture(0.5, 0.5, "Pictures\\Route_Template.jpg", 1, 1);
        StdDraw.picture(0.18, 0.37, bigCityFilename, CITYWIDTHSCALE, CITYHEIGHTSCALE);
    }
}
