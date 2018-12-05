package rub.labs.ui;

import javax.swing.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Simple example");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        MainApp ex = new MainApp();
        ex.setVisible(true);
    }
}
