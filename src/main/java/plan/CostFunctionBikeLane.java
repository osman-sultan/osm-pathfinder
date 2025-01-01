package plan;

import map.Graph;
import map.MapEdge;

/**
 * A class cost function by bike lane
 */
public class CostFunctionBikeLane extends CostFunction {
    /**
     * Initializer
     *
     * @param graph a graph object
     */
    public CostFunctionBikeLane(Graph graph) {
        super(graph);
    }

    /**
     * Gets the cost of passing an edge
     *
     * @param edge the edge object
     * @return the cost of passing this edge
     */
    public double getCost(MapEdge edge) {
        double euclideanDistance = Graph.getDistance(edge.sourceNode, edge.destinationNode);
        double cost = euclideanDistance;
        if (edge.mapRoute.steps) {
            cost = cost * 5;
        } else if (!edge.mapRoute.bikeLane) {
            cost = cost + euclideanDistance;
        }
        return cost;
    }
}
