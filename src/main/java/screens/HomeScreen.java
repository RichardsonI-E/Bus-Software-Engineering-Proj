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
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import components.MapPanel;
import components.topTab;
import permissions.StationManager;
import primary.RoutePlanner;
import primary.Station;

public class HomeScreen extends JPanel {

    //create placeholder objects for the parent's cardlayout and container
    private CardLayout cl;
    private JPanel container;

    //Used to track the amount of Stops currently on the homepage screen
    private int stopCount = 0;
    java.util.List<JComponent[]> stops = new java.util.ArrayList<>();

    //Universal font to be used for Hamburger menu
    Font subTitle = new Font("Arial", Font.BOLD, 20);

    //create form components in advance
    JComboBox<String> departureStation = new JComboBox<>();
    JComboBox<String> arrivalStation = new JComboBox<>();

    //function that is set to call after add/remove button is pressed
    private void updateButtons(JButton addStop, JButton removeStop) {
        removeStop.setVisible(stopCount > 0);//if amount of stops is 0, it is not visible
        addStop.setVisible(stopCount < 3);//if amount of stops is at least 3, it is not visible
    }

    private void initStations(JComboBox<String> x) {
        for (Station s : StationManager.getStations()) {
            if ("refuel".equals(s.getType())) {
                continue;
            }
            x.addItem(s.getName());
        }
        JTextField y = (JTextField) x.getEditor().getEditorComponent();
        y.setText("");
    }

    private void filter(JComboBox<String> combo, JTextField editor,
            List<Station> data, boolean[] adjusting) {

        if (adjusting[0] || !editor.isFocusOwner()) {
            return;
        }

        adjusting[0] = true;

        String input = editor.getText();

        combo.setPopupVisible(false);
        combo.removeAllItems();

        for (Station s : data) {
            if (s.getName().toLowerCase().contains(input.toLowerCase())) {
                if ("refuel".equals(s.getType())) {
                    continue;
                }
                combo.addItem(s.getName());
            }
        }

        if (!editor.getText().equals(input)) {
            editor.setText(input);
            editor.setCaretPosition(input.length());
        }

        if (combo.getItemCount() > 0 && editor.isFocusOwner()) {
            combo.setPopupVisible(true);
        }
        adjusting[0] = false;
    }

    //function to allow searching for stations based on combobox content
    private void stationSearch(JComboBox<String> combo, List<Station> data) {
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        final boolean[] adjusting = {false};

        Timer searchTime = new Timer(200, e -> filter(
                combo, editor, data, adjusting));

        searchTime.setRepeats(false);

        editor.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                restart();
            }

            public void removeUpdate(DocumentEvent e) {
                restart();
            }

            public void changedUpdate(DocumentEvent e) {
                restart();
            }

            private void restart() {
                if (searchTime.isRunning()) {
                    searchTime.restart();
                } else {
                    searchTime.start();
                }
            }
        });
    }

    private void clearForm(){
        JTextField a = (JTextField) departureStation.getEditor().getEditorComponent();
        a.setText("");

        JTextField b = (JTextField) arrivalStation.getEditor().getEditorComponent();
        b.setText("");
    }

    public HomeScreen(JFrame parent, CardLayout cl, JPanel container) {
        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Home", cl, container, this);

        //add placeholder for the history/summary panel to the bottom of page
        JPanel history = new JPanel();
        history.setBackground(Color.yellow);
        history.setAlignmentX(Component.CENTER_ALIGNMENT);
        history.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));

        //"Work in Progress" message for history panel
        JLabel wipMsg = new JLabel("Recent Routes/Summary of current route will go here");
        wipMsg.setFont(new Font("Arial", Font.BOLD, 20));
        history.add(wipMsg);

        //create the main center panel
        JPanel content = new JPanel();

        //title for the main content panel
        JLabel title = new JLabel("Create a new Route");
        title.setAlignmentX(CENTER_ALIGNMENT);
        title.setFont(subTitle);

        //add main form to the content panel, setting layout to gridbag
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);

        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setPreferredSize(new Dimension(500, 250));
        form.setOpaque(false);

        //Entry for departure station, a combobox that will be used to search the database
        JLabel departure = new JLabel("Departure Station:");
        departure.setAlignmentY(Component.CENTER_ALIGNMENT);
        departureStation.setEditable(true);
        departureStation.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        initStations(departureStation);
        stationSearch(departureStation, StationManager.getStations());

        //button to add a stop to the user's route
        JButton addStop = new JButton("Add Stop");
        addStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        addStop.setBackground(Color.GREEN);
        addStop.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //button to remove a stop from the user's route
        JButton removeStop = new JButton("Remove Stop");
        removeStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeStop.setBackground(Color.RED);
        removeStop.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //Entry for arrival station, a combobox that will be used to search the database
        JLabel arrival = new JLabel("Arrival Station:");
        arrival.setAlignmentY(Component.CENTER_ALIGNMENT);
        arrivalStation.setEditable(true);
        arrivalStation.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        initStations(arrivalStation);
        stationSearch(arrivalStation, StationManager.getStations());

        //button to submit the user's route
        JButton submit = new JButton("Generate Route");
        submit.setAlignmentX(Component.CENTER_ALIGNMENT);
        submit.setBackground(Color.BLUE);
        submit.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        MapPanel map = new MapPanel();
        map.setPreferredSize(new Dimension(400, 300));

        content.add(map);

        //listener for the remove stop button
        removeStop.addActionListener(e -> {
            if (stopCount == 0) {
                return; //abort if there are no stops
            }
            JComponent[] lastStop = stops.remove(stops.size() - 1);//remove the last entry in the stops list

            form.remove(lastStop[0]); // remove label
            form.remove(lastStop[1]); // remove combo box

            stopCount--;//stop count decreases by 1

            updateButtons(addStop, removeStop);//call update buttons

            //recreate the page with updated layout afterwards
            form.revalidate();
            form.repaint();
        });

        //listener for add stop button
        addStop.addActionListener((ActionEvent e) -> {
            if (stopCount >= 3) {
                return; //if there are 3+ stops, abort
            }                //create a new label and (editable)combobox (label determined by stop count)
            JLabel stopLabel = new JLabel("Stop " + (char) ('A' + stopCount) + ":");
            JComboBox<String> stopBox = new JComboBox<>();
            stopBox.setEditable(true);
            initStations(stopBox);
            stationSearch(stopBox, StationManager.getStations());
            //place label and combobox at the next row in the gridbag layout
            f.gridy++;
            form.add(stopLabel, f);
            f.gridy++;
            form.add(stopBox, f);

            //add stop to the list and increase stop count
            stops.add(new JComponent[]{stopLabel, stopBox});
            stopCount++;

            updateButtons(addStop, removeStop);//call update buttons

            //recreate the page with updated layout afterwards
            form.revalidate();
            form.repaint();
        });

        //listener for submit button:
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Station> routePoints = new ArrayList<>();

                Station departure = StationManager.getStationByName(
                        (String) departureStation.getSelectedItem());
                Station arrival = StationManager.getStationByName(
                        (String) arrivalStation.getSelectedItem());
                
                routePoints.add(departure);

                for (JComponent[] stop : stops) {
                    JComboBox<String> box = (JComboBox<String>) stop[1];
                    String x = (String) box.getSelectedItem();
                    routePoints.add(StationManager.getStationByName(x));
                }

                routePoints.add(arrival);

                RoutePlanner route = new RoutePlanner(routePoints);

                if(route.testRoute()){
                }else if(route.reroute()){
                }
                clearForm();
            }
        });

        //add all elements to the form
        form.add(departure, f);

        f.gridy = 1;
        form.add(departureStation, f);

        f.gridy = 2;
        form.add(arrival, f);

        f.gridy = 3;
        form.add(arrivalStation, f);

        f.gridy = 4;
        form.add(addStop, f);

        f.gridy = 5;
        form.add(removeStop, f);

        f.gridy = 9;
        form.add(submit, f);

        //set the layout of main content panel and add the title and form to it
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(title);
        content.add(form);

        //set size bottom tab
        history.setPreferredSize(new Dimension(0, 100));

        //add 3 panels to the homescreen
        add(tTab, BorderLayout.NORTH);
        add(history, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }
}
