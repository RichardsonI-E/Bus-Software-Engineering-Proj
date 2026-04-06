package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import components.topTab;
import permissions.Admin;
import primary.User;

public class BManageScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;

    //add fields in advance
    JTextField fName = new JTextField();
    JTextField lName = new JTextField();
    JTextField userName = new JTextField();
    JTextField pass = new JTextField();

    JComboBox<String> perms = new JComboBox<>(
            new String[]{"basic", "busManager", "stationManager", "admin"});

    //add a selected user for admin to update or delete
    User selected = null;

    private void initTable(DefaultTableModel model) {
        model.setRowCount(0); // clear table

        for (User u : Admin.getUsers()) {
            String[] name = u.getName().split(" ");
            model.addRow(new Object[]{
                name[0],
                name[1],
                u.getUsername(),
                u.getPassword(),
                u.getPerms()
            });
        }
    }

    private void clearForm() {
        fName.setText("");
        lName.setText("");
        userName.setText("");
        perms.setSelectedItem(null);
        pass.setText("");
        selected = null;
    }

    public BManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout()); //set page layout as a borderlayout

        topTab tTab = new topTab("Manage Buses", cl, container, this);

        // define and add form for User Settings
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

        // define entries and labels for the user's name, username, and password
        JLabel firstTxt = new JLabel("First Name:");
        firstTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        fName.setEditable(false);
        fName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lastTxt = new JLabel("Last Name:");
        lastTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        lName.setEditable(false);
        lName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel userTxt = new JLabel("Username:");
        userTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        userName.setEditable(false);
        userName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel passTxt = new JLabel("Password:");
        passTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass.setEditable(false);
        pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel permTxt = new JLabel("Permissions");
        permTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        perms.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        //button to update the user's account information
        JButton update = new JButton("Add/Update Account");
        update.setAlignmentX(Component.CENTER_ALIGNMENT);
        update.setBackground(Color.BLUE);
        update.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //button to delete the user's account
        JButton delete = new JButton("Delete Account");
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete.setBackground(Color.RED);
        delete.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //create table of Admin.getUsers() for admin to select
        String[] col = {"First Name", "Last Name", "Username",
            "Password", "Permissions"};

        DefaultTableModel model = new DefaultTableModel(col, 0);
        JTable table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(500, 200));
        initTable(model);

        //Add a listener for table: when a user is selected, fill all fields
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();

                if (row != -1) {
                    String firstName = model.getValueAt(row, 0).toString();
                    String lastName = model.getValueAt(row, 1).toString();
                    String username = model.getValueAt(row, 2).toString();
                    String password = model.getValueAt(row, 3).toString();
                    String permission = model.getValueAt(row, 4).toString();

                    fName.setText(firstName);
                    lName.setText(lastName);
                    userName.setText(username);
                    pass.setText(password);
                    perms.setSelectedItem(permission);

                    selected = Admin.getUsers().get(row);
                }
            }
        });

        //add all elements to the form
        form.add(firstTxt, f);
        f.gridy = 1;
        form.add(fName, f);

        f.gridy = 2;
        form.add(lastTxt, f);
        f.gridy = 3;
        form.add(lName, f);

        f.gridy = 4;
        form.add(userTxt, f);
        f.gridy = 5;
        form.add(userName, f);

        f.gridy = 6;
        form.add(passTxt, f);
        f.gridy = 7;
        form.add(pass, f);

        f.gridy = 8;
        form.add(permTxt, f);
        f.gridy = 9;
        form.add(perms, f);

        f.gridy = 10;
        form.add(update, f);

        f.gridy = 11;
        form.add(delete, f);

        JScrollPane userForm = new JScrollPane();
        userForm.setPreferredSize(new Dimension(500, 200));
        userForm.add(form);

        add(userForm, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
        add(tTab, BorderLayout.NORTH);
    }
}
