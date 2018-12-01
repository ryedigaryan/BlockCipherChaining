package chaining.chain;

import chaining.block.BlockCrypter;
import chaining.utils.BlockCrypterKeyProvider;
import chaining.utils.Resettable;
import cryptoalgo.Cipher;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@NoArgsConstructor
public class Chain implements Cipher, Supplier<byte[]>, Consumer<byte[]>, Resettable {

    @Getter @Setter
    private byte[] initialVector;

    @Getter @Setter
    private List<Node> nodes;

    private byte[] lastGeneratedVector;

    public Chain(byte[] initialVector, Node... nodes) {
        this.initialVector = initialVector;
        this.nodes = new ArrayList<>(Arrays.asList(nodes));

        for (Node node : nodes) {
            node.getBlockCrypter().setVectorProvider(this);
            node.getBlockCrypter().setVectorStorage(this);
        }
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        System.out.println("Chain - encrypt - START");
        lastGeneratedVector = initialVector;

        // we should repeat each node's encryption till there is any available data
        while(openDataIS.available() > 0) {
            for (Node node : nodes) {
                node.encrypt(openDataIS, encryptedDataOS);
            }
        }

        System.out.println("Chain - encrypt - DONE");
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        System.out.println("Chain - decrypt - START");

        lastGeneratedVector = initialVector;

        // we should repeat each node's decryption till there is any available data
        while(encryptedDataIS.available() > 0) {
            for (Node node : nodes) {
                node.decrypt(encryptedDataIS, openDataOS);
            }
        }

        System.out.println("Chain - decrypt - DONE");
    }

    @Override
    public void accept(byte[] vector) {
        lastGeneratedVector = vector;
    }

    @Override
    public byte[] get() {
        return lastGeneratedVector;
    }

    @Override
    public void reset() {
        for (Node node : getNodes()) {
            node.reset();
        }
    }

    ///////////////////////////////////
    ///////////Inner classes///////////
    ///////////////////////////////////

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Node<K> implements Cipher, Resettable {
        private BlockCrypter<K> blockCrypter;
        private BlockCrypterKeyProvider<K> keyProvider;
        private Integer executionCount;

        @Override
        public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
            if(executionCount == -1) {
                // if executionCount is -1 then we should perform encryption until all the data is read
                while (openDataIS.available() > 0) {
                    blockCrypter.encrypt(keyProvider.get(), openDataIS, encryptedDataOS);
                }
            }
            else {
                // if executionCount is not -1, then we should perform only finite number encryption operations
                // and only if there is any available data
                for (int i = 0; i < executionCount && openDataIS.available() > 0; i++) {
                    blockCrypter.encrypt(keyProvider.get(), openDataIS, encryptedDataOS);
                }
            }
        }

        @Override
        public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
            if(executionCount == -1) {
                // if executionCount is -1 then we should perform decryption until all the data is read
                while (encryptedDataIS.available() > 0) {
                    blockCrypter.decrypt(keyProvider.get(), encryptedDataIS, openDataOS);
                }
            }
            else {
                // if executionCount is not -1, then we should perform only finite number decryption operations
                // and only if there is any available data
                for (int i = 0; i < executionCount && encryptedDataIS.available() > 0; i++) {
                    blockCrypter.decrypt(keyProvider.get(), encryptedDataIS, openDataOS);
                }

            }
        }

        @Override
        public void reset() {
            keyProvider.reset();
        }
    }
}
