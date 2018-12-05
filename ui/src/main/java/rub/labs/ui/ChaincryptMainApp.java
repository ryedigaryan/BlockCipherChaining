package rub.labs.ui;

import rub.labs.ui.windows.main.MainWindow;

import javax.swing.*;

public class ChaincryptMainApp {

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainWindow mainWindow = new MainWindow();
        mainWindow.setVisible(true);
    }

}
