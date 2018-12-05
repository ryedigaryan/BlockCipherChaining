package rub.labs.ui;

import javax.swing.*;

public class ChaincryptMainApp extends JFrame {

    public static void main(String[] args) {
        ChaincryptMainApp ex = new ChaincryptMainApp();
        ex.setVisible(true);
    }

    public ChaincryptMainApp() {
        setTitle("Chaincrypt - by Ruddy");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    @Override
    protected void frameInit() {
        super.frameInit();
//        UIManager.getColor()
    }
}
