package screens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import components.Session;
import permissions.Admin;
import primary.User;

/*The main class associated with the Login Screen. It allows the user to input a username and (hidden)
password (currently allows login via pressing the button) or create an account, inputting their first and last name,
then make and confirm a password, in which a username will be automatically generated*/
public class LoginScreen extends JPanel {
    //get parent's layout and container
    private CardLayout cl;
    private JPanel container;

    //declare username and password fields in advance
    private JTextField userField = new JTextField();
    private JPasswordField passField = new JPasswordField();

    //constructor, create layout and background, init main panel
    public LoginScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new GridBagLayout());
        setBackground(Color.LIGHT_GRAY);

        add(createMainPanel(parent));
    }

    // ---------- UI BUILDERS ----------
    private JPanel createMainPanel(JFrame parent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        //create logo, title and for with proper spacing
        panel.add(createLogo());
        panel.add(Box.createVerticalStrut(15));
        panel.add(createTitle());
        panel.add(Box.createVerticalStrut(20));
        panel.add(createForm(parent));

        return panel;
    }

    //create the app logo to display on login screen
    private JPanel createLogo() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.LIGHT_GRAY);

        //convert image to icon and define size
        ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
        Image scaled = logo.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        //add icon to logo panel
        logoPanel.add(new JLabel(new ImageIcon(scaled)));
        return logoPanel;
    }

    //create title of the form
    private JLabel createTitle() {
        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    //create form to input username and password, or create new user
    private JPanel createForm(JFrame parent) {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setBackground(Color.WHITE);

        GridBagConstraints f = baseConstraints();

        //call method to create user and pass fields
        addField(form, f, "Username:", userField);
        addField(form, f, "Password:", passField);

        //create login button and add listener
        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> handleLogin(parent));

        //create custom label for creating new account
        JLabel create = createSignupLabel(parent);

        //declare constraints and add components to form
        f.anchor = GridBagConstraints.CENTER;
        f.fill = GridBagConstraints.NONE;

        form.add(loginBtn, f);
        f.gridy++;
        form.add(create, f);

        return form;
    }

    //declare main form constraints (vertically aligned)
    private GridBagConstraints baseConstraints() {
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 0;
        f.gridy = 0;
        f.fill = GridBagConstraints.HORIZONTAL;
        f.weightx = 1.0;
        f.insets = new Insets(5, 0, 5, 0);
        return f;
    }

    //method to add and style the given field
    private void addField(JPanel panel, GridBagConstraints f, String label, JComponent field) {
        //add label to form, go to next row in form
        panel.add(new JLabel(label), f);
        f.gridy++;

        //restrain field, add to form
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(field, f);
        f.gridy++;
    }

    // ---------- LOGIN LOGIC ----------
    private void handleLogin(JFrame parent) {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        User user = Admin.login(username, password);

        if (user != null) {
            Session.setUser(user);
            cl.show(container, "home");
        } else {
            JOptionPane.showMessageDialog(parent, "Invalid username or password");
        }
    }

    // ---------- SIGNUP ----------

    private JLabel createSignupLabel(JFrame parent) {
        JLabel label = new JLabel("Create an Account");
        label.setForeground(Color.BLUE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        label.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.DARK_GRAY);
            }

            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLUE);
            }

            public void mouseClicked(MouseEvent e) {
                openSignupDialog(parent);
            }
        });

        return label;
    }

    private void openSignupDialog(JFrame parent) {
        JTextField first = new JTextField();
        JTextField last = new JTextField();
        JPasswordField pass = new JPasswordField();
        JPasswordField confirm = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("First Name:"));
        panel.add(first);
        panel.add(new JLabel("Last Name:"));
        panel.add(last);
        panel.add(new JLabel("Password:"));
        panel.add(pass);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirm);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Sign Up",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            handleSignup(parent, first.getText(), last.getText(), pass, confirm);
        }
    }

    private void handleSignup(JFrame parent, String first, String last,
            JPasswordField pass, JPasswordField confirm) {

        if (first.isEmpty() || last.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "All fields required");
            return;
        }

        if (!java.util.Arrays.equals(pass.getPassword(), confirm.getPassword())) {
            JOptionPane.showMessageDialog(parent, "Passwords do not match");
            return;
        }

        String username = Admin.createUser(first, last, new String(pass.getPassword()));

        //confirm an account has been created & show username
        JOptionPane.showMessageDialog(parent,
                "Account created! Username: " + username);
    }
}