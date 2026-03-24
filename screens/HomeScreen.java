package screens;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
public class HomeScreen extends JPanel{
    public HomeScreen(){
        setLayout(new BorderLayout());

        JPanel topTab = new JPanel();
        topTab.setBackground(Color.BLUE);

        JPanel history = new JPanel();
        history.setBackground(Color.yellow);

        JPanel content = new JPanel();
        JScrollPane mainContent = new JScrollPane(content);
        mainContent.setBackground(Color.RED);

        add(topTab, BorderLayout.NORTH);
        add(history, BorderLayout.SOUTH);
        add(mainContent, BorderLayout.CENTER);
    }
}
