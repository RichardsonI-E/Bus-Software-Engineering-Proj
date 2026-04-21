package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import components.FilterString;
import components.Session;
import components.topTab;
import permissions.Admin;

/*This class is to allow a user to modify their own account,
changing their name and password, or delete their account
 */
public class SettingsScreen extends JPanel {
    // get cardlayout and container from parent
    private final CardLayout cl;
    private final JPanel container;

    // add fields for each attribute of user
    private final JTextField fName = new JTextField();
    private final JTextField lName = new JTextField();
    private final JTextField userName = new JTextField();
    private final JTextField pass = new JTextField();

    public SettingsScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());

        //add Settings version of top tab to screen
        add(new topTab("Account Settings", cl, container, this), BorderLayout.NORTH);
        add(createContent(), BorderLayout.CENTER);

        // Refresh fields when screen is shown
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateFields();
            }
        });

        //set name fields to filter out numbers/symbols
        ((AbstractDocument) fName.getDocument()).setDocumentFilter(
            new FilterString());
        ((AbstractDocument) lName.getDocument()).setDocumentFilter(
            new FilterString());
    }

    //create the form and add to main content
    private JPanel createContent() {
        JPanel content = new JPanel();
        content.add(createForm());
        return content;
    }

    //create form to allow user to modify info
    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 60, 20, 60));

        GridBagConstraints gbc = createGBC();

        //create and add fields to form
        addField(form, gbc, 0, "First Name:", fName);
        addField(form, gbc, 2, "Last Name:", lName);
        addField(form, gbc, 4, "Username:", userName);
        addField(form, gbc, 6, "Password:", pass);

        //username will generate automatically, prevent editing
        userName.setEditable(false);

        //create buttons with appropriate colors
        JButton updateBtn = createButton("Update Account", Color.BLUE);
        JButton deleteBtn = createButton("Delete Account", Color.RED);

        //add listeners for both buttons to update/delete
        updateBtn.addActionListener(e -> handleUpdate());
        deleteBtn.addActionListener(e -> handleDelete());

        //add components to form grid
        gbc.gridy = 8;
        form.add(updateBtn, gbc);

        gbc.gridy = 9;
        form.add(deleteBtn, gbc);

        return form;
    }

    //create a new field with respective text, add to next row in form
    private void addField(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);

        gbc.gridy = y;
        panel.add(label, gbc);

        gbc.gridy = y + 1;
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(field, gbc);
    }

    //constrain form to gridbag layout (vertical)
    private GridBagConstraints createGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(5, 0, 5, 0);
        return gbc;
    }

    //method to create button with respective text and color
    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));
        return btn;
    }


    //method to update fields with user's current info
    private void updateFields() {
        //split name into first and last
        String name = Session.getUser().getName();
        String[] parts = name.split(" ");

        //place info in respective field
        fName.setText(parts[0]);
        lName.setText(parts[parts.length - 1]);
        userName.setText(Session.getUser().getUsername());
        pass.setText(Session.getUser().getPassword());
    }

    //update method when info in fields are changed
    private void handleUpdate() {
        //remove unnecessary spaces from text in fields
        String newFirst = fName.getText().trim();
        String newLast = lName.getText().trim();
        String newPass = pass.getText().trim();
        //save old username in case name has changed
        String oldUsername = userName.getText();

        //if fields are empty, abort
        if (newFirst.isEmpty() || newLast.isEmpty() || newPass.isEmpty())
            return;

        //combine first and last to make new full name
        String newName = newFirst + " " + newLast;

        //if new name is not the same as the old, generate new username
        if (!Session.getUser().getName().equals(newName)) {
            String newUser = generateUsername(newFirst, newLast);

            Session.getUser().setName(newName);
            Session.getUser().setUsername(newUser);
        }

        //set password and update user in database
        Session.getUser().setPassword(newPass);
        Admin.updateUser(Session.getUser(), oldUsername);

        //confirm info was updated
        JOptionPane.showMessageDialog(this,
                "Your account has been updated",
                "Account Updated",
                JOptionPane.INFORMATION_MESSAGE);

        updateFields();
    }

    //method to generate a new username based on full name
    private String generateUsername(String first, String last) {
        //base of username: lastname and first letter of first name
        String base = last.toLowerCase() + first.substring(0, 1).toLowerCase();

        //if the base equals another username in database, add random numbers end
        for (var user : Admin.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(base)) {
                return base + ThreadLocalRandom.current().nextInt(1, 10000);
            }
        }

        return base;
    }

    private void handleDelete() {
        //warn user of deletion
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this account? This cannot be undone.",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        //remove user from database, remove as current user, return to login
        if (response == JOptionPane.YES_OPTION) {
            Admin.deleteUser(Session.getUser());
            Session.clearUser();
            cl.show(container, "login");
        }
    }
}