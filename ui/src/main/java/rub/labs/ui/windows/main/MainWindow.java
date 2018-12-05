package rub.labs.ui.windows.main;

import javax.swing.*;

public class MainWindow extends JFrame {
    private JPanel rootPanel;
    private JButton button1;

    public MainWindow() {
        add(rootPanel);
        setSize(1000, 1000);
        setTitle("Chaincrypt - by Ruddy");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
