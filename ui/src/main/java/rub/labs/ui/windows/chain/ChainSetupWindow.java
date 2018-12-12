package rub.labs.ui.windows.chain;

import rub.labs.chaining.block.BlockCrypter;
import rub.labs.chaining.block.CBC;
import rub.labs.chaining.block.CFB;
import rub.labs.chaining.block.ECB;
import rub.labs.chaining.block.OFB;
import rub.labs.chaining.block.PCBC;
import rub.labs.chaining.chain.Chain;
import rub.labs.chaining.utils.FileReaderKeyProvider;
import rub.labs.chaining.utils.RandomNumberKeyProvider;
import rub.labs.cryptoalgo.byteshuffle.BasicShifter;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.function.Consumer;

public class ChainSetupWindow extends JFrame {
    public JPanel rootPanel;
    private JComboBox<String> blockTypes;
    private JList<String> nodesList;
    private DefaultListModel<String> nodesListModel = new DefaultListModel<>();
    private JTextField keysFilePath;
    private JTextField executionCount;
    private JButton addButton;
    private JCheckBox generateRandomKeysCheckBox;
    private JButton finishButton;

    public Chain chain = new Chain();

    public ChainSetupWindow() {
        add(rootPanel);
        setSize(600, 300);
        setResizable(false);

        nodesList.setModel(nodesListModel);

        addButton.addActionListener(e -> {
            try {
                String nodeInfo = "";
                final Chain.Node<Integer> node = new Chain.Node<>();
                String holder;

                // setup block crypter
                BlockCrypter<Integer> bc = blockCrypterForName(holder = String.valueOf(blockTypes.getSelectedItem()));
                if(handleNull(bc, "Unknown block algorithm: " + holder)) {
                    return;
                }
                node.setBlockCrypter(bc);
                nodeInfo += "[" + holder + "]";

                // setup key provider
                if (handleNull(holder = keysFilePath.getText(), "Specify keys file path or check \"Generate Random Keys\" checkbox")) {
                    return;
                }
                final File keysFile = new File(holder);
                if(generateRandomKeysCheckBox.isSelected()) {
                    node.setKeyProvider(new RandomNumberKeyProvider());
                    nodeInfo += "[RandomKeys]";
                }
                else {
                    node.setKeyProvider(new FileReaderKeyProvider<>(keysFile, Integer::new));
                    nodeInfo += "[" + keysFile.getAbsoluteFile() + "]";
                }

                // setup execution count
                if(handleNull(holder = executionCount.getText(), "Specify execution count!"))
                    return;
                node.setExecutionCount(Integer.valueOf(holder));
                nodeInfo += "[" + holder + "]";

                // add node into chain
                chain.addNode(node);
                // put info into nodes list
                nodesListModel.addElement(nodeInfo);

            } catch (Throwable t) {
                handleError(t);
            }


        });
        generateRandomKeysCheckBox.addActionListener(e -> {
            keysFilePath.setEnabled(!keysFilePath.isEnabled());
        });
        finishButton.addActionListener(e -> {
            readyChainConsumer.accept(chain);
            dispatchEvent(new WindowEvent(ChainSetupWindow.this, WindowEvent.WINDOW_CLOSING));
        });
    }

    private BlockCrypter<Integer> blockCrypterForName(String name){
        final BlockCrypter<Integer> bc;
        switch (name) {
            case "CBC":
                bc = new CBC<>();
                break;
            case "CFB":
                bc = new CFB<>();
                break;
            case "ECB":
                bc = new ECB<>();
                break;
            case "OFB":
                bc = new OFB<>();
                break;
            case "PCBC":
                bc = new PCBC<>();
                break;
          default:
              bc = null;
        }
        if(bc == null)
            return null;
        bc.setAlgorithm(new BasicShifter());
        return bc;
    }

    private void handleError(Throwable t) {
        JOptionPane.showMessageDialog(rootPanel, t.getMessage(), t.getClass().getSimpleName().replace("Exception", "").replace("Error", ""), JOptionPane.ERROR_MESSAGE);
    }

    private void showError(String errorMessage) {
        JOptionPane.showMessageDialog(rootPanel, errorMessage, "ERROR!", JOptionPane.ERROR_MESSAGE);
    }

    private boolean handleNull(Object obj, Object message) {
        if(obj == null) {
            showError(message.toString());
            return true;
        }
        return false;
    }

    private Consumer<Chain> readyChainConsumer;

    public void setReadyChainConsumer(Consumer<Chain> readyChainConsumer) {
        this.readyChainConsumer = readyChainConsumer;
    }
}
