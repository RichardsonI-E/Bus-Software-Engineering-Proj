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

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
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
/*This class holds the primary "card" or panel for the user to plan a route
using combo boxes to search for bus stations and an option to add up to 3 stops
*/
public class HomeScreen extends JPanel {

    private CardLayout cl;//save cardlayout from parent as variable
    private JPanel container;//save container from parent as variable
    private MapScreen mapScreen;//save map screen card as variable

    //declare summary panel in advance for methods to use
    private SummaryPanel summaryPanel = new SummaryPanel();

    private int stopCount = 0;//amount of stops in the users' route
    //the row in the form the stop components start from
    private int currentRow = 6;
    //list of stop components(label and combo box)
    private List<JComponent[]> stops = new ArrayList<>();

    //creates a small delay before the summary updates
    private Timer sTimer = new Timer(300, e -> updateSummary());

    //call form in advance for modification
    private JPanel form;

    //call departure and arrival station in advance
    private JComboBox<String> departureStation = new JComboBox<>();
    private JComboBox<String> arrivalStation = new JComboBox<>();

    //Font for the title of the form
    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    //constructor to initialize screen using given parameters from parent
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
        //get "Home" version of topTab
        topTab tTab = new topTab("Home", cl, container, this);
        JPanel content = createContentPanel();

        //use a split pane to change size of form/summary
        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                content,
                summaryPanel
        );
        split.setResizeWeight(0.6);

        //add tab and split pane to screen
        add(tTab, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
    }

    //create the main content of the screen (title and form)
    private JPanel createContentPanel() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        //set title of form
        JLabel title = new JLabel("Create a new Route");
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(subTitle);

        //call form panel method
        JPanel form = createFormPanel();

        //add title and form to content pane
        content.add(title);
        content.add(form);

        return content;
    }

    //method to create main form with labels and combo boxes
    private JPanel createFormPanel() {
        form = new JPanel(new GridBagLayout());
        GridBagConstraints f = createConstraints();

        //set style of form
        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setPreferredSize(new Dimension(500, 250));
        form.setOpaque(false);

        //create a combo box set for arrival and departure
        setupComboBox(departureStation);
        setupComboBox(arrivalStation);

        //create buttons to modify stops and submit
        JButton addStop = createButton("Add Stop", Color.GREEN);
        JButton removeStop = createButton("Remove Stop", Color.RED);
        JButton submit = createButton("Generate Route", Color.BLUE);

        //call methods to add components to main content and add respective listeners
        addFormComponents(form, f, addStop, removeStop, submit);
        setupListeners(form, addStop, removeStop, submit);

        //call update buttons while stop count is 0
        updateButtons(addStop, removeStop);

        return form;
    }

    //define the constraints of the form, aligned vertically
    private GridBagConstraints createConstraints() {
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);
        return f;
    }

    //set up comboboxes and enable stations to search from
    private void setupComboBox(JComboBox<String> combo) {
        combo.setEditable(true);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        initStations(combo);
        stationSearch(combo, StationManager.getStations());

        combo.addActionListener(e -> triggerUpdate());
        addDocumentListener(combo);
    }

    //create buttons with respective labels and colors
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBackground(color);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        return btn;
    }

    //add each form component in sequential order (top to bottom)
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
    //call timer to add delay to update method
    private void triggerUpdate() {
        if (sTimer.isRunning()) {
            sTimer.restart();
        } else {
            sTimer.start();
        }
    }

    //method to update the summary panel
    private void updateSummary() {
        //save stations as checkpoints in route
        List<Station> routePoints = new ArrayList<>();

        //get the chosen station in departure and arrival
        Station departure = getStation(departureStation);
        Station arrival = getStation(arrivalStation);

        //if either station is empty or not found, abort
        if (departure == null || arrival == null) {
            return;
        }

        //add departure as first point
        routePoints.add(departure);

        //set departure station as latest stop
        Station last = departure;

        //only add the next station to the list if it is not the same as previous
        for (JComponent[] stop : stops) {
            Station s = getStation((JComboBox<String>) stop[1]);

            if (s != null && !s.getName().equals(last.getName())) {
                routePoints.add(s);
                last = s;
            }
        }

        //if arrival is not the same as the latest station, add it as final stop
        if (!arrival.getName().equals(last.getName())) {
            routePoints.add(arrival);
        }

        //create a route planner with given stations
        RoutePlanner route = new RoutePlanner(routePoints);

        //if route is invalid, abort
        if (!route.validateRoute()) {
            return;
        }

        //update the summary panel class with given information
        summaryPanel.updateSummary(route);
    }

    //use Station manager to find station with given name selected in combo box
    private Station getStation(JComboBox<String> box) {
        return StationManager.getStationByName((String) box.getSelectedItem());
    }

    //update add/remove stop buttons' visibility based on the amount of stops
    private void updateButtons(JButton add, JButton remove) {
        remove.setVisible(stopCount > 0);
        add.setVisible(stopCount < 3);
    }

    // ================= LISTENERS =================
    //add appropriate listeners to the form and buttons
    private void setupListeners(JPanel form, JButton addStop, JButton removeStop, JButton submit) {

        //if there are no stops, don't add listener to removeStop button
        removeStop.addActionListener(e -> {
            if (stopCount == 0) {
                return;
            }

            //remove label and combobox for last stop in list
            JComponent[] lastStop = stops.remove(stops.size() - 1);
            form.remove(lastStop[0]);
            form.remove(lastStop[1]);

            //reduce stop count, then update form and buttons
            stopCount--;
            updateButtons(addStop, removeStop);
            triggerUpdate();

            form.revalidate();
            form.repaint();
        });

        //if the amount of stops is already maximum, ignore
        addStop.addActionListener((ActionEvent e) -> {
            if (stopCount >= 3) {
                return;
            }

            //add stop combo box with appropriate label (A, B, C)
            JLabel label = new JLabel("Stop " + (char) ('A' + stopCount) + ":");
            JComboBox<String> box = new JComboBox<>();

            //call combo box setup with new stop
            setupComboBox(box);

            //add all components to the form in order
            GridBagConstraints f = createConstraints();
            f.gridy = currentRow++;
            form.add(label, f);

            f.gridy = currentRow++;
            form.add(box, f);

            form.add(label, f);
            f.gridy++;
            form.add(box, f);

            //add label and combo box to list of stop components
            stops.add(new JComponent[]{label, box});
            
            //add to stop count then update form and buttons accordingly
            stopCount++;
            updateButtons(addStop, removeStop);
            triggerUpdate();

            form.revalidate();
            form.repaint();
        });

        //call separate method for submit button
        submit.addActionListener(e -> handleSubmit());
    }

    //provate method to handle submit button
    private void handleSubmit() {
        //save stations in route as list
        List<Station> routePoints = new ArrayList<>();

        Station departure = getStation(departureStation);
        Station arrival = getStation(arrivalStation);

        //show warning if departure or arrival are empty/invalid
        if (departure == null || arrival == null) {
            JOptionPane.showMessageDialog(null,
                    "Please select valid departure and arrival stations.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        //add first station
        routePoints.add(departure);

        //add each stop
        for (JComponent[] stop : stops) {
            Station s = getStation((JComboBox<String>) stop[1]);
            if (s != null) {
                routePoints.add(s);
            }
        }

        //add arrival station
        routePoints.add(arrival);

        //create route planner with stations
        RoutePlanner route = new RoutePlanner(routePoints);

        //if route is invalid, show error message to user
        if (!route.validateRoute()) {
            JOptionPane.showMessageDialog(null,
                    "This route is not possible with available buses/refuel stations.",
                    "Invalid Route",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        //add the route to the map screen and set map as active card
        mapScreen.setRoute(routePoints, route);
        cl.show(container, "map");
        clearForm();
    }

    // ================= UTILITIES =================

    //add each station to the list of possible entries (ignore refuel stations)
    private void initStations(JComboBox<String> box) {
        for (Station s : StationManager.getStations()) {
            if (!"refuel".equals(s.getType())) {
                box.addItem(s.getName());
            }
        }

        ((JTextField) box.getEditor().getEditorComponent()).setText("");
    }

    //update the form whenever the user makes any change to it
    private void addDocumentListener(JComboBox<String> combo) {
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                triggerUpdate();
            }

            public void removeUpdate(DocumentEvent e) {
                triggerUpdate();
            }

            public void changedUpdate(DocumentEvent e) {
                triggerUpdate();
            }
        });
    }

    //method to find a station in possible entries
    private void stationSearch(JComboBox<String> combo, List<Station> data) {
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        final boolean[] adjusting = {false};

        //add slight delay to search, if form is being adjusted, abort
        Timer searchTimer = new Timer(200, e -> {
            if (adjusting[0]) {
                return;
            }

            adjusting[0] = true;

            //save user input as text
            String input = editor.getText();

            // Save caret position
            int caret = editor.getCaretPosition();

            // Build filtered list
            List<String> filtered = new ArrayList<>();

            //if a station contains the user input, add as possible entry
            for (Station s : data) {
                if (!"refuel".equals(s.getType())
                        && s.getName().toLowerCase().contains(input.toLowerCase())) {
                    filtered.add(s.getName());
                }
            }

            //set model of combobox to new filtered entries
            combo.setModel(new javax.swing.DefaultComboBoxModel<>(
                    filtered.toArray(String[]::new)
            ));

            //set text in combobox to given station if it isn't already
            if (!editor.getText().equals(input)) {
                editor.setText(input);
            }

            // Only show popup if user is typing/focused on box
            if (editor.isFocusOwner() && !filtered.isEmpty()) {
                combo.setPopupVisible(true);
            }

            //allow form to be adjusted again
            adjusting[0] = false;
        });

        //ensure search timer occurs once
        searchTimer.setRepeats(false);

        //restart search if user modifies form
        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            public void removeUpdate(DocumentEvent e) {
                searchTimer.restart();
            }

            public void changedUpdate(DocumentEvent e) {
                searchTimer.restart();
            }
        });
    }

    //method to completely clear the form of entries
    private void clearForm() {
        ((JTextField) departureStation.getEditor().getEditorComponent()).setText("");
        ((JTextField) arrivalStation.getEditor().getEditorComponent()).setText("");

        // Remove stop components from UI
        for (JComponent[] stop : stops) {
            form.remove(stop[0]);
            form.remove(stop[1]);
        }

        stopCount = 0;
        stops.clear();

        revalidate();
        repaint();
    }
}
