package plan;

import map.MapEdge;
import map.MapNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * A class defining planning using DFS search
 */
public class DFSPlanner extends Planner {
    private int maxDepth;

    public DFSPlanner(int maxDepth) {
        super();
        this.maxDepth = maxDepth;
    }

    /**
     * Runs Depth First Search
     *
     * @param startNode the start node
     * @param goalNode  the goal node
     * @return a list of MapNode objects
     */
    @Override
    public PlanResult plan(MapNode startNode, MapNode goalNode) {
        HashMap<MapNode, MapNode> parents = new HashMap<>();
        Stack<ChildParentNodePair> stack = new Stack<>();
        HashMap<MapNode, Integer> nodeDepth = new HashMap<>();
        Set<MapNode> expandedNodes = new HashSet<>();

        stack.push(new ChildParentNodePair(startNode, null, 1));

        while (!stack.isEmpty()) {
            ChildParentNodePair pair = stack.pop();
            if (pair.childNodeDepth <= this.maxDepth) {
                if (!nodeDepth.containsKey(pair.child) || nodeDepth.get(pair.child) > pair.childNodeDepth) {
                    nodeDepth.put(pair.child, pair.childNodeDepth);
                    parents.put(pair.child, pair.parent);
                    expandedNodes.add(pair.child);
                    MapNode node = pair.child;
                    if (node.id == goalNode.id) {
                        return new PlanResult(expandedNodes.size(), getNodeList(parents, goalNode));
                    }
                    for (MapEdge edge : node.edges) {
                        MapNode nextNode = edge.destinationNode;
                        stack.add(new ChildParentNodePair(nextNode, node, pair.childNodeDepth + 1));
                    }
                }
            }
        }
        return new PlanResult(expandedNodes.size(), null);
    }

    private static class ChildParentNodePair {
        MapNode child;
        MapNode parent;
        int childNodeDepth;

        public ChildParentNodePair(MapNode child, MapNode parent, int childNodeDepth) {
            this.child = child;
            this.parent = parent;
            this.childNodeDepth = childNodeDepth;
        }
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
