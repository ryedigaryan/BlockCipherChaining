package chaining.chain;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterVectorProvider;
import cryptoalgo.Cipher;
import helpers.handlers.LambdaHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Chain implements Iterable<ChainItem>, Cipher, BlockCrypterVectorProvider, BlockCrypterDelegate {
    private int currentVectorNumber;
    private ArrayList<byte[]> vectors;
    private ChainItem[] chainItems;

    public Chain(byte[] initialVector, ChainItem... chainItems) {
        this.chainItems = chainItems;
        // 1st(at 0 index) is the initialVector
        int blocksCount = 1 + Arrays.stream(chainItems)
                .mapToInt(chainItem -> {
                    chainItem.getBlockCrypter().setVectorProvider(this);
                    chainItem.getBlockCrypter().setDelegate(this);
                    return chainItem.getExecutionCount();
                })
                .sum();
        vectors = new ArrayList<>(blocksCount);
        vectors.add(initialVector);
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) {
        System.out.println("Chain - encrypt");
        reset();
        for (ChainItem chainItem : chainItems) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().encrypt(openDataIS, encryptedDataOS)
                    )
            );
        }
        System.out.println("Chain - encrypt - done");
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) {
        System.out.println("Chain - decrypt");
        reset();
        for (ChainItem chainItem : this) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().decrypt(encryptedDataIS, openDataOS)
                    )
            );
        }
        System.out.println("Chain - decrypt - done");
    }

    @Override
    public byte[] nextEncryptionVector() {
        System.out.println("Chain is providing next encryption vector: " + (1 + currentVectorNumber));
        return vectors.get(++currentVectorNumber);
    }

    @Override
    public byte[] nextDecryptionVector() {
        System.out.println("Chain is providing next decryption vector: " + (1 - currentVectorNumber));
        return nextEncryptionVector();
    }

    @Override
    public void setNextBlockVector(byte[] vector) {
        System.out.println("New vector wants to be added to Chain: " + Arrays.toString(vector));
        vectors.add(vector);
    }

    @Override
    public void reset() {
        System.out.println("Resetting Chain");
        currentVectorNumber = 0;
        for (ChainItem chainItem : chainItems) {
            chainItem.getBlockCrypter().reset();
        }
    }

    @Override
    public Iterator<ChainItem> iterator() {
        return Arrays.asList(chainItems).iterator();
    }
}