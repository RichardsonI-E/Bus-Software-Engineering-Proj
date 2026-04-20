package screens;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import components.topTab;
import permissions.BusManager;
import primary.Bus;
import primary.Bus.CityBus;
import primary.Bus.LongDisBus;

public class BManageScreen extends JPanel {

    // get layout and container from parent
    private CardLayout cl;
    private JPanel container;

    //custom mask to limit characters
    private MaskFormatter nameMask;

    // ---------- Form Fields ----------
    //format textfields as number values
    private NumberFormat num = NumberFormat.getNumberInstance();

    //declare text fields for each bus attribute (formatted respectively)
    private JFormattedTextField make = new JFormattedTextField(nameMask);
    private JFormattedTextField model = new JFormattedTextField(nameMask);
    private JFormattedTextField tankSize = new JFormattedTextField(num);
    private JFormattedTextField fuelBurn = new JFormattedTextField(num);
    private JFormattedTextField cruiseSpeed = new JFormattedTextField(num);

    //add radio button group for city or long distance bus
    private ButtonGroup sType = new ButtonGroup();
    private JRadioButton cityType = new JRadioButton("City");
    private JRadioButton longDistType = new JRadioButton("Long Distance", true);

    //set currently selected bus as variable
    private Bus selected = null;

    // ---------- Declare Table ----------
    private DefaultTableModel tModel;
    private JTable table;

    // ---------- Constructor ----------
    public BManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());

        //add manage buses version of top tab
        add(new topTab("Manage Buses", cl, container, this), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        try {
            nameMask = new MaskFormatter("****************");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //add listener to refresh table when card is active
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshTable();
            }
        });
    }

    // ---------- UI Builders ----------

    //add form and table of buses, then add to split pane
    private JSplitPane createMainContent() {
        JPanel form = createFormPanel();
        JScrollPane tablePane = createTablePanel();

        JSplitPane split = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(form),
                tablePane
        );

        split.setResizeWeight(0.6);
        return split;
    }

    //create form and components
    private JPanel createFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 60, 20, 60));

        //define constraints of form
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);

        //add each (editable) bus attribute as a form field with appropriate label
        addFormField(form, f, "Make:", make);
        addFormField(form, f, "Model:", model);
        addFormField(form, f, "Tank Size (Gallons):", tankSize);
        addFormField(form, f, "Fuel Burn Rate (MPG):", fuelBurn);
        addFormField(form, f, "Cruise Speed (MPH):", cruiseSpeed);

        //add radio buttons
        form.add(createRadioPanel(), f);
        f.gridy++;

        //add buttons
        form.add(createButtonPanel(), f);

        return form;
    }

    //method to add text fields for each component (and respective label)
    private void addFormField(JPanel panel, GridBagConstraints f, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        panel.add(label, f);
        f.gridy++;

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(field, f);
        f.gridy++;
    }

    //create custom panel to hold radio buttons, and add both buttons to group
    private JPanel createRadioPanel() {
        sType.add(cityType);
        sType.add(longDistType);

        JPanel panel = new JPanel();
        panel.add(cityType);
        panel.add(longDistType);

        return panel;
    }

    //create buttons for form
    private JPanel createButtonPanel() {
        //create update/add and delete buttons
        JButton update = new JButton("Add/Update Bus");
        JButton delete = new JButton("Delete Bus");

        //set add/update button to blue, and delete to red
        update.setBackground(Color.BLUE);
        delete.setBackground(Color.RED);

        //add update listeners to both buttons
        update.addActionListener(e -> handleUpdate());
        delete.addActionListener(e -> handleDelete());

        //add buttons to custom panel
        JPanel panel = new JPanel();
        panel.add(update);
        panel.add(delete);

        return panel;
    }

    //create a table to hold list of buses
    private JScrollPane createTablePanel() {
        //add 7 column to sort bus attributes
        String[] col = { "ID", "Make", "Model", "Tank Size", "Fuel Burn", "Speed", "Type" };

        //prevent all cells in table from being edited
        tModel = new DefaultTableModel(col, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        //set model of table and add listener for selected row
        table = new JTable(tModel);
        table.getSelectionModel().addListSelectionListener(e -> handleSelection());

        //refresh table with new parameters
        refreshTable();

        return new JScrollPane(table);
    }

    // ---------- Logic ----------

    //reformat the table
    private void refreshTable() {
        tModel.setRowCount(0);

        //for each bus in the database, add appropriate values
        for (Bus b : BusManager.getBuses()) {
            tModel.addRow(new Object[] {
                    b.getBusID(),
                    b.getMake(),
                    b.getModel(),
                    b.getTankSize(),
                    b.getFuelBurnRate(),
                    b.getCruiseSpeed(),
                    (b instanceof CityBus) ? "City" : "Long Distance"
            });
        }
    }

    //method to handle the selected row
    private void handleSelection() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) tModel.getValueAt(row, 0);

        //for the selected row, set all fields to the appropriate value
        make.setText(tModel.getValueAt(row, 1).toString());
        model.setText(tModel.getValueAt(row, 2).toString());
        tankSize.setValue(tModel.getValueAt(row, 3));
        fuelBurn.setValue(tModel.getValueAt(row, 4));
        cruiseSpeed.setValue(tModel.getValueAt(row, 5));

        //set selected radio button to the bus' type
        boolean isCity = tModel.getValueAt(row, 6).toString().equalsIgnoreCase("city");
        cityType.setSelected(isCity);
        longDistType.setSelected(!isCity);

        selected = BusManager.getBusByID(id);
    }

    //method to update values for a bus
    private void handleUpdate() {
        //validate entries in formatted fields
        try {
            tankSize.commitEdit();
            fuelBurn.commitEdit();
            cruiseSpeed.commitEdit();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format");
            return;
        }

        //create bus from entries in form
        Bus bus = createBusFromForm();

        //if the bus is null, abort
        if (bus == null) return;

        //if a bus is already selected, update selected with given info
        if (selected != null) {
            bus.setBusID(selected.getBusID());
            BusManager.updateBus(bus);
        } else {
            //otherwise add as a new bus
            BusManager.addBus(bus);
        }

        //refresh the table and clear the form after
        clearForm();
        refreshTable();
    }

    //method to delete selected bus
    private void handleDelete() {
        // if no bus is selected, abort
        if (selected == null) return;

        //show confirmation dialog to delete selected bus
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this bus?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        //if user selects yes, delete bus, then refresh
        if (confirm == JOptionPane.YES_OPTION) {
            BusManager.deleteBus(selected);
            clearForm();
            refreshTable();
        }
    }

    //method to create a bus object from values in fields
    private Bus createBusFromForm() {
        //if make or model isn't defined, return a null bus
        if (make.getText().isEmpty() || model.getText().isEmpty()) return null;

        //set appropriate attributes as values in fields
        float tank = ((Number) tankSize.getValue()).floatValue();
        float burn = ((Number) fuelBurn.getValue()).floatValue();
        float speed = ((Number) cruiseSpeed.getValue()).floatValue();

        //set the subclass of the bus depending on radio button selected
        if (cityType.isSelected()) {
            return new CityBus(make.getText(), model.getText(), tank, burn, speed);
        } else {
            return new LongDisBus(make.getText(), model.getText(), tank, burn, speed);
        }
    }

    //clear all fields, and set the selected bus to none
    private void clearForm() {
        make.setText("");
        model.setText("");
        tankSize.setValue(null);
        fuelBurn.setValue(null);
        cruiseSpeed.setValue(null);
        selected = null;
    }
}