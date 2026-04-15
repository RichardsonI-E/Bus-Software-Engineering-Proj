package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import primary.RoutePlanner;
import primary.Station;

public class SummaryPanel extends JPanel {

    private JLabel etaLabel;
    private JLabel busLabel;
    private JPanel centerPanel;

    public SummaryPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        etaLabel = new JLabel("ETA: --");
        busLabel = new JLabel("Your Bus: --");

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        add(createTopPanel(), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateSummary(List<Station> route, RoutePlanner planner) {
        updateTopInfo(planner);
        updateRouteDisplay(route);

        revalidate();
        repaint();
    }

    // ---------- Helper Methods ----------

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(etaLabel);
        top.add(busLabel);
        return top;
    }

    private void updateTopInfo(RoutePlanner planner) {
        int totalMinutes = (int) (planner.getETA() * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        etaLabel.setText("ETA: " + formatTime(hours, minutes));

        busLabel.setText("Your Bus: "
                + planner.getChosenBus().getMake() + " "
                + planner.getChosenBus().getModel());
    }

    private void updateRouteDisplay(List<Station> route) {
        centerPanel.removeAll();

        for (int i = 0; i < route.size(); i++) {
            Station s = route.get(i);

            centerPanel.add(new JLabel(formatStationLabel(s, i, route.size())));

            if (i < route.size() - 1) {
                centerPanel.add(new JLabel("↓"));
            }
        }
    }

    private String formatStationLabel(Station s, int index, int size) {
        String prefix;

        if (index == 0) {
            prefix = "Start: ";
        } else if (index == size - 1) {
            prefix = "End: ";
        } else {
            prefix = "Stop: ";
        }

        return prefix + s.getName() + " (" + s.getType() + ")";
    }

    private String formatTime(int hours, int minutes) {
        return hours + " hour(s) " + minutes + " minute(s)";
    }
}