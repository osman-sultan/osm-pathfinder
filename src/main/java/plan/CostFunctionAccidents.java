package plan;

import map.Graph;
import map.MapEdge;

/**
 * A class cost function by accidents
 */
public class CostFunctionAccidents extends CostFunction {
    /**
     * Initializer
     *
     * @param graph a graph object
     */
    public CostFunctionAccidents(Graph graph) {
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
        }
        if (!edge.mapRoute.bikeLane) {
            cost = cost + euclideanDistance;
        }
        if (edge.mapRoute.lanes < 3) {
            cost = cost + euclideanDistance;
        }
        if (edge.mapRoute.maxSpeed > 80) {
            cost = cost + euclideanDistance * 0.5;
        }
        if (edge.accidentsCount != null) {
            cost = cost + euclideanDistance * 100;
        }
        return cost;
    }
}
