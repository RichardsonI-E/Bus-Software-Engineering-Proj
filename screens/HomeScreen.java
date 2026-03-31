package screens;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;

public class HomeScreen extends JPanel{
    //create placeholder objects for the parent's cardlayout and container
    private CardLayout cl;
    private JPanel container;
    //Used to track the amount of Stops currently on the homepage screen
    private int stopCount = 0;
    java.util.List<JComponent[]> stops = new java.util.ArrayList<>();

    //Universal font to be used for Hamburger menu
    Font subTitle = new Font("Arial", Font.BOLD, 20);

    //function that is set to call after add/remove button is pressed
    private void updateButtons(JButton addStop, JButton removeStop) {
            removeStop.setVisible(stopCount > 0);//if amount of stops is 0, it is not visible
            addStop.setVisible(stopCount < 3);//if amount of stops is at least 3, it is not visible
        }
    public HomeScreen(JFrame parent, CardLayout cl, JPanel container){
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout()); //sets the layout of the screen to Border

        //defines tab that holds hamburger button and Title of the screen
        JPanel topTab = new JPanel();
        topTab.setLayout(new BorderLayout());
        topTab.setAlignmentX(Component.CENTER_ALIGNMENT);
        topTab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //Title of the given screen, aligned to the center of the tab
        JLabel currentPage = new JLabel("Home");
        currentPage.setFont(new Font("Arial", Font.BOLD, 24));
        currentPage.setHorizontalAlignment(JLabel.CENTER);

        //creates an image for the hamburger icon (text does not work properly) and sets its proper size
        ImageIcon hamburger = new ImageIcon("resources/Hamburger_icon.png");
        Image scaled = hamburger.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHamburger = new ImageIcon(scaled);

        //creates the Hamburger button, removing java's traditional button style
        JButton mockHamburger = new JButton(scaledHamburger);
        mockHamburger.setBorderPainted(false);
        mockHamburger.setContentAreaFilled(false);
        mockHamburger.setFocusPainted(false);

        //Defines the hamburger menu, setting individual entries and subtitle font style
        JPopupMenu hbMenu = new JPopupMenu();

        JMenuItem a1 = new JMenuItem("View Map");
        a1.setFont(new Font("Arial", Font.BOLD, 18));
        a1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JMenuItem a2 = new JMenuItem("Account Settings");
        a2.setFont(new Font("Arial", Font.BOLD, 18));
        a2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JMenuItem a3 = new JMenuItem("Manage Buses");
        a3.setFont(new Font("Arial", Font.BOLD, 18));
        a3.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JMenuItem a4 = new JMenuItem("Manage Stations");
        a4.setFont(new Font("Arial", Font.BOLD, 18));
        a4.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JMenuItem a5 = new JMenuItem("Logout");
        a5.setFont(new Font("Arial", Font.BOLD, 18));
        a5.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        hbMenu.add(a1);
        hbMenu.add(a2);
        hbMenu.add(a3);
        hbMenu.add(a4);
        hbMenu.add(a5);

        //if manage Stations/Buttons is pressed, display a warning that the user does not have permission to view the page
        a3.addActionListener(e ->{
            JOptionPane.showMessageDialog(this,
                "You do not have permission to access this page. Please contact an administrator to request permission change.",
                "Invalid Permissions", JOptionPane.WARNING_MESSAGE);
        });

        a4.addActionListener(e ->{
            JOptionPane.showMessageDialog(this,
                "You do not have permission to access this page. Please contact an administrator to request permission change.",
                "Invalid Permissions", JOptionPane.WARNING_MESSAGE);
        });

        a5.addActionListener((actionEvent) -> {
            cl.show(container, "login");
        });
        //set the default size of the hamburger menu
        hbMenu.setPreferredSize(new Dimension(200, hbMenu.getPreferredSize().height));

        //show the hamburger menu when the button is pressed
        mockHamburger.addActionListener(e -> {
            hbMenu.show(mockHamburger, 0, mockHamburger.getHeight());
        });
        mockHamburger.setAlignmentX(Component.LEFT_ALIGNMENT); //align the button to the left

        //add hamburger button and title to the top tab
        topTab.add(mockHamburger, BorderLayout.WEST);
        topTab.add(currentPage, BorderLayout.CENTER);



        //add placeholder for the history/summary panel to the bottom of page
        JPanel history = new JPanel();
        history.setBackground(Color.yellow);
        history.setAlignmentX(Component.CENTER_ALIGNMENT);
        history.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));


        //"Work in Progress" message for history panel
        JLabel wipMsg = new JLabel("Recent Routes/Summary of current route will go here");
        wipMsg.setFont(new Font("Arial", Font.BOLD, 20));
        history.add(wipMsg);

        //create the main center panel
        JPanel content = new JPanel();


        //title for the main content panel
        JLabel title = new JLabel("Create a new Route");
        title.setFont(subTitle);

        //add main form to the content panel, setting layout to gridbag
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
        form.setOpaque(false);


        //Entry for departure station, a combobox that will be used to search the database
        JLabel departure = new JLabel("Departure Station:");
        departure.setAlignmentY(Component.CENTER_ALIGNMENT);
        JComboBox departureStation = new JComboBox();
        departureStation.setEditable(true);
        departureStation.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));

        //button to add a stop to the user's route
        JButton addStop = new JButton("Add Stop");
        addStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        addStop.setBackground(Color.GREEN);
        addStop.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));

        //button to remove a stop from the user's route
        JButton removeStop = new JButton("Remove Stop");
        removeStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeStop.setBackground(Color.RED);
        removeStop.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));

        //listener for the remove stop button
        removeStop.addActionListener(e -> {
            if (stopCount == 0) return; //abort if there are no stops

            JComponent[] lastStop = stops.remove(stops.size() - 1);//remove the last entry in the stops list

            form.remove(lastStop[0]); // remove label
            form.remove(lastStop[1]); // remove combo box

            stopCount--;//stop count decreases by 1

            updateButtons(addStop, removeStop);//call update buttons

            //recreate the page with updated layout afterwards
            form.revalidate();
            form.repaint();
        });

        //listener for add stop button
        addStop.addActionListener(e -> {
            if(stopCount >= 3) return; //if there are 3+ stops, abort
            //create a new label and (editable)combobox (label determined by stop count)
            JLabel stopLabel = new JLabel("Stop " + (char)('A' + stopCount) + ":");
            JComboBox<String> stopBox = new JComboBox<>();
            stopBox.setEditable(true);
            //place label and combobox at the next row in the gridbag layout
            f.gridy++;
            form.add(stopLabel, f);
            f.gridy++;
            form.add(stopBox, f);

            //add stop to the list and increase stop count
            stops.add(new JComponent[]{stopLabel, stopBox});
            stopCount++;

            updateButtons(addStop, removeStop);//call update buttons

            //recreate the page with updated layout afterwards
            form.revalidate();
            form.repaint();
        });

        //Entry for arrival station, a combobox that will be used to search the database
        JLabel arrival = new JLabel("Arrival Station:");
        arrival.setAlignmentY(Component.CENTER_ALIGNMENT);
        JComboBox arrivalStation = new JComboBox();
        arrivalStation.setEditable(true);
        arrivalStation.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));


        //add all elements to the form
        form.add(departure, f);

        f.gridy = 1;
        form.add(departureStation, f);


        f.gridy = 2;
        form.add(arrival, f);

        f.gridy = 3;
        form.add(arrivalStation, f);

        f.gridy = 4;
        form.add(addStop, f);

        f.gridy = 5;
        form.add(removeStop, f);

        //set the layout of main content panel and add the title and form to it
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(title);
        content.add(form);

        //set size of top and bottom tabs
        topTab.setPreferredSize(new Dimension(0, 60));
        history.setPreferredSize(new Dimension(0, 100));

        //add 3 panels to the homescreen
        add(topTab, BorderLayout.NORTH);
        add(history, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }
}
