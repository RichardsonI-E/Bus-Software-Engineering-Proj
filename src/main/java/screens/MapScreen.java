package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import components.SummaryPanel;
import components.topTab;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import permissions.StationManager;
import primary.RoutePlanner;
import primary.Station;

public class MapScreen extends JPanel {

    private CardLayout cl;
    private JPanel container;

    private MapScreen mapScreen;
    private List<Station> route = new ArrayList<>();
    private SummaryPanel summaryPanel = new SummaryPanel();

    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    private JFXPanel content;
    private boolean mapInitialized = false; // ensure map initializes only once
    private WebEngine engine;

    public void setRoute(List<Station> route, RoutePlanner planner) {
        this.route = route;

        if (mapInitialized) {
            refreshMap();
        }

        summaryPanel.updateSummary(planner);
    }

    private void refreshMap() {
        if (!mapInitialized || engine == null || route == null || route.size() < 2) {
            return;
        }

        Platform.runLater(() -> {
            StringBuilder js = new StringBuilder();

            js.append("""
            if (window.routeLine) {
                map.removeLayer(window.routeLine);
            }
        """);

            js.append("window.routeLine = L.polyline([");

            for (Station s : route) {
                js.append(String.format("[%f, %f],",
                        s.getLatitude(),
                        s.getLongitude()));
            }

            js.append("], {color: 'blue'}).addTo(map);\n");

            js.append("map.fitBounds(window.routeLine.getBounds());");

            engine.executeScript(js.toString());
        });
    }

    public MapScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Map", cl, container, this);

        // --- Content Panel (JavaFX WebView) ---
        content = new JFXPanel();
        content.setLayout(null);
        summaryPanel.setBounds(10, content.getHeight() - 120, 300, 100);
        content.add(summaryPanel);

        // --- Listen for Swing component being shown (important for CardLayout) ---
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Only initialize once
                MapScreen.this.removeComponentListener(this);
                SwingUtilities.invokeLater(() -> initializeMap(content));
            }
        });

        add(tTab, BorderLayout.NORTH);
        JLayeredPane layeredPane = new JLayeredPane();

        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = layeredPane.getWidth();
                int h = layeredPane.getHeight();

                content.setBounds(0, 0, w, h);

                summaryPanel.setBounds(10, h - 170, 250, 150);
            }
        });

        content.setBounds(0, 0, 800, 600); //resize later
        summaryPanel.setBounds(10, 450, 300, 100);

        layeredPane.add(content, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(summaryPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void initializeMap(JFXPanel content) {
        // Ensure JFXPanel is initialized
        Platform.runLater(() -> {
            WebView webView = new WebView();
            this.engine = webView.getEngine();
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
                        window.map = L.map('map').setView([44, -100], 5);

                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: 'Map data © OpenStreetMap'
                        }).addTo(map);

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

            //wait until webview finishes
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                    mapInitialized = true;
                    refreshMap();
                }
            });
        });
    }
}
