package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import primary.RouteLeg;
import primary.RoutePlanner;
import primary.Station;

public class SummaryPanel extends JPanel {

    // ---------- Fields ----------

    // label to hold the route's estimated time of arrival
    private JLabel etaLabel;
    // label to hold the route's chosen bus
    private JLabel busLabel;
    // label to hold the route's stops
    private JPanel centerPanel;

    // font for each label
    private final Font subtitle = new Font("Arial", Font.BOLD, 18);

    // ---------- Constructor ----------

    // default constructor, when the route is not yet defined
    public SummaryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 100));

        etaLabel = new JLabel("ETA: --");
        busLabel = new JLabel("Your Bus: --");

        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(8));

        JScrollPane scroll = new JScrollPane(centerPanel);

        add(createTopPanel(), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    // ---------- Public Methods ----------
    // call the update methods when route is modified/submitted
    public void updateSummary(RoutePlanner planner) {
        updateTopInfo(planner);
        updateRouteDisplay(planner);

        revalidate();
        repaint();
    }

    // ---------- UI Construction ----------

    //create panels to display the connecting stations
    private JPanel createStopPanel(Station station, String prefix) {
        JPanel panel = new JPanel(new BorderLayout());

        //add label with prefix, name and coordinates
        JLabel label = new JLabel(
                prefix + station.getName() + " (" + station.getType() + ")"  + " ["
                + station.getLatitude() + ", " + station.getLongitude() + "]");
        label.setHorizontalAlignment(JLabel.CENTER);

        panel.add(label, BorderLayout.CENTER);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        return panel;
    }

    //create panels between each stop displaying the distance and heading
    private JPanel createConnectionPanel(RouteLeg leg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel arrow = new JLabel("↓");
        arrow.setAlignmentX(CENTER_ALIGNMENT);

        JLabel info = new JLabel(
                String.format("%.2f miles • %s",
                        leg.getDistance(),
                        leg.getHeading()));
        info.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(arrow);
        panel.add(info);

        return panel;
    }

    // make top panel and add eta and chosen bus to it
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

    //update top panel with current eta and bus
    private void updateTopInfo(RoutePlanner planner) {
        int totalMinutes = (int) (planner.getETA() * 60);
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;

        etaLabel.setText("ETA: " + formatTime(hours, minutes));

        busLabel.setText(
                "Your Bus: "
                        + planner.getChosenBus().getMake() + " "
                        + planner.getChosenBus().getModel());
    }

    // update the stops from top to bottom
    private void updateRouteDisplay(RoutePlanner planner) {
        centerPanel.removeAll();
        centerPanel.add(Box.createVerticalStrut(8));

        List<RouteLeg> legs = planner.getRoute();

        if (legs.isEmpty())
            return;

        // --- Add FIRST station (start) ---
        Station first = legs.get(0).getStart();
        centerPanel.add(createStopPanel(first, "Start: "));
        centerPanel.add(Box.createVerticalStrut(6));

        // --- add each stop ---
        for (int i = 0; i < legs.size(); i++) {
            RouteLeg leg = legs.get(i);

            // Add connection info (distance and heading)
            centerPanel.add(createConnectionPanel(leg));
            centerPanel.add(Box.createVerticalStrut(6));

            // Add destination station
            String prefix = (i == legs.size() - 1) ? "End: " : "Stop: ";
            centerPanel.add(createStopPanel(leg.getEnd(), prefix));
            centerPanel.add(Box.createVerticalStrut(6));
        }
    }

    // ---------- Formatting Helper ----------
    // interpret time as hours and minutes
    private String formatTime(int hours, int minutes) {
        return hours + " hour(s) " + minutes + " minute(s)";
    }
}