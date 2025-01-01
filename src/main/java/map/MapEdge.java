package map;

import java.text.DecimalFormat;


/**
 * A class that defines an directed edge between two nodes in the graph.
 */
public class MapEdge {

    /**
     * the route that this edge belongs to
     */
    public MapRoute mapRoute;

    /**
     * the destination node of this edge
     */
    public MapNode destinationNode;

    /**
     * the source node of this edge
     */
    public MapNode sourceNode;

    /**
     * the accident count of this route
     */
    public Integer accidentsCount;

    public static Graph graph;
    public static DecimalFormat df = new DecimalFormat("#.###");

    /**
     * Initializer
     *
     * @param mapRoute        the route that this edge belongs to
     * @param sourceNode      the source node of the edge
     * @param destinationNode the destination node of the edge
     */
    public MapEdge(MapRoute mapRoute, MapNode sourceNode, MapNode destinationNode) {
        this.mapRoute = mapRoute;
        this.sourceNode = sourceNode;
        this.destinationNode = destinationNode;
        try {
            double lon = Double.parseDouble(df.format(destinationNode.longitude));
            double lat = Double.parseDouble(df.format(destinationNode.latitude));
            accidentsCount = graph.accidents.get(lon).get(lat);
        } catch (NullPointerException e) {
            accidentsCount = null;
        }
    }
}
