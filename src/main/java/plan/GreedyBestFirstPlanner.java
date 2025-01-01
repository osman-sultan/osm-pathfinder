package plan;

import map.MapEdge;
import map.MapNode;

import java.util.*;

/**
 * A class defining planning using Best First Search
 */
public class GreedyBestFirstPlanner extends Planner {
    /**
     * heuristics used for Best First Search
     */
    Heuristic heuristic;

    /**
     * Initializer
     *
     * @param heuristic a heuristic object
     */
    public GreedyBestFirstPlanner(Heuristic heuristic) {
        super();
        //TODO
        this.heuristic = heuristic;
    }

    /**
     * Runs Best First Search
     *
     * @param startNode the start node
     * @param goalNode  the goal node
     * @return a list of MapNode objects
     */
    @Override
    public PlanResult plan(MapNode startNode, MapNode goalNode) {
        // TODO
        HashMap<MapNode, MapNode> parents = new HashMap<>();
        Set<MapNode> expandedNodes = new HashSet<>();

        // prio queue to store and sort nodes by heuristic h(n)
        PriorityQueue<MapNode> queue = new PriorityQueue<>(Comparator.comparingDouble(node -> heuristic.getHeuristics(node, goalNode)));

        // initialize the search; start node has no parent and zero cost
        parents.put(startNode, null);
        queue.add(startNode);

        // search until no more nodes to explore
        while (!queue.isEmpty()) {
            /// retrieves and remove node with lowest heuristic estimate h(n) from queue
            MapNode currentNode = queue.poll();
            expandedNodes.add(currentNode);

            if (currentNode.id == goalNode.id) {
                // if goal found, reconstruct return path
                return new PlanResult(expandedNodes.size(), getNodeList(parents, goalNode));
            }
            // explore all edges and adjacent nodes of the current node
            for (MapEdge edge : currentNode.edges) {
                MapNode neighbor = edge.destinationNode;
                // if node has not been visited, add it to queue.
                if (!parents.containsKey(neighbor)) {
                    parents.put(neighbor, currentNode);
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
