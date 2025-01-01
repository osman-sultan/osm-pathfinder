package map;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for drawing route on the map viewer
 */
public class RoutePainter implements Painter<JXMapViewer> {
    private Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.BLACK, Color.green, Color.ORANGE, Color.CYAN};
    private boolean antiAlias = true;

    private List<List<GeoPosition>> routes;

    /**
     * @param routes list of routes to paint
     */
    public RoutePainter(List<List<GeoPosition>> routes) {
        // copy the list so that changes in the original list do not have an effect here
        this.routes = new ArrayList<>(routes);
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
        g = (Graphics2D) g.create();

        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        if (antiAlias) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        drawRoute(g, map);
        g.dispose();
    }

    /**
     * @param g   the graphics object
     * @param map the map
     */
    private void drawRoute(Graphics2D g, JXMapViewer map) {

        for (int i = 0; i < routes.size(); i++) {
            List<GeoPosition> route = routes.get(i);
            if (route == null) {
                continue;
            }
            int lastX = 0;
            int lastY = 0;
            boolean first = true;

            // set diff lines for diff routes
            g.setStroke(new BasicStroke(7 - i));
            Color color = colors[i];
            g.setColor(color);

            for (GeoPosition gp : route) {
                // convert geo-coordinate to world bitmap pixel
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

                if (first) {
                    first = false;
                } else {
                    g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
                }

                lastX = (int) pt.getX();
                lastY = (int) pt.getY();
            }
        }
    }
}