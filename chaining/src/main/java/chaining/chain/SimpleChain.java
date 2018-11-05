package chaining.chain;

import helpers.handlers.LambdaHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
    @SuppressWarnings("unchecked")
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        state_DELETE_ME_FUCK = true;
        vectors = new ArrayList<>(blocksCount);
        System.out.println("SimpleChain - encrypt");
        reset();
        while (openDataIS.available() > 0) {
            for (ChainItem chainItem : this) {
                chainItem.getBlockCrypter().reset();
            }
            for (ChainItem chainItem : this) {
                for(int execNum = 0; execNum < chainItem.getExecutionCount(); execNum++) {
                    if (openDataIS.available() > 0) {
                        chainItem.getBlockCrypter().encrypt(chainItem.getKeyProvider().nextKey(), openDataIS, encryptedDataOS);
                    }
                }
                chainItem.onEachExecution(
                        LambdaHelper.rethrowAsError((key) -> {
                                    if (openDataIS.available() > 0) {
                                        chainItem.getBlockCrypter().encrypt(key, openDataIS, encryptedDataOS);
                                    }
                                }
                        )
                );
            }
        }
        System.out.println("SimpleChain - encrypt - done");
    }

    boolean state_DELETE_ME_FUCK;

    @Override
    @SuppressWarnings("unchecked")
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        state_DELETE_ME_FUCK = false;
        System.out.println("SimpleChain - decrypt");
        reset();
        while (encryptedDataIS.available() > 0) {
            for (ChainItem chainItem : this) {
                chainItem.getBlockCrypter().reset();
            }
            for (ChainItem chainItem : this) {
                chainItem.onEachExecution(
                        LambdaHelper.rethrowAsError((key) ->
                                chainItem.getBlockCrypter().decrypt(key, encryptedDataIS, openDataOS)
                        )
                );
            }
        }
        System.out.println("SimpleChain - decrypt - done");
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
        if(state_DELETE_ME_FUCK)
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