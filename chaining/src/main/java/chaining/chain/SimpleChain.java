package chaining.chain;

import helpers.handlers.LambdaHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

public class SimpleChain extends Chain {
private Iterator<byte[]> vectorIterator;

    public SimpleChain(byte[] initialVector, ChainItem... chainItems) {
        super(initialVector, chainItems);
    }

    //
    // Cipher's overrides
    //
    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        System.out.println("SimpleChain - encrypt");
        reset();
        for (ChainItem chainItem : this) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().encrypt(chainItem.getBlockCrypter().keyProvider.nextKey(), openDataIS, encryptedDataOS)
                    )
            );
        }
        System.out.println("SimpleChain - encrypt - done");
        openDataIS.close();
        encryptedDataOS.close();
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        System.out.println("SimpleChain - decrypt");
        reset();
        for (ChainItem chainItem : this) {
            chainItem.onEachExecution(
                    LambdaHelper.rethrowAsError(() ->
                            chainItem.getBlockCrypter().decrypt(chainItem.getBlockCrypter().keyProvider.nextKey(), encryptedDataIS, openDataOS)
                    )
            );
        }
        System.out.println("SimpleChain - decrypt - done");
        encryptedDataIS.close();
        openDataOS.close();
    }

    //
    // BlockCrypterVectorProvider's overrides
    //

    @Override
    public byte[] nextEncryptionVector() {
        System.out.println("SimpleChain is providing next encryption vector");
        return vectorIterator.hasNext() ? vectorIterator.next() : null;
    }

    @Override
    public byte[] nextDecryptionVector() {
        System.out.println("SimpleChain is providing next decryption vector");
        return nextEncryptionVector();
    }

    //
    // BlockCrypterDelegate's overrides
    //

    @Override
    public void setNextBlockVector(byte[] vector) {
        System.out.println("New vector wants to be added to SimpleChain: " + Arrays.toString(vector));
        vectors.add(vector);
    }

    //
    // Resettable's overrides
    //

    @Override
    public void reset() {
        System.out.println("Resetting SimpleChain");
        vectorIterator = vectors.iterator();
        for (ChainItem chainItem : this) {
            chainItem.getBlockCrypter().reset();
        }
    }
}