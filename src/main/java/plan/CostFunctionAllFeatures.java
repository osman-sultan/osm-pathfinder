package plan;

import map.Graph;
import map.MapEdge;

/**
 * A class cost function by all features
 */
public class CostFunctionAllFeatures extends CostFunction {
    /**
     * Initializer
     *
     * @param graph a graph object
     */
    public CostFunctionAllFeatures(Graph graph) {
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
        // Tired to carry the bicycle if the route is stairs 
        if (edge.mapRoute.steps) {
            cost = cost * 5;
        }
        // Not safe if the route doesn't have a bike lane
        if (!edge.mapRoute.bikeLane) {
            cost = cost + euclideanDistance;
        }
        // Want to choose a wide road
        if (edge.mapRoute.lanes < 3) {
            cost = cost + euclideanDistance;
        }
        //  Not safe if speed limit is high
        if (edge.mapRoute.maxSpeed > 80) {
            cost = cost + euclideanDistance * 0.5;
        }
        // More accidents more cost
        if (edge.accidentsCount != null) {
            cost = cost + euclideanDistance * 0.1 * edge.accidentsCount;
        }
        return cost;
    }
}
