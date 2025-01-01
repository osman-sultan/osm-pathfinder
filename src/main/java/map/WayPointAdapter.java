package map;

import main.Demo;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.*;

/**
 * an adapter class for adapting data source to UI components and handling mouse events
 */
public class WayPointAdapter extends MouseAdapter {
    private JXMapViewer mapViewer;
    private MapNode closestNode = null;
    private MapNode startNode = null;
    private MapNode endNode = null;

    private Set<Waypoint> waypoints;
    private List<List<GeoPosition>> routes;
    private PreviewPainter previewPainter;
    private Demo demo;

    private HashMap<Long, MapNode> nodes;
    private List<Painter<JXMapViewer>> painters;

    /**
     * @param viewer the jxmapviewer
     */
    public WayPointAdapter(JXMapViewer viewer, HashMap<Long, MapNode> nodes, Demo demo) {

        this.mapViewer = viewer;
        this.nodes = nodes;
        this.demo = demo;

        previewPainter = new PreviewPainter();
        previewPainter.setAdapter(this);

        waypoints = new HashSet<>();
        routes = new ArrayList<>();
        painters = new ArrayList<>();

        painters.add(previewPainter);
        mapViewer.addMouseListener(this);
        mapViewer.addMouseMotionListener(this);
        mapViewer.setOverlayPainter(previewPainter);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON3)
            return;
        if (startNode != null && endNode != null) {
            resetWayPoints();
        } else if (startNode == null) {
            startNode = closestNode;
            System.out.printf("Start node id: %s%n", startNode.id);
            GeoPosition newGeo = new GeoPosition(closestNode.latitude, closestNode.longitude);
            waypoints.add(new DefaultWaypoint(newGeo));
        } else if (endNode == null) {
            endNode = closestNode;
            System.out.printf("End node id: %s%n", endNode.id);
            routes = demo.runSearches(startNode, endNode, demo.getPlanners());
            GeoPosition newGeo = new GeoPosition(closestNode.latitude, closestNode.longitude);
            waypoints.add(new DefaultWaypoint(newGeo));

        }

        // Create a waypoint painter and route painter
        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setWaypoints(waypoints);
        RoutePainter routePainter = new RoutePainter(routes);

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        painters.add(routePainter);
        painters.add(waypointPainter);
        mapViewer.setOverlayPainter(new CompoundPainter<>(painters));
        mapViewer.repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        // get the geo location of where the mouse is
        JXMapViewer l_src = (JXMapViewer) e.getSource();
        Rectangle viewportBounds = l_src.getViewportBounds();
        int wx = viewportBounds.x + e.getX();
        int wy = viewportBounds.y + e.getY();
        GeoPosition geo = l_src.getTileFactory().pixelToGeo(new Point(wx, wy), l_src.getZoom());

        double minDiff = Double.POSITIVE_INFINITY;
        for (long key : nodes.keySet()) {
            double diff = Math.abs(geo.getLatitude() - nodes.get(key).latitude) + Math.abs(geo.getLongitude() - nodes.get(key).longitude);
            if (diff < minDiff) {
                minDiff = diff;
                closestNode = nodes.get(key);
            }
        }
        mapViewer.repaint();
    }

    public MapNode getClosestNode() {
        return closestNode;
    }

    private void resetWayPoints() {
        startNode = null;
        endNode = null;
        waypoints = new HashSet<>();
        routes = new ArrayList<>();
        painters = new ArrayList<>();
        painters.add(previewPainter);
    }
}