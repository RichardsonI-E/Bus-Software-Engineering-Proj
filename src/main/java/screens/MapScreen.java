package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import components.topTab;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import permissions.StationManager;
import primary.Station;

public class MapScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;
    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    private JFXPanel content;
    private boolean mapInitialized = false; // ensure map initializes only once

    public MapScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Map", cl, container, this);

        // --- Content Panel (JavaFX WebView) ---
        content = new JFXPanel();

        // --- Listen for Swing component being shown (important for CardLayout) ---
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Only initialize once
                content.removeComponentListener(this);
                SwingUtilities.invokeLater(() -> initializeMap(content));
            }
        });

        add(tTab, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initializeMap(JFXPanel content) {
        // Ensure JFXPanel is initialized
        Platform.runLater(() -> {
            WebView webView = new WebView();
            WebEngine engine = webView.getEngine();
            Scene scene = new Scene(webView);
            webView.prefWidthProperty().bind(scene.widthProperty());
            webView.prefHeightProperty().bind(scene.heightProperty());

            //Loop to add all stations in database to the map
            StringBuilder stationMarkers = new StringBuilder();

            for (Station s : StationManager.getStations()) {
                stationMarkers.append(String.format(
                        "L.marker([%f, %f]).addTo(map).bindPopup('%s');\n",
                        s.getLatitude(),
                        s.getLongitude(),
                        s.getName()
                ));
            }

            // HTML content with Leaflet
            String htmlCode = """
            <!DOCTYPE html>
            <html>
            <head>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.3/leaflet.css"/>
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    html, body, #map { margin:0; padding:0; width:100%%; height:100%%; }
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

                        %s

                        // Handle resizing after panel shows
                        window.addEventListener('resize', () => map.invalidateSize());
                        setTimeout(() => map.invalidateSize(), 200);
                    }
                    window.onload = initMap;
                </script>
            </body>
            </html>
            """.formatted(stationMarkers.toString());

            engine.loadContent(htmlCode);
            content.setScene(scene);
        });
    }
}
