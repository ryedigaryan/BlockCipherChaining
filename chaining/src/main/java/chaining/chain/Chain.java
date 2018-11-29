package chaining.chain;

import chaining.block.BlockCrypter;
import chaining.utils.BlockCrypterKeyProvider;
import chaining.utils.Resettable;
import cryptoalgo.Cipher;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Chain implements Cipher, Supplier<byte[]>, Consumer<byte[]> {

    @Getter @Setter
    private byte[] initialVector;

    @Getter @Setter
    private List<Node> nodes;

    private byte[] lastGeneratedVector;
    private Supplier<byte[]> vectorSupplier;

    public Chain() {}

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
        vectorSupplier = new InitialVectorSupplier();

        while (openDataIS.available() > 0) {
            for (Node node : nodes) {
                node.encrypt(openDataIS, encryptedDataOS);
            }
        }

        vectorSupplier = null;
        System.out.println("Chain - encrypt - done");
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        vectorSupplier = new InitialVectorSupplier();

        while (encryptedDataIS.available() > 0) {
            for (Node node : nodes) {
                node.decrypt(encryptedDataIS, openDataOS);
            }
        }

        vectorSupplier = null;
        System.out.println("Chain - decrypt - done");
    }

    @Override
    public void accept(byte[] vector) {
        lastGeneratedVector = vector;
    }

    @Override
    public byte[] get() {
        return vectorSupplier.get();
    }

    ///////////////////////////////////
    ///////////Inner classes///////////
    ///////////////////////////////////

    @Getter @Setter
    public static class Node<K> implements Cipher, Resettable {
        private BlockCrypter<K> blockCrypter;
        private BlockCrypterKeyProvider<K> keyProvider;
        private int executionCount;

        public Node(BlockCrypter<K> blockCrypter, int executionCount) {
            this.blockCrypter = blockCrypter;
            this.executionCount = executionCount;
        }

        @Override
        public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
            for(int i = 0; i < executionCount; i++) {
                if(openDataIS.available() > 0) {
                    blockCrypter.encrypt(keyProvider.get(), openDataIS, encryptedDataOS);
                }
            }
        }

        @Override
        public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
            for(int i = 0; i < executionCount; i++) {
                if(encryptedDataIS.available() > 0) {
                    blockCrypter.decrypt(keyProvider.get(), encryptedDataIS, openDataOS);
                }
            }
        }

        @Override
        public void reset() {
            keyProvider.reset();
        }
    }

    private class InitialVectorSupplier implements Supplier<byte[]> {
        @Override
        public byte[] get() {
            vectorSupplier = new LastGeneratedVectorSupplier();
            return initialVector;
        }
    }

    private class LastGeneratedVectorSupplier implements Supplier<byte[]> {
        @Override
        public byte[] get() {
            return lastGeneratedVector;
        }
    }
}
