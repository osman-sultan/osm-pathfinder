package map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Paints a selection rectangle
 *
 * @author Martin Steiger
 */
public class PreviewPainter implements Painter<JXMapViewer> {
    /**
     * this painter paints the highway node closest to the cursor at all times
     */
    public WayPointAdapter adapter;

    public PreviewPainter() {
    }

    public void setAdapter(WayPointAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        MapNode closestNode = adapter.getClosestNode();
        if (closestNode != null) {
            GeoPosition geo = new GeoPosition(closestNode.latitude, closestNode.longitude);
//            System.out.println(String.format("Latitude %f Longitude %f",closestNode.latitude,closestNode.longitude));

            Point2D pt = map.getTileFactory().geoToPixel(geo, map.getZoom());
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.fillOval((int) pt.getX() - 5, (int) pt.getY() - 5, 10, 10);
        }
    }
}
