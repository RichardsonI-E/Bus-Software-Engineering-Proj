package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

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

/*
This class uses javafx to emulate html and js code in order
to display a web map of the world, the user can view the current stations in
the database on the map (blue for bus, yellow for refuel), and the user is
automatically redirected here to view their route once submitted
*/
public class MapScreen extends JPanel {
    //get cardlayout and container from parent
    private final CardLayout cl;
    private final JPanel container;

    //declare summary panel and list of stations in route
    private final SummaryPanel summaryPanel = new SummaryPanel();
    private final List<Station> route = new ArrayList<>();

    //create a javafx panel and webengine to launch map
    private JFXPanel content;
    private WebEngine engine;

    //variable to determine if map has been activated
    private boolean mapInitialized = false;

    // Constructor
    public MapScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());

        //create map version of top tab and add to pane
        topTab tTab = new topTab("Map", cl, container, this);
        add(tTab, BorderLayout.NORTH);

        content = new JFXPanel();

        //layer the pane so summary can overlay the map
        JLayeredPane layeredPane = createLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        // Initialize map when screen becomes visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                removeComponentListener(this);
                SwingUtilities.invokeLater(() -> initializeMap());
            }
        });
    }

    //method to draw the user route if it has been submitted
    public void setRoute(List<Station> route, RoutePlanner planner) {
        this.route.clear();
        this.route.addAll(route);

        summaryPanel.updateSummary(planner);

        if (mapInitialized) {
            refreshMap();
        }
    }

    //public method so other classes can refresh the map
    public void refreshStations() {
        updateStations();
    }

    private JLayeredPane createLayeredPane() {
        JLayeredPane layeredPane = new JLayeredPane();

        //set map as man content with summary as a palette
        layeredPane.add(content, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(summaryPanel, JLayeredPane.PALETTE_LAYER);

        //redefine content and summary position when content is resized
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = layeredPane.getWidth();
                int h = layeredPane.getHeight();

                content.setBounds(0, 0, w, h);
                summaryPanel.setBounds(10, h - 170, 250, 150);
            }
        });

        return layeredPane;
    }

    //start the map in content panel
    private void initializeMap() {
        //wrap in runlater to prevent early launch
        Platform.runLater(() -> {
            WebView webView = new WebView();
            engine = webView.getEngine();

            //load the content as HTML
            Scene scene = new Scene(webView);
            webView.prefWidthProperty().bind(scene.widthProperty());
            webView.prefHeightProperty().bind(scene.heightProperty());

            engine.loadContent(getHTML());

            content.setScene(scene);

            //wait until engine is loaded before starting up map
            engine.getLoadWorker().stateProperty().addListener((obs, o, n) -> {
                if (n == javafx.concurrent.Worker.State.SUCCEEDED) {
                    mapInitialized = true;
                    waitForMap();
                }
            });
        });
    }

    private void waitForMap() {
        //delay method
        Platform.runLater(() -> {
            try {
                Object ready = engine.executeScript("window.javaMapReady === true");

                //if the window is ready to load map, start process
                if (Boolean.TRUE.equals(ready)) {
                    updateStations();
                    refreshMap();
                } else {
                    retry();
                }

            } catch (Exception e) {
                retry();
            }
        });
    }

    //method to wait before trying to load map again
    private void retry() {
        new Timer(100, e -> {
            ((Timer) e.getSource()).stop();
            waitForMap();
        }).start();
    }

    private void updateStations() {
        //if the engine/map hasnt loaded, abort
        if (!mapInitialized || engine == null)
            return;

        Platform.runLater(() -> {
            try {
                StringBuilder js = new StringBuilder();

                //rewrite layer that shows stations in js
                js.append("""
                            if (window.stationLayer) {
                                map.removeLayer(window.stationLayer);
                            }
                            window.stationLayer = L.layerGroup().addTo(map);
                        """);

                //loop through stations
                for (Station s : StationManager.getStations()) {
                    //if station is refuel type, set color as yelllow
                    String color = (s instanceof Station.RefuelStation) ? "yellow" : "blue";

                    //add markers for station on the map with respective
                    //location and name
                    js.append(String.format("""
                                L.circleMarker([%f, %f], {
                                    color: '%s',
                                    radius: 6
                                }).addTo(window.stationLayer)
                                .bindPopup('%s');
                            """,
                            s.getLatitude(),
                            s.getLongitude(),
                            color,
                            escapeJS(s.getName())));
                }

                //execute the string as js code
                engine.executeScript(js.toString());
            } catch (Exception ignored) {
            }
        });
    }

    //method to update map if parameters have changed
    private void refreshMap() {
        //abort if map/engine isn't loaded or less than 2 stations in route
        if (!mapInitialized || engine == null || route.size() < 2)
            return;

        Platform.runLater(() -> {
            StringBuilder js = new StringBuilder();

            //remove and rewrite layer that holds route
            js.append("""
                        if (window.routeLine) {
                            map.removeLayer(window.routeLine);
                        }
                    """);

            js.append("window.routeLine = L.polyline([");

            //for each station in route, add as point in route
            for (int i = 0; i < route.size(); i++) {
                Station s = route.get(i);
                js.append(String.format("[%f, %f]", s.getLatitude(), s.getLongitude()));
                if (i < route.size() - 1)
                    js.append(",");
            }

            //add the route line in green and set map bounds to the route size
            js.append("], {color: 'green'}).addTo(map);");
            js.append("map.fitBounds(window.routeLine.getBounds());");

            //execute the string as js code
            engine.executeScript(js.toString());
        });
    }

    //add escape to prevent error in station name
    private String escapeJS(String s) {
        return s.replace("'", "\\'");
    }

    //method that saves custom html code for engine to run
    private String getHTML() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.3/leaflet.css"/>
                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

                    <style>
                        html, body {
                            margin:0;
                            padding:0;
                            width:100%;
                            height:100%;
                        }

                        #map {
                            width:100%;
                            height:100%;
                            position:absolute;
                        }

                        #coords {
                            position:absolute;
                            top:10px;
                            right:10px;
                            background:rgba(255,255,255,0.9);
                            padding:6px 12px;
                            border-radius:6px;
                            font-size:12px;
                            font-family:Arial;
                            box-shadow:0 2px 6px rgba(0,0,0,0.3);
                            z-index:1000;
                            pointer-events:none;
                        }
                    </style>
                </head>

                <body>
                    <div id="coords">Lat: -- , Long: --</div>
                    <div id="map"></div>

                    <script>
        //--------------set the window view to the center of the U.S.
                        window.map = L.map('map').setView([44, -100], 5);

        //--------------add attribution to corner of the content pane
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: 'Map data © OpenStreetMap'
                        }).addTo(map);

        //--------------Add listener to find coordinates mouse is located
                        map.on('mousemove', function(e) {
                            const lat = e.latlng.lat.toFixed(5);
                            const lng = e.latlng.lng.toFixed(5);

        //------------------show the found coordinates in the pane
                            document.getElementById('coords').innerHTML =
                                "Lat: " + lat + " | Long: " + lng;
                        });

        //--------------declare the map is ready to run
                        window.javaMapReady = true;
                    </script>
                </body>
                </html>
                """;
    }
}