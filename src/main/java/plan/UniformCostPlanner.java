package plan;

import map.MapEdge;
import map.MapNode;

import java.util.*;

/**
 * A class defining planning using Uniform Cost Search
 */
public class UniformCostPlanner extends Planner {
    /**
     * heuristics used for Uniform Cost Search
     */
    CostFunction costFunction;

    /**
     * Initializer
     *
     * @param costFunction a costFunction object
     */
    public UniformCostPlanner(CostFunction costFunction) {
        super();
        this.costFunction = costFunction;
    }

    /**
     * Runs Uniform Cost Search
     *
     * @param startNode the start node
     * @param goalNode  the goal node
     * @return a list of MapNode objects
     */
    @Override
    public PlanResult plan(MapNode startNode, MapNode goalNode) {
        //TODO
        HashMap<MapNode, MapNode> parents = new HashMap<>();
        Set<MapNode> expandedNodes = new HashSet<>();

        // map each node to the cost to reach it
        HashMap<MapNode, Double> costSoFar = new HashMap<>();
        // prio queue to store and sort nodes by cost g(n)
        PriorityQueue<MapNode> queue = new PriorityQueue<>(
                Comparator.comparingDouble(node -> costSoFar.getOrDefault(node, Double.POSITIVE_INFINITY))
        );

        // initialize the search; start node has no parent and zero cost
        parents.put(startNode, null);
        costSoFar.put(startNode, 0.0);
        queue.add(startNode);

        // search until no more nodes to explore
        while (!queue.isEmpty()) {
            // retrieves and removes node with lowest cost g(n) from queue
            MapNode currentNode = queue.poll();
            expandedNodes.add(currentNode);

            if (currentNode.id == goalNode.id) {
                // if goal found, reconstruct return path
                return new PlanResult(expandedNodes.size(), getNodeList(parents, goalNode));
            }
            // explore all edges and adjacent nodes of the current node
            for (MapEdge edge : currentNode.edges) {
                MapNode nextNode = edge.destinationNode;
                double newCost = costSoFar.get(currentNode) + this.costFunction.getCost(edge);
                // if next node has not been visited or a cheaper path is found, update cost and parent
                if (!costSoFar.containsKey(nextNode) || newCost < costSoFar.get(nextNode)) {
                    costSoFar.put(nextNode, newCost);
                    parents.put(nextNode, currentNode);
                    if (!queue.contains(nextNode)) {
                        queue.add(nextNode);
                    }
                }
            }
        }
        // if the goal node is not reachable, return a null path
        return new PlanResult(expandedNodes.size(), null);
    }

    /**
     * Gets the name of the planner
     *
     * @return planner name
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
