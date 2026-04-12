package components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import primary.Station;


public class MapPanel extends JPanel {

    List<Station> stationPoints = new ArrayList<>();

    public void setRoute(List<Station> points) {
        this.stationPoints = points;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (stationPoints.size() < 2) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g;

        // Draw lines between stations
        for (int i = 0; i < stationPoints.size() - 1; i++) {
            Station p1 = stationPoints.get(i);
            Station p2 = stationPoints.get(i + 1);

            g2.drawLine((int) p1.getLatitude(), (int) p1.getLongitude(), (int) p2.getLatitude(), (int) p2.getLongitude());
        }

        // Draw stations
        for (Station p : stationPoints) {
            g2.fillOval((int) (p.getLatitude() - 5), (int) (p.getLongitude() - 5), 10, 10);
        }
    }
}
