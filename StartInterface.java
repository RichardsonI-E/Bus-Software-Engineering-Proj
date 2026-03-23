import java.awt.*;
import javax.swing.*;
import screens.LoginScreen;

class StartInterface{
    public static void main(String[] args) {
        JFrame app = new JFrame(); //Create a new java swing window to start the program

        CardLayout layout = new CardLayout(); //creates layout that allows swapping between tabs/screens
        JPanel container = new JPanel(layout);

        //----------------------------Screens---------------------------------------------------------------
        JPanel startScreen = new JPanel(new GridBagLayout());
        //creates a container to hold the startup screen (just the disclaimer for now)
        JLabel disclaimer = new JLabel("THIS SOFTWARE IS NOT TO BE USED FOR ROUTE PLANNING PURPOSES");
        disclaimer.setFont(new Font("Arial", Font.BOLD, 20));
        disclaimer.setAlignmentX(Component.CENTER_ALIGNMENT);
        startScreen.add(disclaimer); //create and add disclaimer to start screen for software as per instructions

        LoginScreen loginScreen = new LoginScreen();//refer to LoginScreen.java

        JPanel homeScreen = new JPanel();
        //creates a container to hold the home screen

        JPanel mapScreen = new JPanel();
        //creates a container to hold the map screen

        JPanel sManageScreen = new JPanel();
        //creates a container to hold the screen for managers to manage stations

        JPanel bManageScreen = new JPanel();
        //creates a container to hold the screen for managers to manage buses

        //To add: more screens (usermanage, routeSummary, etc.)

        //add all screens to the cardLayout
        container.add(startScreen, "start");
        container.add(loginScreen, "login");
        container.add(homeScreen, "home");
        container.add(mapScreen, "map");
        container.add(sManageScreen, "smanage");
        container.add(bManageScreen, "bmanage");

        app.add(container);

        app.setSize(800, 600);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        Timer start = new Timer(3000, e ->{
                layout.show(container, "login");
            });
        start.setRepeats(false);
        start.start();
    }
}