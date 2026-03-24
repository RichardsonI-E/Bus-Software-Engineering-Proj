package screens;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.User;

public class LoginScreen extends JPanel{
    private CardLayout cl;
    private JPanel container;
    public LoginScreen(JFrame parent, CardLayout cl, JPanel container){
        this.cl = cl;
        this.container = container;
        User x = null;
        setLayout(new GridBagLayout());
        setBackground(Color.lightGray);
        JPanel logContainer = new JPanel();
        logContainer.setBackground(Color.lightGray);
        logContainer.setLayout(new BoxLayout(logContainer, BoxLayout.Y_AXIS));
        //creates a container to hold the login screen with grid layout (1 column, 3 rows)

        //define and add placeholder logo
        JPanel logoP = new JPanel();
        logoP.setPreferredSize(new Dimension(120, 120));
        logoP.setMaximumSize(new Dimension(120, 120));
        logoP.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoP.setBackground(Color.yellow);
        logoP.add(new JLabel("(Placeholder)"));

        logContainer.add(logoP);
        logContainer.add(Box.createRigidArea(new Dimension(0, 15)));
    
        //define and add Title label
        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(10, 20, 10, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBackground(Color.lightGray);
        title.setOpaque(true);

        logContainer.add(title);
        logContainer.add(Box.createRigidArea(new Dimension(0, 20)));

        //define and add form for login/signup
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
        form.setOpaque(true);
        form.setBackground(Color.white);

        JLabel userTxt = new JLabel("Username:");
        userTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField user = new JTextField();
        user.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel passTxt = new JLabel("Password:");
        passTxt.setAlignmentY(Component.LEFT_ALIGNMENT);
        JPasswordField pass = new JPasswordField();
        pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));


        JButton login = new JButton("Login");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);
        login.addActionListener( e -> {
            String userIn = user.getText().trim();
            char[] password = pass.getPassword();
            String passIn = new String(password);
            if(userIn.equalsIgnoreCase("Test") 
                && passIn.equalsIgnoreCase("Test")){
            cl.show(container, "home");
            }
        /*
            for (user i : users){
                if (i.getUsername == userIn){
                    if (i.getPassword == passIn){
                    //return given user
                    }else{
                        pass.borderColor(red)
                        addLabel("Incorrect Password")}
                }else{
                    user.borderColor(red)
                    addLabel("User not Found")}}
        }
        */
        });

        JLabel create = new JLabel("Create an Account");
        create.setForeground(Color.BLUE);
        create.setAlignmentX(Component.CENTER_ALIGNMENT);



        form.add(userTxt, f);
        f.gridy = 1;
        form.add(user, f);

        f.gridy = 2;
        form.add(passTxt, f);
        f.gridy = 3;
        form.add(pass, f);

        f.gridy = 4;
        f.anchor = GridBagConstraints.CENTER;
        f.fill = GridBagConstraints.NONE;
        form.add(login, f);

        f.gridy = 5;
        form.add(create, f);

        logContainer.add(form);
        add(logContainer);

        
        //-------------functions for login:----------------

        create.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseEntered(MouseEvent e){
                create.setForeground(Color.DARK_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e){
                create.setForeground(Color.BLUE);
            }
            @Override
            public void mouseClicked(MouseEvent e){
                JDialog signUp = new JDialog(parent, "Sign Up", true);
                signUp.getContentPane().setLayout(new GridBagLayout());
                GridBagConstraints s = new GridBagConstraints();
                s.gridx = 0;
                s.gridy = 0;
                s.fill = GridBagConstraints.HORIZONTAL;
                s.weightx = 1.0;
                s.insets = new Insets(5, 0, 5, 0);

                JLabel fNTxt = new JLabel("First Name:");
                JTextField firstName = new JTextField();
                user.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                JLabel lNTxt = new JLabel("Last Name:");
                JTextField lastName = new JTextField();
                pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));


                JLabel pTxt = new JLabel("Create a Password:");
                JPasswordField makePass = new JPasswordField();
                pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                JLabel pCTxt = new JLabel("Confirm Password:");
                JPasswordField confPass = new JPasswordField();
                pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                JButton submit = new JButton("Create Account");
                submit.addActionListener(m ->{
                    System.out.println("Click");
                    if(java.util.Arrays.equals(makePass.getPassword(), confPass.getPassword())){
                    String firstNamef = firstName.getText().trim();
                    String lastNamef = lastName.getText().trim();
                    String usernamef = lastNamef +
                        (firstNamef.isEmpty() ? "" :
                        firstNamef.substring(0,1).toUpperCase() + firstNamef.substring(1));
                    User newU = new User();
                    newU.setName(firstNamef + " " + lastNamef);
                    char[] password = makePass.getPassword();
                    newU.setPassword(new String(password));
                    java.util.Arrays.fill(password, '\0'); // clear it
                    newU.setUsername(usernamef);
                    JOptionPane.showMessageDialog(signUp,
                        "Your Username: " + usernamef,
                        "Account Created!",
                        JOptionPane.INFORMATION_MESSAGE);
                    signUp.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(signUp, "Password does not match confirmation", "Error", JOptionPane.WARNING_MESSAGE);
                    }
                });
                

                signUp.add(fNTxt, s);
                s.gridy = 1;
                signUp.add(firstName, s);
                s.gridy = 2;
                signUp.add(lNTxt, s);
                s.gridy = 3;
                signUp.add(lastName, s);
                s.gridy = 4;
                signUp.add(pTxt, s);
                s.gridy = 5;
                signUp.add(makePass, s);
                s.gridy = 6;
                signUp.add(pCTxt, s);
                s.gridy = 7;
                signUp.add(confPass, s);

                s.anchor = GridBagConstraints.CENTER;
                s.fill = GridBagConstraints.NONE;
                s.gridy = 8;
                signUp.add(submit, s);
                


                signUp.setSize(400, 300);
                signUp.setLocationRelativeTo(parent);
                signUp.setVisible(true);
            }
        });
        /*
        
        */
    }
}