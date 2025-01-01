package plan;

import map.Graph;
import map.MapNode;

/**
 * A class defining heuristic for A* search
 */
public class AStarHeuristic extends Heuristic {
    /**
     * Initializer
     *
     * @param graph a graph object
     */
    public AStarHeuristic(Graph graph) {
        super(graph);
    }

    /**
     * Returns the estimated cost from the current node to the goal node (heuristic function)
     *
     * @param node     the current node
     * @param goalNode the goal node
     * @return the estimated cost
     */
    public double getHeuristics(MapNode node, MapNode goalNode) {
        //TODO

        // euclidean distance
        return Graph.getDistance(node, goalNode);
    }
}
