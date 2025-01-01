package map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class representing the whole graph
 */
public class Graph {

    /**
     * xml document representing the osm file
     */
    private Document osmDoc;

    /**
     * the coordinate for focusing
     */
    public double[] focus;

    /**
     * a hashmap containing all nodes in this graph; key: node id, value: map.MapNode object
     */
    public HashMap<Long, MapNode> nodes;

    /**
     * a hashmap containing all nodes in a route; key: node id, value: map.MapNode object
     */
    public HashMap<Long, MapNode> routeNodes;

    /**
     * a hashmap containing all routes in this graph; key: route id, value: map.MapRoute object
     */
    public HashMap<Long, MapRoute> routes;

    /**
     * a nested hashmap containing accident count at a coordinate; outside key: longitude,
     * inside key: latitude, value: accident count
     */
    public HashMap<Double, HashMap<Double, Integer>> accidents; //longitude, latitude

    /**
     * earth radius in meters
     */
    public static final double EARTH_RADIUS = 6371000;

    /**
     * Initializer
     */
    public Graph() {
        this("./data/toronto.osm", "./data/Cyclists.csv");
    }

    /**
     * Initializer
     *
     * @param osmFilePath       the osm file path
     * @param accidentsFilePath the accidents file path
     */
    public Graph(String osmFilePath, String accidentsFilePath) {
        accidents = new HashMap<>();
        nodes = new HashMap<>();
        routeNodes = new HashMap<>();
        routes = new HashMap<>();

        loadFiles(osmFilePath, accidentsFilePath);
        getFocus();

        MapEdge.graph = this;
        buildGraph();
    }

    /**
     * Loads the osm file to this osmDoc object
     *
     * @param osmFilePath       the path of osm file
     * @param accidentsFilePath the path of the accidents file
     */
    public void loadFiles(String osmFilePath, String accidentsFilePath) {
        // load osm file
        try {
            File file = new File(osmFilePath);
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            osmDoc = db.parse(file);
            osmDoc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // load toronto police csv file
        BufferedReader br = null;
        String line = "";
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.FLOOR);
        try {
            br = new BufferedReader(new FileReader(accidentsFilePath));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] entry = line.split(",");
                double lat = Double.parseDouble(df.format(Double.parseDouble(entry[15])));
                double lon = Double.parseDouble(df.format(Double.parseDouble(entry[16])));
                if (accidents.get(lon) == null) {
                    accidents.put(lon, new HashMap<>());
                }
                HashMap<Double, Integer> latMap = accidents.get(lon);
                if (latMap.get(lat) == null) {
                    latMap.put(lat, 1);
                } else {
                    latMap.put(lat, latMap.get(lat) + 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses xml document object osmDoc into a graph object
     */
    private void buildGraph() {
        NodeList nodeList = osmDoc.getElementsByTagName("node");
        NodeList routeList = osmDoc.getElementsByTagName("way");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element node = (Element) nodeList.item(i);
            MapNode newNode = new MapNode(node);
            nodes.put(newNode.id, newNode);
        }
        for (int i = 0; i < routeList.getLength(); i++) {
            Element route = (Element) routeList.item(i);
            boolean useMe = false;
            boolean oneWay = false;
            boolean bikeLane = false;
            String routeName = "unnamed route";
            String routeType = "";
            int maxSpeed = -1;
            int lanes = -1;
            List<Long> nodeIdList = new ArrayList<>();

            // this for loop is not inside the map.MapRoute init function because not every way is a route
            NodeList tagsForRoute = route.getElementsByTagName("tag");
            for (int j = 0; j < tagsForRoute.getLength(); j++) {
                Element tag = (Element) tagsForRoute.item(j);
                if (tag.getAttribute("k").equals("highway")) {
                    useMe = true;
                    routeType = tag.getAttribute("v");
                } else if (tag.getAttribute("k").equals("name")) {
                    routeName = tag.getAttribute("v");
                } else if (tag.getAttribute("k").equals("oneway") && tag.getAttribute("v").equals("yes")) {
                    oneWay = true;
                } else if (tag.getAttribute("k").equals("cycleway") || (tag.getAttribute("k").equals("bicycle") && (tag.getAttribute("v").equals("yes") || tag.getAttribute("v").equals("designated")))) {
                    bikeLane = true;
                } else if (tag.getAttribute("k").equals("maxspeed")) {
                    maxSpeed = Integer.parseInt(tag.getAttribute("v"));
                } else if (tag.getAttribute("k").equals("lanes")) {
                    lanes = Character.getNumericValue(tag.getAttribute("v").charAt(0));
                }
            }
            if (useMe) {
                MapRoute newRoute = new MapRoute(route, routeName, routeType, bikeLane, maxSpeed, lanes);
                NodeList nodesInRoute = route.getElementsByTagName("nd");
                for (int j = 0; j < nodesInRoute.getLength(); j++) {
                    Element nd = (Element) nodesInRoute.item(j);
                    nodeIdList.add(Long.parseLong(nd.getAttribute("ref")));
                }
                long thisNode = nodeIdList.get(0);
                long nextNode;
                for (int j = 1; j < nodeIdList.size(); j++) {
                    nextNode = nodeIdList.get(j);
                    nodes.get(thisNode).edges.add(new MapEdge(newRoute, nodes.get(thisNode), nodes.get(nextNode)));
                    thisNode = nextNode;
                }
                if (!oneWay) {
                    thisNode = nodeIdList.get(nodeIdList.size() - 1);
                    for (int j = nodeIdList.size() - 2; j > -1; j--) {
                        nextNode = nodeIdList.get(j);
                        nodes.get(thisNode).edges.add(new MapEdge(newRoute, nodes.get(thisNode), nodes.get(nextNode)));
                        thisNode = nextNode;
                    }
                }
                newRoute.nodeIds = nodeIdList;
                routes.put(newRoute.routeId, newRoute);
                for (long nodeId : nodeIdList) {
                    routeNodes.put(nodeId, nodes.get(nodeId));
                }
            }
        }
//        System.out.println(String.format("number of highway nodes: %d", routeNodes.size()));
    }

    /**
     * Gets the coordinate for focusing
     */
    private void getFocus() {
        NodeList boundsList = osmDoc.getElementsByTagName("bounds");
        Element bounds = (Element) boundsList.item(0);
        double minLat = Double.parseDouble(bounds.getAttribute("minlat"));
        double maxLat = Double.parseDouble(bounds.getAttribute("maxlat"));
        double minLon = Double.parseDouble(bounds.getAttribute("minlon"));
        double maxLon = Double.parseDouble(bounds.getAttribute("maxlon"));
        focus = new double[]{(minLon + maxLon) / 2, (minLat + maxLat) / 2};
    }

    /**
     * Returns the distance from the source node to the destination node
     *
     * @param sourceNode      the source node
     * @param destinationNode the destination node
     * @return the distance between two nodes
     */
    public static double getDistance(MapNode sourceNode, MapNode destinationNode) {
        double lon1 = Math.toRadians(destinationNode.longitude);
        double lon2 = Math.toRadians(sourceNode.longitude);
        double lat1 = Math.toRadians(destinationNode.latitude);
        double lat2 = Math.toRadians(sourceNode.latitude);
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return c * EARTH_RADIUS;
    }
}
