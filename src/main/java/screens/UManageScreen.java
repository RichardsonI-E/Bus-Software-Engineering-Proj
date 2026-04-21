package screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import components.topTab;
import permissions.Admin;
import primary.User;

public class UManageScreen extends JPanel {
    //get the map, layout and container from parent
    private CardLayout cl;
    private JPanel container;

    // add fields for each attribute of user
    private JTextField fName = new JTextField();
    private JTextField lName = new JTextField();
    private JTextField userName = new JTextField();
    private JTextField pass = new JTextField();

    //declare a combobox to choose the permissions of a user
    private JComboBox<String> perms = new JComboBox<>(
            new String[]{"basic", "busManager", "stationManager", "admin"});

    //variable to hold the user that is selected
    private User selected = null;

    //model for the table of users
    private DefaultTableModel model;

    // ===================== CONSTRUCTOR =====================
    public UManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());

        //add user management version of top tab
        add(new topTab("Manage Users", cl, container, this), BorderLayout.NORTH);
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

        //configure the constraints of the fields
        configureFields();

        //create fields with labels
        addField(form, f, new JLabel("First Name:"), fName);
        addField(form, f, new JLabel("Last Name:"), lName);
        addField(form, f, new JLabel("Username:"), userName);
        addField(form, f, new JLabel("Password:"), pass);
        addField(form, f, new JLabel("Permissions:"), perms);

        //add the buttons and fields to form
        form.add(createUpdateButton(), nextRow(f));
        form.add(createDeleteButton(), nextRow(f));

        return form;
    }

    private JScrollPane createTable() {
        //create table with user attributes as column labels
        String[] col = {"First Name", "Last Name", "Username", "Password", "Permissions"};

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

    //set the size of the fields and prevent personal info from being edited
    private void configureFields() {
        for (JTextField field : new JTextField[]{fName, lName, userName, pass}) {
            field.setEditable(false);
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        }
        perms.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    //add each user in the database to the table
    private void initTable() {
        model.setRowCount(0);

        for (User u : Admin.getUsers()) {
            String[] name = u.getName().split(" ");
            model.addRow(new Object[]{
                    name[0], name[1], u.getUsername(), u.getPassword(), u.getPerms()
            });
        }
    }

    //listener to get selected stations
    private void addTableListener(JTable table) {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            //abort if table is being adjusted
            int row = table.getSelectedRow();
            if (row == -1) return;

            //set the fields to respective values from selected user
            fName.setText(model.getValueAt(row, 0).toString());
            lName.setText(model.getValueAt(row, 1).toString());
            userName.setText(model.getValueAt(row, 2).toString());
            pass.setText(model.getValueAt(row, 3).toString());
            perms.setSelectedItem(model.getValueAt(row, 4));

            selected = Admin.getUsers().get(row);
        });
    }

    //create the update button for the form
    private JButton createUpdateButton() {
        JButton btn = new JButton("Update");
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

    private void handleUpdate() {
        //if no user is selected, abort
        if (selected == null) return;

        //set the info in selected user to info in fields
        selected.setName(fName.getText() + " " + lName.getText());
        selected.setPassword(pass.getText());
        selected.setPerms(perms.getSelectedItem().toString());

        //upload user to database
        Admin.updateUser(selected);
        refresh();
    }

    private void handleDelete() {
        //if no station is selected, abort
        if (selected == null) return;

        //show confirmation dialog to delete user
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this account?", "Confirm",
                JOptionPane.YES_NO_OPTION);

        //if confirmed, remove user and reload the form and table
        if (confirm == JOptionPane.YES_OPTION) {
            Admin.deleteUser(selected);
            refresh();
        }
    }

    //method to reload the page
    private void refresh() {
        //no station selected
        selected = null;
        //remove values from form fields
        clearForm();
        //restart table
        initTable();
    }

    //remove values from form fields
    private void clearForm() {
        fName.setText("");
        lName.setText("");
        userName.setText("");
        pass.setText("");
        perms.setSelectedItem(null);
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