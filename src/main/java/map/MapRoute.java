package map;

import org.w3c.dom.Element;

import java.util.List;


/**
 * a class that defines a route in the map. A route includes a collection of nodes that make up buildings,
 * parks, roads, etc. It contains a list of IDs of all nodes within the route, as well as other information.
 * Refer to <a>https://wiki.openstreetmap.org/wiki/Way</a> for more details.
 */
public class MapRoute {

    /**
     * the id of this route
     */
    public long routeId;

    /**
     * the name of this route
     */
    public String routeName;

    /**
     * the type of this route
     */
    public String routeType;

    /**
     * nodes within this route
     */
    public List<Long> nodeIds;

    /**
     * whether this route has a bike lane
     */
    public boolean bikeLane;

    /**
     * whether this route is steps (stairs)
     */
    public boolean steps = false;

    /**
     * maximum speed of this route
     */
    public int maxSpeed;

    /**
     * number of lanes of this route
     */
    public int lanes;

    /**
     * Initializer
     *
     * @param route     an xml element representing a route
     * @param routeName the name of the route
     * @param routeType the type of the route
     * @param bikeLane  whether the route has a bike lane
     * @param maxSpeed  the maximum speed of the route
     * @param lanes     number of lanes of the route
     */
    public MapRoute(Element route, String routeName, String routeType, boolean bikeLane, int maxSpeed, int lanes) {
        routeId = Long.parseLong(route.getAttribute("id"));
        this.routeName = routeName;
        this.routeType = routeType;
        this.maxSpeed = maxSpeed;
        this.lanes = lanes;

        if (routeType.equals("cycleway")) {
            this.bikeLane = true;
        } else {
            this.bikeLane = bikeLane;
        }

        if (routeType.equals("steps")) {
            steps = true;
        }
    }
}
