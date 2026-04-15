package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import components.SummaryPanel;
import components.topTab;
import permissions.StationManager;
import primary.RoutePlanner;
import primary.Station;

public class HomeScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;
    private MapScreen mapScreen;

    private SummaryPanel summaryPanel = new SummaryPanel();

    private int stopCount = 0;
    private List<JComponent[]> stops = new ArrayList<>();

    private Timer sTimer = new Timer(300, e -> updateSummary());

    private JComboBox<String> departureStation = new JComboBox<>();
    private JComboBox<String> arrivalStation = new JComboBox<>();

    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    public HomeScreen(JFrame parent, CardLayout cl, JPanel container, MapScreen mapScreen) {
        this.cl = cl;
        this.container = container;
        this.mapScreen = mapScreen;

        setLayout(new BorderLayout());
        sTimer.setRepeats(false);

        initUI();
    }

    // ================= UI SETUP =================

    private void initUI() {
        topTab tTab = new topTab("Home", cl, container, this);

        summaryPanel.setBackground(Color.YELLOW);
        summaryPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        summaryPanel.setPreferredSize(new Dimension(0, 100));

        JPanel content = createContentPanel();

        add(tTab, BorderLayout.NORTH);
        add(summaryPanel, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create a new Route");
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(subTitle);

        JPanel form = createFormPanel();

        content.add(title);
        content.add(form);

        return content;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints f = createConstraints();

        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setPreferredSize(new Dimension(500, 250));
        form.setOpaque(false);

        setupComboBox(departureStation);
        setupComboBox(arrivalStation);

        JButton addStop = createButton("Add Stop", Color.GREEN);
        JButton removeStop = createButton("Remove Stop", Color.RED);
        JButton submit = createButton("Generate Route", Color.BLUE);

        addFormComponents(form, f, addStop, removeStop, submit);
        setupListeners(form, addStop, removeStop, submit);

        return form;
    }

    private GridBagConstraints createConstraints() {
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);
        return f;
    }

    private void setupComboBox(JComboBox<String> combo) {
        combo.setEditable(true);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        initStations(combo);
        stationSearch(combo, StationManager.getStations());

        combo.addActionListener(e -> triggerUpdate());
        addDocumentListener(combo);
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(color);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        return btn;
    }

    private void addFormComponents(JPanel form, GridBagConstraints f,
                                   JButton addStop, JButton removeStop, JButton submit) {

        form.add(new JLabel("Departure Station:"), f);
        f.gridy++;
        form.add(departureStation, f);

        f.gridy++;
        form.add(new JLabel("Arrival Station:"), f);
        f.gridy++;
        form.add(arrivalStation, f);

        f.gridy++;
        form.add(addStop, f);

        f.gridy++;
        form.add(removeStop, f);

        f.gridy += 4;
        form.add(submit, f);
    }

    // ================= LOGIC =================

    private void triggerUpdate() {
        if (sTimer.isRunning()) {
            sTimer.restart();
        } else {
            sTimer.start();
        }
    }

    private void updateSummary() {
        List<Station> routePoints = new ArrayList<>();

        Station departure = getStation(departureStation);
        Station arrival = getStation(arrivalStation);

        if (departure == null || arrival == null) return;

        routePoints.add(departure);

        Station last = departure;

        for (JComponent[] stop : stops) {
            Station s = getStation((JComboBox<String>) stop[1]);

            if (s != null && !s.getName().equals(last.getName())) {
                routePoints.add(s);
                last = s;
            }
        }

        if (!arrival.getName().equals(last.getName())) {
            routePoints.add(arrival);
        }

        RoutePlanner route = new RoutePlanner(routePoints);

        if (!route.validateRoute()) return;

        summaryPanel.updateSummary(routePoints, route);
    }

    private Station getStation(JComboBox<String> box) {
        return StationManager.getStationByName((String) box.getSelectedItem());
    }

    private void updateButtons(JButton add, JButton remove) {
        remove.setVisible(stopCount > 0);
        add.setVisible(stopCount < 3);
    }

    // ================= LISTENERS =================

    private void setupListeners(JPanel form, JButton addStop, JButton removeStop, JButton submit) {

        removeStop.addActionListener(e -> {
            if (stopCount == 0) return;

            JComponent[] lastStop = stops.remove(stops.size() - 1);
            form.remove(lastStop[0]);
            form.remove(lastStop[1]);

            stopCount--;
            updateButtons(addStop, removeStop);
            triggerUpdate();

            form.revalidate();
            form.repaint();
        });

        addStop.addActionListener((ActionEvent e) -> {
            if (stopCount >= 3) return;

            JLabel label = new JLabel("Stop " + (char) ('A' + stopCount) + ":");
            JComboBox<String> box = new JComboBox<>();

            setupComboBox(box);

            GridBagConstraints f = createConstraints();
            f.gridy = form.getComponentCount();

            form.add(label, f);
            f.gridy++;
            form.add(box, f);

            stops.add(new JComponent[]{label, box});
            stopCount++;

            updateButtons(addStop, removeStop);
            triggerUpdate();

            form.revalidate();
            form.repaint();
        });

        submit.addActionListener(e -> handleSubmit());
    }

    private void handleSubmit() {
        List<Station> routePoints = new ArrayList<>();

        Station departure = getStation(departureStation);
        Station arrival = getStation(arrivalStation);

        if (departure == null || arrival == null) {
            JOptionPane.showMessageDialog(null,
                    "Please select valid departure and arrival stations.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        routePoints.add(departure);

        for (JComponent[] stop : stops) {
            Station s = getStation((JComboBox<String>) stop[1]);
            if (s != null) routePoints.add(s);
        }

        routePoints.add(arrival);

        RoutePlanner route = new RoutePlanner(routePoints);

        if (!route.validateRoute()) {
            JOptionPane.showMessageDialog(null,
                    "This route is not possible with available buses/refuel stations.",
                    "Invalid Route",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        mapScreen.setRoute(routePoints);
        cl.show(container, "map");
        clearForm();
    }

    // ================= UTIL =================

    private void initStations(JComboBox<String> box) {
        for (Station s : StationManager.getStations()) {
            if (!"refuel".equals(s.getType())) {
                box.addItem(s.getName());
            }
        }

        ((JTextField) box.getEditor().getEditorComponent()).setText("");
    }

    private void addDocumentListener(JComboBox<String> combo) {
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { triggerUpdate(); }
            public void removeUpdate(DocumentEvent e) { triggerUpdate(); }
            public void changedUpdate(DocumentEvent e) { triggerUpdate(); }
        });
    }

    private void stationSearch(JComboBox<String> combo, List<Station> data) {
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        final boolean[] adjusting = {false};

        Timer searchTimer = new Timer(200, e -> {
            if (adjusting[0] || !editor.isFocusOwner()) return;

            adjusting[0] = true;

            String input = editor.getText();

            combo.setPopupVisible(false);
            combo.removeAllItems();

            for (Station s : data) {
                if (!"refuel".equals(s.getType()) &&
                    s.getName().toLowerCase().contains(input.toLowerCase())) {
                    combo.addItem(s.getName());
                }
            }

            editor.setText(input);
            editor.setCaretPosition(input.length());

            if (combo.getItemCount() > 0) {
                combo.setPopupVisible(true);
            }

            adjusting[0] = false;
        });

        searchTimer.setRepeats(false);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { searchTimer.restart(); }
            public void removeUpdate(DocumentEvent e) { searchTimer.restart(); }
            public void changedUpdate(DocumentEvent e) { searchTimer.restart(); }
        });
    }

    private void clearForm() {
        ((JTextField) departureStation.getEditor().getEditorComponent()).setText("");
        ((JTextField) arrivalStation.getEditor().getEditorComponent()).setText("");

        stopCount = 0;
        stops.clear();

        revalidate();
        repaint();
    }
}