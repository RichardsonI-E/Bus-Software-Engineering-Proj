package screens;
import java.awt.BorderLayout;
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
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
public class HomeScreen extends JPanel{
    private int stopCount = 0;
    java.util.List<JComponent[]> stops = new java.util.ArrayList<>();
    Font subTitle = new Font("Arial", Font.BOLD, 20);
    private void updateButtons(JButton addStop, JButton removeStop) {
            removeStop.setVisible(stopCount > 0);
            addStop.setVisible(stopCount < 3);
        }
    public HomeScreen(){
        setLayout(new BorderLayout());

        JPanel topTab = new JPanel();
        topTab.setLayout(new BorderLayout());
        topTab.setAlignmentX(Component.CENTER_ALIGNMENT);
        topTab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JLabel currentPage = new JLabel("Home");
        currentPage.setFont(new Font("Arial", Font.BOLD, 24));
        currentPage.setHorizontalAlignment(JLabel.CENTER);
        

        ImageIcon hamburger = new ImageIcon("resources/Hamburger_icon.png");
        Image scaled = hamburger.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHamburger = new ImageIcon(scaled);
        JButton mockHamburger = new JButton(scaledHamburger);
        mockHamburger.setFont(new Font("Arial", Font.BOLD, 24));
        mockHamburger.setBorderPainted(false);
        mockHamburger.setContentAreaFilled(false);
        mockHamburger.setFocusPainted(false);

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

        hbMenu.setPreferredSize(new Dimension(200, hbMenu.getPreferredSize().height));

        mockHamburger.addActionListener(e -> {
            hbMenu.show(mockHamburger, 0, mockHamburger.getHeight());
        });
        mockHamburger.setAlignmentX(Component.LEFT_ALIGNMENT);
        topTab.add(mockHamburger, BorderLayout.WEST);

        topTab.add(currentPage, BorderLayout.CENTER);


        JPanel history = new JPanel();
        history.setBackground(Color.yellow);
        history.setAlignmentX(Component.CENTER_ALIGNMENT);
        history.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        JLabel wipMsg = new JLabel("Recent Routes/Summary of current route will go here");
        wipMsg.setFont(new Font("Arial", Font.BOLD, 20));
        history.add(wipMsg);

        JPanel content = new JPanel();


        JLabel title = new JLabel("Create a new Route");
        title.setFont(new Font("Arial", Font.BOLD, 20));

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

        JLabel departure = new JLabel("Departure Station:");
        departure.setAlignmentY(Component.CENTER_ALIGNMENT);
        JComboBox departureStation = new JComboBox();
        departureStation.setEditable(true);
        departureStation.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));

        JButton addStop = new JButton("Add Stop");
        addStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        addStop.setBackground(Color.GREEN);
        addStop.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));


        JButton removeStop = new JButton("Remove Stop");
        removeStop.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeStop.setBackground(Color.RED);
        removeStop.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));
        removeStop.addActionListener(e -> {
            if (stopCount == 0) return;

            JComponent[] lastStop = stops.remove(stops.size() - 1);

            form.remove(lastStop[0]); // label
            form.remove(lastStop[1]); // combo box

            stopCount--;

            updateButtons(addStop, removeStop);

            form.revalidate();
            form.repaint();
        });

        addStop.addActionListener(e -> {
            if(stopCount >= 3) return;
            JLabel stopLabel = new JLabel("Stop " + (char)('A' + stopCount) + ":");
            JComboBox<String> stopBox = new JComboBox<>();
            stopBox.setEditable(true);
            f.gridy++;
            form.add(stopLabel, f);
            f.gridy++;
            form.add(stopBox, f);

            stops.add(new JComponent[]{stopLabel, stopBox});
            stopCount++;

            updateButtons(addStop, removeStop);

            form.revalidate();
            form.repaint();
        });

        JLabel arrival = new JLabel("Arrival Station:");
        arrival.setAlignmentY(Component.CENTER_ALIGNMENT);
        JComboBox arrivalStation = new JComboBox();
        arrivalStation.setEditable(true);
        arrivalStation.setMaximumSize(new Dimension(Integer.MAX_VALUE/3, 60));


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


        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(title);
        content.add(form);

        topTab.setPreferredSize(new Dimension(0, 60));
        history.setPreferredSize(new Dimension(0, 100));

        add(topTab, BorderLayout.NORTH);
        add(history, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }
}
