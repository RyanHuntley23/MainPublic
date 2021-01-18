package bearmaps.proj2c;
import bearmaps.proj2ab.ArrayHeapMinPQ;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.Stopwatch;

/** Computes the shortest distance from a start vertex to an end vertex
 *  using a memory optimized, AStar algorithm.
 * @author Ryan Huntley */
public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    /** The outcome of the algorithm (SOLVED, TIMEOUT, or UNSOLVABLE). */
    private SolverOutcome outcome;
    /** List of all vertices passed through en route from start to end
     *  (in order). */
    private List<Vertex> solution;
    /** Sum of weights of all edges in solution. */
    private double solutionWeight;
    /** Total number of states considered by algorithm. */
    private int numStatesExplored;
    /** Total time that algorithm took to run. */
    private double explorationTime;
    /** Maps each vertex to an integer corresponding to the best known
     *  distance from the start to that vertex. */
    private HashMap<Vertex, Double> distTo;
    /** Maps each vertex V to the vertex that would come before V in the
     *  shortest path from V to the start. */
    private HashMap<Vertex, Vertex> edgeTo;
    /** MinPQ containing every Vertex seen but not relaxed so far. */
    private ArrayHeapMinPQ<Vertex> vertexPQ;

    /** Constructor (runs the algorithm and saves all the data in instance
     *  variables).
     *  @param input the graph of all vertices
     *  @param start the starting vertex
     *  @param end the goal vertex
     *  @param timeout the maximum acceptable time that the constructor
     *  will run for */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start,
                       Vertex end, double timeout) {

        Vertex currentVertex;
        vertexPQ = new ArrayHeapMinPQ<>();
        vertexPQ.add(start, input.estimatedDistanceToGoal(start, end));
        distTo = new HashMap<>();
        distTo.put(start, 0.0);
        edgeTo = new HashMap<>();
        edgeTo.put(start, null);

        Stopwatch sw = new Stopwatch();
        while (vertexPQ.size() > 0 && !vertexPQ.getSmallest().equals(end)
               && sw.elapsedTime() < timeout) {

            currentVertex = vertexPQ.removeSmallest();
            numStatesExplored += 1;
            for (WeightedEdge<Vertex> e : input.neighbors(currentVertex)) {
                relax(input, end, e);
            }
        }
        explorationTime = sw.elapsedTime();

        solution = new ArrayList<>();

        if (explorationTime >= timeout) {
            outcome = SolverOutcome.TIMEOUT;
            solutionWeight = 0.0;
        } else if (vertexPQ.size() == 0) {
            outcome = SolverOutcome.UNSOLVABLE;
            solutionWeight = 0.0;
        } else {
            outcome = SolverOutcome.SOLVED;
            constructSolution(end);
            solutionWeight = distTo.get(end);
        }
    }

    /** Relaxes an edge and updates the best known path to the new vertex.
     *  Also adds the new vertex to our minPQ if it is not already in it.
     *  @param input the graph of all vertices
     *  @param end the goal vertex
     *  @param e the edge being relaxed */
    private void relax(AStarGraph<Vertex> input, Vertex end,
                       WeightedEdge<Vertex> e) {
        Vertex currentV = e.from();
        Vertex newV = e.to();
        double weight = e.weight();
        double guess;

        if (vertexPQ.contains(newV)) {
            double newDist = distTo.get(currentV) + weight;
            if (newDist < distTo.get(newV)) {
                distTo.put(newV, newDist);
                edgeTo.put(newV, currentV);
                guess = newDist + input.estimatedDistanceToGoal(newV, end);
                vertexPQ.changePriority(newV, guess);
            }
        } else {
            double newDist = distTo.get(currentV) + weight;
            distTo.put(newV, newDist);
            edgeTo.put(newV, currentV);
            guess = newDist + input.estimatedDistanceToGoal(newV, end);
            vertexPQ.add(newV, guess);
        }
    }

    /** Builds our solution list by recursively walking back through
     *  the edgeTo map.
     *  @param currentV the active vertex (will be added after every
     *  preceding vertex) */
    private void constructSolution(Vertex currentV) {
        if (currentV == null) {
            return;
        }
        constructSolution(edgeTo.get(currentV));
        solution.add(currentV);
    }

    /** @return one of three outcomes (SOLVED, TIMEOUT, or UNSOLVABLE) */
    public SolverOutcome outcome() {
        return outcome;
    }

    /** @return the shortest path list (empty list if outcome was
     *  TIMEOUT or UNSOLVABLE) */
    public List<Vertex> solution() {
        return solution;
    }

    /** @return the total weight of the solution */
    public double solutionWeight() {
        return solutionWeight;
    }

    /** @return the number of states that were explored by AStar (0 if
     *  outcome was TIMEOUT or UNSOLVABLE) */
    public int numStatesExplored() {
        return numStatesExplored;
    }

    /** @return the total time spent by the constructor in finding
     *  the solution */
    public double explorationTime() {
        return explorationTime;
    }
}
