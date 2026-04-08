package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import components.topTab;
import permissions.StationManager;
import primary.Station;
import primary.Station.BusStation;
import primary.Station.RefuelStation;

// 24.5°N to 71.4°N latitude and 66.9°W to 172.5°E longitude

public class SManageScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;

    JPanel fuelHolder = new JPanel();
    int rowAdd = 0;

    NumberFormat num = NumberFormat.getNumberInstance();

    //add fields in advance
    JTextField name = new JTextField();
    JFormattedTextField longitude = new JFormattedTextField(num);
    JFormattedTextField latitude = new JFormattedTextField(num);

    ButtonGroup sType = new ButtonGroup();
    JRadioButton busType = new JRadioButton("Bus");
    JRadioButton refuelType = new JRadioButton("Refuel", true);

    JCheckBox unleaded = new JCheckBox("Unleaded", true);
    JCheckBox diesel = new JCheckBox("Diesel", true);

    //add a selected station for manager to update or delete
    Station selected = null;

    private void initTable(DefaultTableModel model) {
        model.setRowCount(0); // clear table

        for (Station s : StationManager.getStations()) {
            if (s instanceof Station.BusStation) {
                model.addRow(new Object[]{
                    s.getName(),
                    s.getLatitude(),
                    s.getLongitude(),
                    s.getType()
                });
            } else if (s instanceof Station.RefuelStation r) {
                StringBuilder fuelTypes = new StringBuilder();
                for (int x = 0; x < r.getFuelType().size(); x++) {
                    fuelTypes.append(r.getFuelType().get(x));
                    if(x != r.getFuelType().size() - 1){
                        fuelTypes.append(", ");
                    }
                }
                model.addRow(new Object[]{
                    s.getName(),
                    s.getLatitude(),
                    s.getLongitude(),
                    s.getType(),
                    fuelTypes.toString()
                });
            }
        }
    }

    private void clearForm() {
        name.setText("");
        latitude.setText("");
        longitude.setText("");
        selected = null;
    }

    public SManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout()); //set page layout as a borderlayout

        topTab tTab = new topTab("Manage Stations", cl, container, this);

        // define and add form for Station Settings
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);

        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setOpaque(true);

        // define entries and labels for the station's name, username, and password
        JLabel nameTxt = new JLabel("Name:");
        nameTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel latTxt = new JLabel("Latitude:");
        latTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        latitude.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel longTxt = new JLabel("Longitude:");
        longTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        longitude.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        sType.add(busType);
        sType.add(refuelType);
        busType.setAlignmentX(Component.CENTER_ALIGNMENT);
        refuelType.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel stationRadio = new JPanel();
        stationRadio.setAlignmentX(Component.CENTER_ALIGNMENT);
        stationRadio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        stationRadio.add(busType);
        stationRadio.add(refuelType);

        fuelHolder.add(unleaded);
        fuelHolder.add(diesel);
        fuelHolder.setAlignmentX(Component.CENTER_ALIGNMENT);
        fuelHolder.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        //button to update the station's account information
        JButton update = new JButton("Add/Update Station");
        update.setAlignmentX(Component.CENTER_ALIGNMENT);
        update.setBackground(Color.BLUE);
        update.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //button to delete the station's account
        JButton delete = new JButton("Delete Station");
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete.setBackground(Color.RED);
        delete.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //create table of StationManager.getStations() for manager to select
        String[] col = {"Name", "Latitude", "Longitude",
            "Station Type", "Supported Fuel(s)"};

        DefaultTableModel model = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Return false to make all cells non-editable
                return false;
            }
        };

        JTable table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 200));
        initTable(model);

        //Add a listener for table: when a station is selected, fill all fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row != -1) {
                    String nameV = model.getValueAt(row, 0).toString();
                    String latitudeV = model.getValueAt(row, 1).toString();
                    String longitudeV = model.getValueAt(row, 2).toString();
                    String typeV = model.getValueAt(row, 3).toString();

                    name.setText(nameV);
                    latitude.setText(latitudeV);
                    longitude.setText(longitudeV);

                    if (typeV.equals("bus")) {
                        busType.setSelected(true);
                        refuelType.setSelected(false);
                        fuelHolder.setVisible(false);
                    } else {
                        busType.setSelected(false);
                        refuelType.setSelected(true);
                        fuelHolder.setVisible(true);
                        String[] fuelsV = model.getValueAt(row, 4).toString()
                                .replace(",", "").split(" ");

                        unleaded.setSelected(false);
                        diesel.setSelected(false);

                        for (String i : fuelsV) {
                            if (i.equals("Unleaded")) {
                                unleaded.setSelected(true);
                            }
                            if (i.equals("Diesel")) {
                                diesel.setSelected(true);
                            }
                        }
                    }
                    selected = StationManager.getStations().get(row);
                    form.revalidate();
                    form.repaint();
                }
            }
        });

        //listener for station type radiogroup:
        ActionListener radioListener = e -> {
            JRadioButton rb = (JRadioButton) e.getSource();
            if (rb.isSelected()) {
                if (rb.getText().equals("Bus")) {
                    fuelHolder.setVisible(false);
                } else if (rb.getText().equals("Refuel")) {
                    fuelHolder.setVisible(true);
                }
                form.revalidate();
                form.repaint();
            }
        };
        busType.addActionListener(radioListener);
        refuelType.addActionListener(radioListener);

        //add listener for update button
        update.addActionListener(e -> {
            if (selected != null) {
                if (busType.isSelected()) {
                    String ogName = selected.getName();

                    BusStation newS = new BusStation(
                            name.getText(),
                            Float.parseFloat(latitude.getText()),
                            Float.parseFloat(longitude.getText())
                    );

                    if (ogName.equals(newS.getName())) {
                        StationManager.updateStation(newS);
                    } else {
                        StationManager.updateStation(newS, ogName);
                    }
                } else {
                    String ogName = selected.getName();
                    
                    ArrayList<String> fuels = new ArrayList<>();
                    if (diesel.isSelected()) {
                        fuels.add("diesel");
                    }
                    if (unleaded.isSelected()) {
                        fuels.add("unleaded");
                    }

                    RefuelStation newS = new RefuelStation(
                            name.getText(),
                            Float.parseFloat(latitude.getText()),
                            Float.parseFloat(longitude.getText()),
                            fuels
                    );

                    if (ogName.equals(newS.getName())) {
                        StationManager.updateStation(newS);
                    } else {
                        StationManager.updateStation(newS, ogName);
                    }
                }
            } else {
                if (!name.getText().isEmpty() && !latitude.getText().isEmpty() && !longitude.getText().isEmpty()) {
                    if (busType.isSelected()) {

                        BusStation newS = new BusStation(
                                name.getText(),
                                Float.parseFloat(latitude.getText()),
                                Float.parseFloat(longitude.getText())
                        );

                        StationManager.addStation(newS);
                    } else {

                        ArrayList<String> fuels = new ArrayList<>();
                        if (diesel.isSelected()) {
                            fuels.add("diesel");
                        }
                        if (unleaded.isSelected()) {
                            fuels.add("unleaded");
                        }

                        RefuelStation newS = new RefuelStation(
                                name.getText(),
                                Float.parseFloat(latitude.getText()),
                                Float.parseFloat(longitude.getText()),
                                fuels
                        );

                        StationManager.addStation(newS);
                    }
                }
            }
            clearForm();
            initTable(model);
            form.revalidate();
            form.repaint();
        });

        //add listener for delete button
        delete.addActionListener(e -> {
            if (selected != null) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete this account? This action cannot be undone.",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    StationManager.deleteStation(selected);
                    selected = null;
                    clearForm();
                    initTable(model);
                    form.revalidate();
                    form.repaint();
                } else {
                    //do nothing, pane will close
                }
            }
        });

        //failsafe: reinitialize table whenever screen is reentered
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                initTable(model);
            }
        });

        //add all elements to the form
        form.add(nameTxt, f);
        f.gridy = 1;
        form.add(name, f);

        f.gridy = 2;
        form.add(latTxt, f);
        f.gridy = 3;
        form.add(latitude, f);

        f.gridy = 4;
        form.add(longTxt, f);
        f.gridy = 5;
        form.add(longitude, f);

        f.gridy = 6;
        form.add(stationRadio, f);

        f.gridy = 7;
        form.add(fuelHolder, f);

        f.gridy = 8;
        form.add(update, f);

        f.gridy = 9;
        form.add(delete, f);

        JScrollPane stationForm = new JScrollPane(form);

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                stationForm,
                scroll
        );
        split.setResizeWeight(0.6);

        add(split, BorderLayout.CENTER);
        add(tTab, BorderLayout.NORTH);
    }
}
