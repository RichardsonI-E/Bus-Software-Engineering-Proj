package screens;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

import components.FilterString;
import components.topTab;
import permissions.StationManager;
import primary.Station;
import primary.Station.BusStation;
import primary.Station.RefuelStation;
/*
This screen allows station managers and admins to add, modify or delete buses
in the database. The radio buttons determine the stations' type and,
consequentially, the subclass that the bus is saved under. The checkboxes
determine the fuel types that the station supports (assuming it is a refuel
station)
*/
public class SManageScreen extends JPanel {
    //get the map, layout and container from parent
    private MapScreen mapScreen;
    private CardLayout cl;
    private JPanel container;

    //variable to hold the station that is selected
    private Station selected = null;

    //text fields (formatted) for station information
    private JTextField name = new JTextField();
    private JFormattedTextField latitude;
    private JFormattedTextField longitude;

    //radio buttons and check boxes to choose stations type and fuels stored
    private JRadioButton busType = new JRadioButton("Bus");
    private JRadioButton refuelType = new JRadioButton("Refuel", true);
    private JCheckBox unleaded = new JCheckBox("Unleaded", true);
    private JCheckBox diesel = new JCheckBox("Diesel", true);

    //panel to hold the fuel type checkboxes
    private JPanel fuelHolder = new JPanel();

    //model for the table of stations
    private DefaultTableModel model;

    //format to only take numbers in the given textfield
    private NumberFormat num = NumberFormat.getNumberInstance();

    // ===================== CONSTRUCTOR =====================
    public SManageScreen(JFrame parent, CardLayout cl, JPanel container, MapScreen mapScreen) {
        this.mapScreen = mapScreen;
        this.cl = cl;
        this.container = container;

        //set lat and long as number format
        latitude = new JFormattedTextField(num);
        longitude = new JFormattedTextField(num);

        //filter symbols/numbers out of string
        ((AbstractDocument) name.getDocument()).setDocumentFilter(new FilterString());

        setLayout(new BorderLayout());

        //add station screen version of top tab
        add(new topTab("Manage Stations", cl, container, this), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    //create main panel, vertically split for station table and form
    private JSplitPane createMainPanel() {
        JPanel form = createFormPanel();
        JScrollPane table = createTable();

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(form), table);
        split.setResizeWeight(0.6);

        return split;
    }

    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 60, 20, 60));

        GridBagConstraints f = createGBC();

        //create fields with labels
        addField(form, f, new JLabel("Name:"), name);
        addField(form, f, new JLabel("Latitude (24.5–71.4):"), latitude);
        addField(form, f, new JLabel("Longitude (-168 to -52):"), longitude);

        //add the buttons and fields to form
        form.add(createStationTypePanel(), nextRow(f));
        form.add(createFuelPanel(), nextRow(f));
        form.add(createUpdateButton(), nextRow(f));
        form.add(createDeleteButton(), nextRow(f));

        return form;
    }

    private JScrollPane createTable() {
        //create table with station attributes as column labels
        String[] col = { "ID", "Name", "Latitude", "Longitude", "Type", "Fuel" };

        //prevent cells from being edited
        model = new DefaultTableModel(col, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        //initialize table and add selected listener
        JTable table = new JTable(model);
        initTable();
        addTableListener(table);

        return new JScrollPane(table);
    }

    //create panel to hold station type radio buttons
    private JPanel createStationTypePanel() {
        ButtonGroup group = new ButtonGroup();
        group.add(busType);
        group.add(refuelType);

        JPanel panel = new JPanel();
        panel.add(busType);
        panel.add(refuelType);

        //add action listeners to both radio buttons
        ActionListener listener = e -> fuelHolder.setVisible(refuelType.isSelected());
        busType.addActionListener(listener);
        refuelType.addActionListener(listener);

        return panel;
    }

    //add fuel checkboxes to panel
    private JPanel createFuelPanel() {
        fuelHolder.add(unleaded);
        fuelHolder.add(diesel);
        return fuelHolder;
    }

    //create the update button for the form
    private JButton createUpdateButton() {
        JButton btn = new JButton("Add / Update");
        btn.setBackground(Color.BLUE);
        btn.addActionListener(e -> handleUpdate());
        return btn;
    }

    //create the delete button for the form
    private JButton createDeleteButton() {
        JButton btn = new JButton("Delete");
        btn.setBackground(Color.RED);
        btn.addActionListener(e -> handleDelete());
        return btn;
    }

    private void initTable() {
        //clear table rows before initialization
        model.setRowCount(0);

        //if given station is bus, ignore fuel type field
        for (Station s : StationManager.getStations()) {
            if (s instanceof BusStation) {
                model.addRow(new Object[] {
                        s.getID(), s.getName(), s.getLatitude(), s.getLongitude(), s.getType(), ""
                });
        //if station is refuel, add supported fuels
            } else if (s instanceof RefuelStation r) {
                model.addRow(new Object[] {
                        s.getID(), s.getName(), s.getLatitude(), s.getLongitude(),
                        s.getType(), String.join(", ", r.getFuelType())
                });
            }
        }
    }

    //listener to get selected stations
    private void addTableListener(JTable table) {
        table.getSelectionModel().addListSelectionListener(e -> {
            //abort if table is being adjusted
            if (e.getValueIsAdjusting()) return;

            //get the row the user selected
            int row = table.getSelectedRow();
            if (row == -1) return;

            selected = StationManager.getStationByID((int) model.getValueAt(row, 0));

            //set the fields to respective values from station
            name.setText(model.getValueAt(row, 1).toString());
            latitude.setText(model.getValueAt(row, 2).toString());
            longitude.setText(model.getValueAt(row, 3).toString());

            //select appropriate type for the station and hide fuels if bus station
            boolean isBus = model.getValueAt(row, 4).equals("bus");
            busType.setSelected(isBus);
            refuelType.setSelected(!isBus);
            fuelHolder.setVisible(!isBus);

            //if refuel station, convert supported fuels to string to select checkboxes
            if (!isBus) setFuelSelection(model.getValueAt(row, 5).toString());
        });
    }

    //method to get the supported fuels from a station
    private void setFuelSelection(String fuelStr) {
        //set both to false in advance
        unleaded.setSelected(false);
        diesel.setSelected(false);

        //select each check if it matches contents of fueltype string
        for (String f : fuelStr.replace(",", "").split(" ")) {
            if (f.equalsIgnoreCase("unleaded")) unleaded.setSelected(true);
            if (f.equalsIgnoreCase("diesel")) diesel.setSelected(true);
        }
    }

    private void handleUpdate() {
        try {
            //get values in lat and long fields as float
            float lat = Float.parseFloat(latitude.getText());
            float lon = Float.parseFloat(longitude.getText());

            //check if coordinates are within North America
            if (!verifyCoords(lat, lon)) {
                showError("Invalid coordinates");
                return;
            }

            //create a new station with given coordinates
            Station s = createStation(lat, lon);

            //if a station is selected update with new information
            if (selected != null) {
                s.setID(selected.getID());
                StationManager.updateStation(s);
            //otherwise, add as a new station
            } else {
                StationManager.addStation(s);
            }

            //reload the form and table
            refresh();
        } catch (Exception ex) {
            showError("Invalid input");
        }
    }

    private void handleDelete() {
        //if no station is selected, abort
        if (selected == null) return;

        //show confirmation dialog to delete station
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this station?", "Confirm",
                JOptionPane.YES_NO_OPTION);

        //if confirmed, remove station and reload the form and table
        if (confirm == JOptionPane.YES_OPTION) {
            StationManager.deleteStation(selected);
            refresh();
        }
    }

    //create a station with given coordinates
    private Station createStation(float lat, float lon) {
        //if station is a bus station: add as bus station subclass
        if (busType.isSelected()) {
            return new BusStation(name.getText(), lat, lon);
        }

        //add fuels to the supported fuels if checkbox is selected
        ArrayList<String> fuels = new ArrayList<>();
        if (diesel.isSelected()) fuels.add("diesel");
        if (unleaded.isSelected()) fuels.add("unleaded");

        //create refuel station with name, coords, and supported fuels
        return new RefuelStation(name.getText(), lat, lon, fuels);
    }

    //method to reload the page
    private void refresh() {
        //no station selected
        selected = null;
        //remove values from form fields
        clearForm();
        //restart table
        initTable();
        //update map with new station info
        mapScreen.refreshStations();
    }

    //remove values from form fields
    private void clearForm() {
        name.setText("");
        latitude.setText("");
        longitude.setText("");
    }

    //check if the coodinates are within North America
    private boolean verifyCoords(float lat, float lon) {
        return lat >= 24.5 && lat <= 71.4 && lon >= -168 && lon <= -52;
    }

    //show error message when unexpected error occurs
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }

    //constrain form
    private GridBagConstraints createGBC() {
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);
        return f;
    }

    //get the next available row in form
    private GridBagConstraints nextRow(GridBagConstraints f) {
        f.gridy++;
        return f;
    }

    //add a new field to the form (with appropriate label)
    private void addField(JPanel panel, GridBagConstraints f, JLabel label, JComponent field) {
        panel.add(label, f);
        panel.add(field, nextRow(f));
    }
}