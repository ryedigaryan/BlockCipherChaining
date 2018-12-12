package rub.labs.ui.windows.main;

import rub.labs.chaining.chain.Chain;
import rub.labs.ui.windows.chain.ChainSetupWindow;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainWindow extends JFrame {
    private JPanel rootPanel;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton setupChainButton;
    private JTextField outputFilePathTextField;
    private JTextField inputFilePathTextField;
    private JTextField initialVectorTextField;

    private Chain chain;

    public MainWindow() {
        add(rootPanel);
        setSize(rootPanel.getPreferredSize());
        setResizable(false);
        setTitle("Chaincrypt - by Ruddy");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        encryptButton.addActionListener(e -> {
            try (InputStream is = new FileInputStream(inputFilePathTextField.getText());
                 OutputStream os = new FileOutputStream(outputFilePathTextField.getText())) {
                System.out.println("Encrypting: " + inputFilePathTextField.getText() +
                        " into: " + outputFilePathTextField.getText());
                setEnabled(false);
                chain.setInitialVector(initialVectorTextField.getText().getBytes());
                chain.encrypt(is, os);
                showSuccess("DONE: encryption");
                setEnabled(true);
            } catch (Exception error) {
                System.out.println("ERROR");
                error.printStackTrace();
                handleError(error);
            }
        });
        decryptButton.addActionListener(e -> {
            try (InputStream is = new FileInputStream(inputFilePathTextField.getText());
                 OutputStream os = new FileOutputStream(outputFilePathTextField.getText())) {
                System.out.println("Decrypting: " + inputFilePathTextField.getText() +
                        " into: " + outputFilePathTextField.getText());
                setEnabled(false);
                chain.setInitialVector(initialVectorTextField.getText().getBytes());
                chain.decrypt(is, os);
                showSuccess("DONE: decryption");
                setEnabled(true);
            } catch (IOException error) {
                System.out.println("ERROR");
                error.printStackTrace();
                handleError(error);
            }
        });
        setupChainButton.addActionListener(e -> {
            ChainSetupWindow setupWindow = new ChainSetupWindow();
            MainWindow.this.setEnabled(false);
            setupWindow.setVisible(true);
            setupWindow.setReadyChainConsumer(readyChain -> chain = readyChain);
            setupWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    MainWindow.this.setEnabled(true);
                }
            });
        });
    }

    private void handleError(Throwable t) {
        JOptionPane.showMessageDialog(rootPanel, t.getMessage(), t.getClass().getSimpleName().replace("Exception", "").replace("Error", ""), JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(Object msg) {
        JOptionPane.showMessageDialog(rootPanel, msg.toString(), "Success!", JOptionPane.INFORMATION_MESSAGE);
    }
}
