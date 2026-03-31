package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import components.topTab;


public class SManageScreen extends JPanel{
    private CardLayout cl;
    private JPanel container;

    public SManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Manage Stations", cl, container, this);

        add(tTab, BorderLayout.NORTH);
}
}
