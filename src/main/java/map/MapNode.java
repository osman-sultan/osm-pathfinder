package map;

import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;


/**
 * A class that defines a node in the graph. Each node has a unique ID as well as stores the longitude
 * and latitude of the location. Refer to <a>https://wiki.openstreetmap.org/wiki/Node</a> for more details.
 */
public class MapNode {

    /**
     * the id of this node
     */
    public long id;

    /**
     * the longitude of this node
     */
    public double longitude;

    /**
     * the latitude of this node
     */
    public double latitude;

    /**
     * a list of edges that this node connects to
     */
    public List<MapEdge> edges;

    /**
     * Initializer
     *
     * @param e an xml element representing a node
     */
    public MapNode(Element e) {
        id = Long.parseLong(e.getAttribute("id"));
        longitude = Double.parseDouble(e.getAttribute("lon"));
        latitude = Double.parseDouble(e.getAttribute("lat"));
        edges = new ArrayList<>();
    }
}
