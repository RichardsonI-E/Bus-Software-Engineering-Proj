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
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import components.Session;
import components.topTab;
import permissions.Admin;

public class SettingsScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;

    //add fields in advance for methods
    JTextField fName = new JTextField();
    JTextField lName = new JTextField();
    JTextField userName = new JTextField();
    JTextField pass = new JTextField();

    private void updateFields() {
        //get User's name, username and password
        String name = Session.getUser().getName();
        String username = Session.getUser().getUsername();
        String password = Session.getUser().getPassword();

        //split user's full name into first and last
        String[] nameArr = name.split(" ");

        fName.setText(nameArr[0]);
        lName.setText(nameArr[nameArr.length - 1]);
        userName.setText(username);
        pass.setText(password);
    }

    public SettingsScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Account Settings", cl, container, this);

        JPanel content = new JPanel();

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
        fName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lastTxt = new JLabel("Last Name:");
        lastTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        lName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel userTxt = new JLabel("Username:");
        userTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        userName.setEditable(false);
        userName.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel passTxt = new JLabel("Password:");
        passTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        //button to update the user's account information
        JButton update = new JButton("Update Account");
        update.setAlignmentX(Component.CENTER_ALIGNMENT);
        update.setBackground(Color.BLUE);
        update.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //button to delete the user's account
        JButton delete = new JButton("Delete Account");
        delete.setAlignmentX(Component.CENTER_ALIGNMENT);
        delete.setBackground(Color.RED);
        delete.setMaximumSize(new Dimension(Integer.MAX_VALUE / 3, 60));

        //set action listener for update button
        update.addActionListener(e -> {
            String newFirst = fName.getText().trim();
            String newLast = lName.getText().trim();
            String newPass = pass.getText().trim();
            String ogUsername = userName.getText();

            if (!newFirst.isEmpty() && !newLast.isEmpty()
                    && !newPass.isEmpty()) {

                String newName = newFirst + " " + newLast;

                if (!Session.getUser().getName().equals(newName)) {
                    // create and set custom username for user
                    String newUser = newLast.toLowerCase()
                            + (newFirst.isEmpty() ? "" : newFirst.substring(0, 1).toLowerCase());
                    // contingent: if two usernames are the same, add a 1 to the end of the username
                    Admin.getUsers();
                    for (int l = 0; l < Admin.getUsers().size(); l++) {
                        if (Admin.getUsers().get(l).getUsername().equalsIgnoreCase(newUser)) {
                            newUser = newUser + ThreadLocalRandom.current().nextInt(1, 10000);
                        }
                    }
                    Session.getUser().setName(newFirst + " " + newLast);
                    Session.getUser().setUsername(newUser);
                }

                Session.getUser().setPassword(newPass);

                Admin.updateUser(Session.getUser(), ogUsername);

                JOptionPane.showMessageDialog(null,
                                    "Your account has been updated",
                                    "Account Updated!",
                                    JOptionPane.INFORMATION_MESSAGE);
                    
                updateFields();
                revalidate();
                repaint();
            }
        });

        //set action listener for delete account button
        delete.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to delete this account? This action cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                Admin.deleteUser(Session.getUser());
                Session.clearUser();
                cl.show(container, "login");
            } else {
                //do nothing, pane will close
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateFields();
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
        form.add(update, f);

        f.gridy = 9;
        form.add(delete, f);

        content.add(form);

        add(tTab, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
}
