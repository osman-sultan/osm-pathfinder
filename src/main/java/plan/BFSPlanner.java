package plan;

import map.MapEdge;
import map.MapNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * A class defining planning using BFS search
 */
public class BFSPlanner extends Planner {
    /**
     * Runs Breadth First Search
     *
     * @param startNode the start node
     * @param goalNode  the goal node
     * @return a list of MapNode objects
     */
    @Override
    public PlanResult plan(MapNode startNode, MapNode goalNode) {
        HashMap<MapNode, MapNode> parents = new HashMap<>();
        LinkedList<MapNode> queue = new LinkedList<>(); // Create a queue for BFS
        Set<MapNode> expandedNodes = new HashSet<>();

        parents.put(startNode, null);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            MapNode node = queue.poll();
            expandedNodes.add(node);
            if (node.id == goalNode.id) {
                return new PlanResult(expandedNodes.size(), getNodeList(parents, goalNode));
            }
            for (MapEdge edge : node.edges) {
                MapNode nextNode = edge.destinationNode;
                if (!parents.containsKey(nextNode)) {
                    parents.put(nextNode, node);
                    queue.add(nextNode);
                }
            }
        }
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
