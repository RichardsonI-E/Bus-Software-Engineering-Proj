package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
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
import permissions.BusManager;
import primary.Bus;
import primary.Bus.CityBus;
import primary.Bus.LongDisBus;

// 24.5°N to 71.4°N latitude and 66.9°W to 172.5°E longitude
public class BManageScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;

    NumberFormat num = NumberFormat.getNumberInstance();

    //add fields in advance
    JTextField make = new JTextField();
    JTextField model = new JTextField();
    JFormattedTextField tankSize = new JFormattedTextField(num);
    JFormattedTextField fuelBurn = new JFormattedTextField(num);
    JFormattedTextField cruiseSpeed = new JFormattedTextField(num);

    ButtonGroup sType = new ButtonGroup();
    JRadioButton cityType = new JRadioButton("City");
    JRadioButton longDistType = new JRadioButton("Long Distance", true);

    //add a selected bus for manager to update or delete
    Bus selected = null;

    private void initTable(DefaultTableModel model) {
        model.setRowCount(0); // clear table

        for (Bus s : BusManager.getBuses()) {
            if (s instanceof Bus.CityBus) {
                model.addRow(new Object[]{
                    s.getBusID(),
                    s.getMake(),
                    s.getModel(),
                    s.getTankSize(),
                    s.getFuelBurnRate(),
                    s.getCruiseSpeed(),
                    "City"
                });
            } else if (s instanceof Bus.LongDisBus r) {
                model.addRow(new Object[]{
                    s.getBusID(),
                    s.getMake(),
                    s.getModel(),
                    s.getTankSize(),
                    s.getFuelBurnRate(),
                    s.getCruiseSpeed(),
                    "Long Distance"
                });
            }
        }
    }

    private void clearForm() {
        make.setText("");
        model.setText("");
        tankSize.setText("");
        fuelBurn.setText("");
        cruiseSpeed.setText("");
        selected = null;
    }

    public BManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout()); //set page layout as a borderlayout

        topTab tTab = new topTab("Manage Buses", cl, container, this);

        // define and add form for Bus Settings
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

        // define entries and labels for the bus' make, usermake, and password
        JLabel makeTxt = new JLabel("Make:");
        makeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        make.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel modelTxt = new JLabel("Model:");
        modelTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        model.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel tankSizeTxt = new JLabel("Tank Size (in Gallons):");
        tankSizeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        tankSize.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel fuelBurnTxt = new JLabel("Fuel Burn Rate (in Miles per Gallon):");
        fuelBurnTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        fuelBurn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel cruiseSpeedTxt = new JLabel("Cruise Speed (in Miles per Hour):");
        cruiseSpeedTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        cruiseSpeed.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        sType.add(cityType);
        sType.add(longDistType);
        cityType.setAlignmentX(Component.CENTER_ALIGNMENT);
        longDistType.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel busRadio = new JPanel();
        busRadio.setAlignmentX(Component.CENTER_ALIGNMENT);
        busRadio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        busRadio.add(cityType);
        busRadio.add(longDistType);

        //button to update the bus' account information
        JButton update = new JButton("Add/Update Bus");
        update.setAlignmentX(Component.CENTER_ALIGNMENT);
        update.setBackground(Color.BLUE);
        update.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //button to delete the bus' account
        JButton delete = new JButton("Delete Bus");
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete.setBackground(Color.RED);
        delete.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //create table of BusManager.getBuses() for manager to select
        String[] col = {"ID", "Make", "Model", "Tank Size (Gallons)",
            "Fuel Burn Rate (Miles per Gallon)", "Cruise Speed", "Bus Type"};

        DefaultTableModel tModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Return false to make all cells non-editable
                return false;
            }
        };

        JTable table = new JTable(tModel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 200));
        initTable(tModel);

        //Add a listener for table: when a bus is selected, fill all fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row != -1) {
                    int id = (int) tModel.getValueAt(row, 0);
                    String makeV = tModel.getValueAt(row, 1).toString();
                    String modelV = tModel.getValueAt(row, 2).toString();
                    float tankSizeV = (float) ((Number) (tModel.getValueAt(row, 3))).floatValue();
                    float fuelBurnV = (float) ((Number) (tModel.getValueAt(row, 4))).floatValue();
                    float cruiseSpeedV = (float) ((Number) (tModel.getValueAt(row, 5))).floatValue();
                    String busType = tModel.getValueAt(row, 6).toString();

                    make.setText(makeV);
                    model.setText(modelV);
                    tankSize.setText(String.valueOf(tankSizeV));
                    fuelBurn.setText(String.valueOf(fuelBurnV));
                    cruiseSpeed.setText(String.valueOf(cruiseSpeedV));

                    if (busType.equalsIgnoreCase("city")) {
                        cityType.setSelected(true);
                        longDistType.setSelected(false);
                    } else {
                        cityType.setSelected(false);
                        longDistType.setSelected(true);
                    }
                    selected = BusManager.getBusByID(id);
                    form.revalidate();
                    form.repaint();
                }
            }
        });

        //add listener for update button
        update.addActionListener(e -> {
            try {
                tankSize.commitEdit();
                fuelBurn.commitEdit();
                cruiseSpeed.commitEdit();
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Invalid number format");
                return;
            }
            
            if (selected != null) {
                if (cityType.isSelected()) {
                    CityBus newS = new CityBus(
                            make.getText(),
                            model.getText(),
                            ((Number) (tankSize.getValue())).floatValue(),
                            ((Number) (fuelBurn.getValue())).floatValue(),
                            ((Number) (cruiseSpeed.getValue())).floatValue()
                    );

                    newS.setBusID(selected.getBusID());
                    BusManager.updateBus(newS);
                } else {
                    LongDisBus newS = new LongDisBus(
                            make.getText(),
                            model.getText(),
                            ((Number) (tankSize.getValue())).floatValue(),
                            ((Number) (fuelBurn.getValue())).floatValue(),
                            ((Number) (cruiseSpeed.getValue())).floatValue()
                    );

                    newS.setBusID(selected.getBusID());
                    BusManager.updateBus(newS);
                }
            } else {
                if (!make.getText().isEmpty() && !model.getText().isEmpty()
                        && !tankSize.getText().isEmpty() && !fuelBurn.getText().isEmpty()
                        && !cruiseSpeed.getText().isEmpty()) {
                    if (cityType.isSelected()) {

                        CityBus newS = new CityBus(
                                make.getText(),
                                model.getText(),
                                ((Number) (tankSize.getValue())).floatValue(),
                                ((Number) (fuelBurn.getValue())).floatValue(),
                                ((Number) (cruiseSpeed.getValue())).floatValue()
                        );

                        BusManager.addBus(newS);
                    } else {

                        LongDisBus newS = new LongDisBus(
                                make.getText(),
                                model.getText(),
                                ((Number) (tankSize.getValue())).floatValue(),
                                ((Number) (fuelBurn.getValue())).floatValue(),
                                ((Number) (cruiseSpeed.getValue())).floatValue()
                        );

                        BusManager.addBus(newS);
                    }
                }
            }
            clearForm();
            initTable(tModel);
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
                    BusManager.deleteBus(selected);
                    selected = null;
                    clearForm();
                    initTable(tModel);
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
                initTable(tModel);
            }
        });

        //add all elements to the form
        form.add(makeTxt, f);
        f.gridy = 1;
        form.add(make, f);

        f.gridy = 2;
        form.add(modelTxt, f);
        f.gridy = 3;
        form.add(model, f);

        f.gridy = 4;
        form.add(tankSizeTxt, f);
        f.gridy = 5;
        form.add(tankSize, f);

        f.gridy = 6;
        form.add(fuelBurnTxt, f);
        f.gridy = 7;
        form.add(fuelBurn, f);

        f.gridy = 8;
        form.add(cruiseSpeedTxt, f);
        f.gridy = 9;
        form.add(cruiseSpeed, f);

        f.gridy = 10;
        form.add(busRadio, f);

        f.gridy = 11;
        form.add(update, f);

        f.gridy = 12;
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
