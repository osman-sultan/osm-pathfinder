package plan;

import map.Graph;
import map.MapEdge;

/**
 * A class cost function by distance
 */
public class CostFunctionDistance extends CostFunction {
    /**
     * Initializer
     *
     * @param graph a graph object
     */
    public CostFunctionDistance(Graph graph) {
        super(graph);
    }

    /**
     * Gets the cost of passing an edge
     *
     * @param edge the edge object
     * @return the cost of passing this edge
     */
    public double getCost(MapEdge edge) {
        return Graph.getDistance(edge.sourceNode, edge.destinationNode);
    }
}
