package components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import primary.RoutePlanner;
import primary.Station;

public class SummaryPanel extends JPanel {

    // ---------- Fields ----------

    //label to hold the route's estimated time of arrival
    private JLabel etaLabel;
    //label to hold the route's chosen bus
    private JLabel busLabel;
    //label to hold the route's stops
    private JPanel centerPanel;

    //font for each label
    private final Font subtitle = new Font("Arial", Font.BOLD, 18);

    // ---------- Constructor ----------

    //default constructor, when the route is not yet defined
    public SummaryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 100));

        etaLabel = new JLabel("ETA: --");
        busLabel = new JLabel("Your Bus: --");

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        add(createTopPanel(), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    // ---------- Public Methods ----------
    // call the update methods when route is modified/submitted
    public void updateSummary(List<Station> route, RoutePlanner planner) {
        updateTopInfo(planner);
        updateRouteDisplay(route);

        revalidate();
        repaint();
    }

    // ---------- UI Construction ----------
    //Method to make the eta and chosen bus and set its layout
    private JPanel createTopPanel() {
        JPanel top = new JPanel(new GridLayout(2, 1));

        top.setAlignmentX(CENTER_ALIGNMENT);

        etaLabel.setAlignmentX(CENTER_ALIGNMENT);
        etaLabel.setFont(subtitle);
        top.add(etaLabel);

        busLabel.setAlignmentX(CENTER_ALIGNMENT);
        busLabel.setFont(subtitle);
        top.add(busLabel);

        return top;
    }

    // ---------- Update Methods ----------
    /*Update the eta in terms of hours and minutes and the bus'
    make and mode*/
    private void updateTopInfo(RoutePlanner planner) {
        int totalMinutes = (int) (planner.getETA() * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        etaLabel.setText("ETA: " + formatTime(hours, minutes));

        busLabel.setText(
            "Your Bus: "
            + planner.getChosenBus().getMake() + " "
            + planner.getChosenBus().getModel()
        );
    }

    //update the stops from top to bottom
    private void updateRouteDisplay(List<Station> route) {
        centerPanel.removeAll();

        for (int i = 0; i < route.size(); i++) {
            Station station = route.get(i);

            centerPanel.add(
                new JLabel(formatStationLabel(station, i, route.size()))
            );

            if (i < route.size() - 1) {
                centerPanel.add(new JLabel("↓"));
            }
        }
    }

    // ---------- Formatting Helpers ----------
    //set the formatting to properly label the stations and eta
    private String formatStationLabel(Station station, int index, int size) {
        String prefix;

        if (index == 0) {
            prefix = "Start: ";
        } else if (index == size - 1) {
            prefix = "End: ";
        } else {
            prefix = "Stop: ";
        }

        return prefix + station.getName() + " (" + station.getType() + ")";
    }

    private String formatTime(int hours, int minutes) {
        return hours + " hour(s) " + minutes + " minute(s)";
    }
}