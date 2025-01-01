package main;

import map.Graph;
import map.MapEdge;
import map.MapNode;
import map.WayPointAdapter;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import plan.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A class for run route planning and display on the map
 */
public class Demo {
    Graph graph;
    List<Planner> planners;

    /**
     * Initializer
     *
     * @param graph    a graph object
     * @param planners a list of planners
     */
    public Demo(Graph graph, List<Planner> planners) {
        this.graph = graph;
        this.planners = planners;
    }

    /**
     * @return the list of planners to run route planning
     */
    public List<Planner> getPlanners() {
        return planners;
    }

    /**
     * Initializes the map viewer
     */
    public void initializeMapViewer() {
        JXMapViewer mapViewer = new JXMapViewer();

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);
        // Use 8 threads in parallel to load the tiles
        tileFactory.setThreadPoolSize(8);

        // Set the focus
        GeoPosition toronto = new GeoPosition(graph.focus[1], graph.focus[0]);
        mapViewer.setZoom(3);
        mapViewer.setAddressLocation(toronto);

        // Add mouse listeners for panning and zooming
        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        // Add mouse listener for placing waypoints
        JFrame frame = new JFrame("OpenStreetAStar");
        new WayPointAdapter(mapViewer, graph.routeNodes, this);

        // Display the viewer in a JFrame
        frame.setLayout(new BorderLayout());
        String text = "Use left mouse button to pan, mouse wheel to zoom and right mouse button to set waypoints";
        frame.add(new JLabel(text), BorderLayout.NORTH);
        frame.getContentPane().add(mapViewer);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Returns a list of solutions by running selected algorithms
     *
     * @param startNode the start node
     * @param endNode   the end node
     * @return a list of paths
     */
    public List<List<GeoPosition>> runSearches(MapNode startNode, MapNode endNode, List<Planner> planners) {
        List<List<GeoPosition>> solutions = new ArrayList<>();
        for (Planner planner : planners) {
            PlanResult result = planner.plan(startNode, endNode);
            System.out.printf("Result of Planner: %s:%n", planner.getName());
            printResult(result);

            List<GeoPosition> geoList = new ArrayList<>();
            if (result.path != null) {
                for (MapNode node : result.path) {
                    geoList.add(new GeoPosition(node.latitude, node.longitude));
                }
            }
            solutions.add(geoList);
        }
        return solutions;
    }

    /**
     * Prints plan result
     *
     * @param result plan result
     */
    public void printResult(PlanResult result) {
        System.out.printf("Number of nodes expanded: %d%n", result.expandedNodeCount);
        if (result.path != null) {
            System.out.println("Path found: ");
            StringBuilder path = new StringBuilder();
            for (int i = 0; i < result.path.size(); i++) {
                path.append(result.path.get(i).id);
                if (i < result.path.size() - 1) {
                    path.append("->");
                }
            }
            System.out.println(path);
            System.out.printf("Path cost: %f%n", getPathCost(new CostFunctionAllFeatures(graph), result.path));
        } else {
            System.out.println("No path found");
        }
        System.out.println();
    }

    /**
     * Gets the total path cost
     *
     * @param costFunction cost function to use
     * @param path         path as a list of MapNode
     * @return path cost
     */
    public static double getPathCost(CostFunction costFunction, List<MapNode> path) {
        double cost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            MapNode curNode = path.get(i);
            MapNode nextNode = path.get(i + 1);
            for (MapEdge edge : curNode.edges) {
                if (edge.destinationNode.id == nextNode.id) {
                    cost += costFunction.getCost(edge);
                }
            }
        }
        return cost;
    }

    /**
     * Main function
     *
     * @param args args
     */
    public static void main(String[] args) {
        String osmFile = "./data/toronto.osm";
        String cyclistsAccidentFile = "./data/Cyclists.csv";
        Graph torontoGraph = new Graph(osmFile, cyclistsAccidentFile);

        CostFunction costFunction = new CostFunctionAllFeatures(torontoGraph);
        Heuristic heuristic = new AStarHeuristic(torontoGraph);

//        Planner bfsPlanner = new BFSPlanner();
//        Planner dfsPlanner = new DFSPlanner(500);
//
//        Planner uniformCostPlanner = new UniformCostPlanner(costFunction);
//        Planner greedyBestFirstPlanner = new GreedyBestFirstPlanner(heuristic);
        Planner aStarPlanner = new AStarPlanner(heuristic, costFunction);

        List<Planner> planners = new ArrayList<>();
//        planners.add(bfsPlanner);
//        planners.add(dfsPlanner);
//        planners.add(uniformCostPlanner);
//        planners.add(greedyBestFirstPlanner);
        planners.add(aStarPlanner);

        //Show result on mapViewer
        Demo demo = new Demo(torontoGraph, planners);
        demo.initializeMapViewer();
    }
}
