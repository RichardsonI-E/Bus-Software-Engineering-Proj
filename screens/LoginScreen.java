package screens;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginScreen extends JPanel{
    public LoginScreen(){
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
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 60, 20, 60));
        form.setPreferredSize(new Dimension(500, 250));
        form.setOpaque(true);
        form.setBackground(Color.white);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField user = new JTextField("Username");
        user.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JTextField pass = new JTextField("Password");
        pass.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton login = new JButton("Login");
        login.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel create = new JLabel("Create an Account");
        create.setForeground(Color.BLUE);
        create.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel reset = new JLabel("Request Account Reset");
        reset.setForeground(Color.BLUE);
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(user);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(pass);
        form.add(Box.createRigidArea(new Dimension(0, 15)));
        form.add(login);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(create);
        form.add(reset);

        logContainer.add(form);
        add(logContainer);
    }
}