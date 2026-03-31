package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class SettingsScreen extends JPanel{
    private CardLayout cl;
    private JPanel container;
    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    public SettingsScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());

        // --- Top Tab (Hamburger + Title) ---
        JPanel topTab = new JPanel(new BorderLayout());
        topTab.setAlignmentX(Component.CENTER_ALIGNMENT);
        topTab.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        JLabel currentPage = new JLabel("Map View");
        currentPage.setFont(new Font("Arial", Font.BOLD, 24));
        currentPage.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon hamburger = new ImageIcon(getClass().getClassLoader().getResource("Hamburger_icon.png"));
        Image scaled = hamburger.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHamburger = new ImageIcon(scaled);

        JButton mockHamburger = new JButton(scaledHamburger);
        mockHamburger.setBorderPainted(false);
        mockHamburger.setContentAreaFilled(false);
        mockHamburger.setFocusPainted(false);

        // Hamburger menu
        JPopupMenu hbMenu = new JPopupMenu();
        JMenuItem a1 = new JMenuItem("Home");
        a1.setFont(subTitle);
        a1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JMenuItem a2 = new JMenuItem("View Map");
        a2.setFont(subTitle);
        a2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JMenuItem a3 = new JMenuItem("Manage Buses");
        a3.setFont(subTitle);
        a3.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JMenuItem a4 = new JMenuItem("Manage Stations");
        a4.setFont(subTitle);
        a4.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JMenuItem a5 = new JMenuItem("Logout");
        a5.setFont(subTitle);
        a5.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        hbMenu.add(a1);
        hbMenu.add(a2);
        hbMenu.add(a3);
        hbMenu.add(a4);
        hbMenu.add(a5);

        // Button actions
        a1.addActionListener(e -> cl.show(container, "home"));
        a3.addActionListener(e -> JOptionPane.showMessageDialog(this, "You do not have permission to access this page.", "Invalid Permissions", JOptionPane.WARNING_MESSAGE));
        a4.addActionListener(e -> JOptionPane.showMessageDialog(this, "You do not have permission to access this page.", "Invalid Permissions", JOptionPane.WARNING_MESSAGE));
        a5.addActionListener(e -> cl.show(container, "login"));

        hbMenu.setPreferredSize(new Dimension(200, hbMenu.getPreferredSize().height));
        mockHamburger.addActionListener(e -> hbMenu.show(mockHamburger, 0, mockHamburger.getHeight()));
        mockHamburger.setAlignmentX(Component.LEFT_ALIGNMENT);

        topTab.add(mockHamburger, BorderLayout.WEST);
        topTab.add(currentPage, BorderLayout.CENTER);

        add(topTab, BorderLayout.NORTH);
}
}
