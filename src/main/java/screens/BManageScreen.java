package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;

import components.topTab;

public class BManageScreen extends JPanel{
    private CardLayout cl;
    private JPanel container;
    private Font subTitle = new Font("Arial", Font.BOLD, 20);

    public BManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout()); //set page layout as a borderlayout

        topTab tTab = new topTab("Manage Buses", cl, container, this);

        add(tTab, BorderLayout.NORTH);
}
}
