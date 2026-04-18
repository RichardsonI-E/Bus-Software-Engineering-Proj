package components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import primary.User;
/*This class acts as a global top panel for each screen, used to indicate
the current screen and navigate to other screens*/
public class topTab extends JPanel {

    public topTab(String title, CardLayout cl, JPanel container, Component parent) {
        //set tab as a border layout and create bottom borderline
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        //create the Title of the current screen
        JLabel currentPage = new JLabel(title);
        currentPage.setFont(new Font("Arial", Font.BOLD, 24));
        currentPage.setHorizontalAlignment(JLabel.CENTER);

        //Create the Hamburger Icon and properly size it
        ImageIcon hamburger = new ImageIcon(getClass().getClassLoader().getResource("Hamburger_icon.png"));
        Image scaled = hamburger.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHamburger = new ImageIcon(scaled);

        //set Hamburger Icon as button and remove traditional button styles
        JButton mockHamburger = new JButton(scaledHamburger);
        mockHamburger.setBorderPainted(false);
        mockHamburger.setContentAreaFilled(false);
        mockHamburger.setFocusPainted(false);
        mockHamburger.setAlignmentX(Component.LEFT_ALIGNMENT); //align to top-left corner

        //Create Hamburger menu and entry Items
        JPopupMenu menu = new JPopupMenu();

        JMenuItem home = createItem("Home");
        JMenuItem map = createItem("View Map");
        JMenuItem buses = createItem("Manage Buses");
        JMenuItem stations = createItem("Manage Stations");
        JMenuItem users = createItem("Manage Users");
        JMenuItem settings = createItem("Account Settings");
        JMenuItem logout = createItem("Logout");

        //if statement string: only add given item if it is not the name of the current screen
        if (!title.equals("Home")) {
            menu.add(home);
        }
        if (!title.equals("Map")) {
            menu.add(map);
        }
        if (!title.equals("Manage Buses")) {
            menu.add(buses);
        }
        if (!title.equals("Manage Stations")) {
            menu.add(stations);
        }
        if (!title.equals("Manage Users")) {
            menu.add(users);
        }
        if (!title.equals("Account Settings")) {
            menu.add(settings);
        }

        menu.add(logout); //logout will always be available as a menu option

        //action listener: show menu when hamburger Icon is pressed
        mockHamburger.addActionListener(e -> {
            menu.show(mockHamburger, 0, mockHamburger.getHeight());
        });

        //action listeners: menu items that don't require special permissions
        /*pressing respective item transfers you to that screen while logout returns
        to login screen and clears current User*/
        home.addActionListener(
                e -> cl.show(container, "home")
        );

        map.addActionListener(
                e -> cl.show(container, "map")
        );

        settings.addActionListener(
                e -> cl.show(container, "settings")
        );

        logout.addActionListener(e -> {
            Session.clearUser();
            cl.show(container, "login");
        });

        //action listeners: Menu items that require specific permissions to allow access
        //if current user is station manager or admin, allow access, otherwise show warning message
        stations.addActionListener(e -> {
            //get the current user from session to check permissions
            User currentUser = Session.getUser();

            if (currentUser.getPerms().equals("admin")
                    || currentUser.getPerms().equals("stationManager")) {
                cl.show(container, "sManage");
            } else {
                JOptionPane.showMessageDialog(this, "You do not have permission to access this page.",
                        "Invalid Permissions", JOptionPane.WARNING_MESSAGE);
            }
        });

        //if current user is bus manager or admin, allow access, otherwise show warning message
        buses.addActionListener(e -> {
            //get the current user from session to check permissions
            User currentUser = Session.getUser();

            if (currentUser.getPerms().equals("admin")
                    || currentUser.getPerms().equals("busManager")) {
                cl.show(container, "bManage");
            } else {
                JOptionPane.showMessageDialog(this, "You do not have permission to access this page.",
                        "Invalid Permissions", JOptionPane.WARNING_MESSAGE);
            }
        });

        //if current user is admin, allow access, otherwise show warning message
        users.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get the current user from session to check permissions
                User currentUser = Session.getUser();
                if (currentUser.getPerms().equals("admin")) {
                    cl.show(container, "uManage");
                } else {
                    JOptionPane.showMessageDialog(topTab.this,
                        "You do not have permission to access this page.",
                        "Invalid Permissions", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        //add the title and hamburger menu to the panel
        add(mockHamburger, BorderLayout.WEST);
        add(currentPage, BorderLayout.CENTER);
    }

    //creates each menu item with appropriate names and borders
    private JMenuItem createItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Arial", Font.PLAIN, 20));
        item.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return item;
    }
}
