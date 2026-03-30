package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;
    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    private JFXPanel content;
    private boolean mapInitialized = false; // ensure map initializes only once

    public MapScreen(JFrame parent, CardLayout cl, JPanel container) {
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
        JMenuItem a2 = new JMenuItem("Account Settings");
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

        // --- Content Panel (JavaFX WebView) ---
        content = new JFXPanel();
        add(topTab, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();

        // --- Listen for Swing component being shown (important for CardLayout) ---
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Only initialize once
                content.removeComponentListener(this);
                SwingUtilities.invokeLater(() -> initializeMap(content));
            }
        });
    }

    private void initializeMap(JFXPanel content) {
        // Ensure JFXPanel is initialized
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();
            Scene scene = new Scene(webView);
            webView.prefWidthProperty().bind(scene.widthProperty());
            webView.prefHeightProperty().bind(scene.heightProperty());

            // HTML content with Leaflet
            String htmlCode = """
            <!DOCTYPE html>
            <html>
            <head>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.3/leaflet.css"/>
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    html, body, #map { margin:0; padding:0; width:100%; height:100%; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    function initMap() {
                        var map = L.map('map').setView([34.0, -81.0], 100);
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: 'Map data © OpenStreetMap'
                        }).addTo(map);

                        L.marker([34.0, -81.0]).addTo(map).bindPopup("default");

                        // Handle resizing after panel shows
                        window.addEventListener('resize', () => map.invalidateSize());
                        setTimeout(() => map.invalidateSize(), 200);
                    }
                    window.onload = initMap;
                </script>
            </body>
            </html>
            """;

            engine.loadContent(htmlCode);
            content.setScene(scene);
        });
    }
}
