package plan;

import map.Graph;
import map.MapEdge;

/**
 * An abstract class defining cost function
 */
public abstract class CostFunction {
    /**
     * a map.Graph object
     */
    protected Graph graph;

    /**
     * Initializer
     *
     * @param graph a map.Graph object
     */
    public CostFunction(Graph graph) {
        this.graph = graph;
    }

    /**
     * Gets the cost of passing an edge
     *
     * @param edge the edge object
     * @return the cost of passing this edge
     */
    public abstract double getCost(MapEdge edge);
}
