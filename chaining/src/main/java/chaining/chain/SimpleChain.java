package chaining.chain;

import helpers.handlers.LambdaHelper;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

public class SimpleChain extends Chain {
    private Iterator<byte[]> vectorIterator;

    public SimpleChain(byte[] initialVector, ChainItem... chainItems) {
        super(initialVector, chainItems);
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) {
        System.out.println("SimpleChain - encrypt");
        reset();
        for (ChainItem chainItem : this) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().encrypt(openDataIS, encryptedDataOS)
                    )
            );
        }
        System.out.println("SimpleChain - encrypt - done");
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) {
        System.out.println("SimpleChain - decrypt");
        reset();
        for (ChainItem chainItem : this) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().decrypt(encryptedDataIS, openDataOS)
                    )
            );
        }
        System.out.println("SimpleChain - decrypt - done");
    }

    @Override
    public byte[] nextEncryptionVector() {
        System.out.println("SimpleChain is providing next encryption vector");
        return vectorIterator.next();
    }

    @Override
    public byte[] nextDecryptionVector() {
        System.out.println("SimpleChain is providing next decryption vector");
        return nextEncryptionVector();
    }

    @Override
    public void setNextBlockVector(byte[] vector) {
        System.out.println("New vector wants to be added to SimpleChain: " + Arrays.toString(vector));
        vectors.add(vector);
    }

    @Override
    public void reset() {
        System.out.println("Resetting SimpleChain");
        vectorIterator = vectors.iterator();
        for (ChainItem chainItem : this) {
            chainItem.getBlockCrypter().reset();
        }
    }
}