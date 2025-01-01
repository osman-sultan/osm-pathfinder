package plan;

import map.MapEdge;
import map.MapNode;

import java.util.*;

/**
 * A class defining planning using A* search
 */
public class AStarPlanner extends Planner {
    /**
     * heuristics used for A*
     */
    Heuristic heuristic;
    /**
     * cost function used for A*
     */
    CostFunction costFunction;

    /**
     * Initializer
     *
     * @param heuristic a heuristic object
     * @param costFunction    cost function option
     */
    public AStarPlanner(Heuristic heuristic, CostFunction costFunction) {
        super();
        //TODO
        this.heuristic = heuristic;
        this.costFunction = costFunction;
    }

    /**
     * Runs A* search
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
        // prio queue to store and sort nodes by sum of cost and heuristic ie f(n) = g(n) + h(n)
        PriorityQueue<MapNode> queue = new PriorityQueue<>(Comparator.comparingDouble(node ->
                costSoFar.getOrDefault(node, Double.POSITIVE_INFINITY) + heuristic.getHeuristics(node, goalNode)));

        // initialize the search; start node has no parent and zero cost
        parents.put(startNode, null);
        costSoFar.put(startNode, 0.0);
        queue.add(startNode);

        // search until no more nodes to explore
        while (!queue.isEmpty()) {
            // retrieves and remove node with lowest f(n) from queue
            MapNode currentNode = queue.poll();
            expandedNodes.add(currentNode);

            if (currentNode.id == goalNode.id) {
                // if goal found, reconstruct return path
                return new PlanResult(expandedNodes.size(), getNodeList(parents, goalNode));
            }
            // explore all edges and adjacent nodes of the current node
            for (MapEdge edge : currentNode.edges) {
                MapNode neighbor = edge.destinationNode;
                double newCost = costSoFar.get(currentNode) + costFunction.getCost(edge);
                // if next node has not been visited or a cheaper path is found, update cost and parent
                if (!costSoFar.containsKey(neighbor) || newCost < costSoFar.get(neighbor)) {
                    costSoFar.put(neighbor, newCost);
                    parents.put(neighbor, currentNode);
                    // Re-queue the neighbor with the new cost.
                    queue.remove(neighbor);
                    queue.add(neighbor);
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
