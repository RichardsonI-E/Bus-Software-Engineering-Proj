package screens;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import components.topTab;

public class UManageScreen extends JPanel{
    private final CardLayout cl;
    private JPanel container;

    public UManageScreen(JFrame parent, CardLayout cl, JPanel container) {
        this.cl = cl;
        this.container = container;

        setLayout(new BorderLayout());//set page layout as a borderlayout

        topTab tTab = new topTab("Manage Users", cl, container, this);

        add(tTab, BorderLayout.NORTH);
}
}
