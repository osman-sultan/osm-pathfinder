package plan;

import map.MapNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * An abstract class defining planners
 */
public abstract class Planner {

    /**
     * Runs Planning using different search algorithms
     *
     * @param startNode the start node object
     * @param goalNode  the goal node object
     * @return a list of MapNode objects
     */
    public abstract PlanResult plan(MapNode startNode, MapNode goalNode);

    /**
     * Gets the name of a planner
     */
    public abstract String getName();

    /**
     * Returns a list of MapNode objects
     *
     * @param parents  the hashmap representation of a search tree, the value is the parent node of the key
     * @param goalNode the goalNode
     * @return a list of MapNode objects
     */
    protected static List<MapNode> getNodeList(HashMap<MapNode, MapNode> parents, MapNode goalNode) {
        MapNode thisNode = goalNode;
        List<MapNode> nodeList = new ArrayList<>();
        while (thisNode != null) {
            nodeList.add(thisNode);
            thisNode = parents.get(thisNode);
        }
        Collections.reverse(nodeList);
        return nodeList;
    }
}
