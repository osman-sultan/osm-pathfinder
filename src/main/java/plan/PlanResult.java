package plan;

import map.MapNode;

import java.util.List;

/**
 * A class defining the result of a planner
 */
public class PlanResult {
    /**
     * number of node expanded
     */
    public int expandedNodeCount;
    /**
     * path found by the planner
     */
    public List<MapNode> path;

    public PlanResult(int expandedNodeCount, List<MapNode> path) {
        this.expandedNodeCount = expandedNodeCount;
        this.path = path;
    }
}
