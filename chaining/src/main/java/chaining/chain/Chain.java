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

        // ask each node to make enscription till there is any available data
        for (int i = 0; i < nodes.size() && openDataIS.available() > 0; i++) {
            nodes.get(i).encrypt(openDataIS, encryptedDataOS);
        }

        System.out.println("Chain - encrypt - DONE");
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        System.out.println("Chain - decrypt - START");

        lastGeneratedVector = initialVector;

        // ask each node to make description till there is any available data
        for(int i = 0; i < nodes.size() && encryptedDataIS.available() > 0; i++) {
            nodes.get(i).decrypt(encryptedDataIS, openDataOS);
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
        private int executionCount;

        @Override
        public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
            for(int i = 0; i < executionCount && openDataIS.available() > 0; i++) {
                blockCrypter.encrypt(keyProvider.get(), openDataIS, encryptedDataOS);
            }
        }

        @Override
        public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
            for(int i = 0; i < executionCount && encryptedDataIS.available() > 0; i++) {
                blockCrypter.decrypt(keyProvider.get(), encryptedDataIS, openDataOS);
            }
        }

        @Override
        public void reset() {
            keyProvider.reset();
        }
    }
}
