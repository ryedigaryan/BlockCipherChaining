package chaining;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterVectorProvider;
import cryptoalgo.Cipher;
import helpers.handlers.LambdaHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Chain implements BlockCrypterVectorProvider, BlockCrypterDelegate, Cipher {
    private int currentBlockCrypterNumber;
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
        currentBlockCrypterNumber = 0;
        for (ChainItem chainItem : chainItems) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().encrypt(openDataIS, encryptedDataOS)
                    )
            );
        }
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) {
        currentBlockCrypterNumber = 0;
        for (ChainItem chainItem : chainItems) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().decrypt(encryptedDataIS, openDataOS)
                    )
            );
        }
    }

    @Override
    public byte[] nextEncryptionVector() {
        System.out.println("Chain is providing next encryption vector: " + (1 + currentBlockCrypterNumber));
        return vectors.get(++currentBlockCrypterNumber);
    }

    @Override
    public byte[] nextDecryptionVector() {
        System.out.println("Chain is providing next decryption vector: " + (1 - currentBlockCrypterNumber));
        return nextEncryptionVector();
    }

    @Override
    public void setNextBlockVector(byte[] vector) {
        System.out.println("New vector wants to be added to Chain: " + Arrays.toString(vector));
        vectors.add(vector);
    }
}