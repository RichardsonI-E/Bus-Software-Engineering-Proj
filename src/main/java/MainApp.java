import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import javafx.embed.swing.JFXPanel;
import screens.BManageScreen;
import screens.HomeScreen;
import screens.LoginScreen;
import screens.MapScreen;
import screens.SManageScreen;
import screens.SettingsScreen;
import screens.UManageScreen;

/*This class serves as the main executable for our bus planning system.
It utilizes a CardLayout to switch between tabs/screens that are defined in the screens package (with the
exception of the disclaimer)
 */
class StartInterface {
    public static void main(String[] args) throws IOException {
        new JFXPanel(); // initialize javafx to prevent later crash

        // Create a new java swing window to start the program
        JFrame app = new JFrame("Group 6: Bus Planner");

        // creates layout that allows swapping between tabs/screens
        CardLayout layout = new CardLayout();
        // creates container that holds all screens for the program
        JPanel container = new JPanel(layout);

        // ------------------------------------Screens-------------------------
        JPanel startScreen = new JPanel(new GridBagLayout());
        // creates a container to hold the startup screen

        JLabel disclaimer = new JLabel(
                "THIS SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSES");

        disclaimer.setFont(new Font("Arial", Font.BOLD, 20));

         // Set the JLabel to align to the center of the screen
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);

         // create and add disclaimer to start screen for software as per instructions
        startScreen.add(disclaimer);

        // refer to LoginScreen.java
        LoginScreen loginScreen = new LoginScreen(
                app, layout, container);

        // refer to MapScreen.java
        MapScreen mapScreen = new MapScreen(
                app, layout, container);

        // refer to HomeScreen.java
        HomeScreen homeScreen = new HomeScreen(
                app, layout, container, mapScreen);

        // refer to SettingsScreen.java
        SettingsScreen settingsScreen = new SettingsScreen(
                app, layout, container);

        // refer to SManageScreen.java
        SManageScreen sManageScreen = new SManageScreen(
                app, layout, container, mapScreen);

        // refer to BManageScreen.java
        BManageScreen bManageScreen = new BManageScreen(
                app, layout, container);

        // refer to UManageScreen.java
        UManageScreen uManageScreen = new UManageScreen(
                app, layout, container);

        // add all screens to the cardLayout
        container.add(startScreen, "start");
        container.add(loginScreen, "login");
        container.add(homeScreen, "home");
        container.add(mapScreen, "map");
        container.add(settingsScreen, "settings");
        container.add(sManageScreen, "sManage");
        container.add(bManageScreen, "bManage");
        container.add(uManageScreen, "uManage");

        app.add(container); // add the container to the JFrame

        // set the window size, appearance and exit on close
        app.setSize(800, 600);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);

        // Timer switches to the login screen after 3 seconds
        Timer start = new Timer(3000, e -> {
            layout.show(container, "login");
        });
        start.setRepeats(false);
        start.start();
    }
}
