package plan;

import map.Graph;
import map.MapNode;

/**
 * An abstract class defining heuristic
 */
public abstract class Heuristic {
    /**
     * a map.Graph object
     */
    protected Graph graph;

    /**
     * Initializer
     *
     * @param graph a map.Graph object
     */
    public Heuristic(Graph graph) {
        this.graph = graph;
    }

    /**
     * Gets the estimated distance from node to goalNode
     *
     * @param node     a node object
     * @param goalNode the goalNode object
     * @return the estimated cost
     */
    public abstract double getHeuristics(MapNode node, MapNode goalNode);
}
