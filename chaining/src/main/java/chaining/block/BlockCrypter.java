package chaining.block;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterVectorProvider;
import chaining.helper.Resettable;
import cryptoalgo.EncryptionAlgorithm;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@Getter @Setter
public abstract class BlockCrypter<K> extends EncryptionAlgorithm<K> implements Resettable {
    private EncryptionAlgorithm<K> algorithm;
    private int cryptableBlockSize;
    private BlockCrypterVectorProvider vectorProvider;
    protected BlockCrypterDelegate delegate;

    public BlockCrypter(EncryptionAlgorithm<K> algorithm, int cryptableBlockSize) {
        this.algorithm = algorithm;
        this.cryptableBlockSize = cryptableBlockSize;
    }

    //
    // EncryptionAlgorithm
    //

    /**
     * This class must not do any normalization, because the root encryption {@link #algorithm} will do that when it's
     * necessarily (mainly in {@link EncryptionAlgorithm#encrypt(Object, InputStream, OutputStream)} &
     * {@link EncryptionAlgorithm#decrypt(Object, InputStream, OutputStream)} methods)
     * @param key raw key, which must be normalized
     * @return the same {@code key} which passed as argument
     * @throws IllegalArgumentException if passed {@code key} type is not {@link K}
     */
    @Override
    protected final K normalizeKey(Object key) throws IllegalArgumentException {
        try {
            return (K) key;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e); //TODO: mb e.getMessage() is better?
        }
    }

    /**
     * This class must not do any calculation, because the root encryption {@link #algorithm} will do that when it's
     * needed (mainly in {@link EncryptionAlgorithm#decrypt(Object, InputStream, OutputStream)} method)
     * @param eKey encryption key from which decryption key must be constructed
     * @return the same {@code eKey} which passed as argument
     */
    @Override
    protected final K decryptionKey(K eKey) {
        return eKey;
    }

    @Override
    protected void applyEncryptionAlgorithm(K eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        byte[] vector = vectorProvider.nextEncryptionVector();
        int size = getCryptableBlockSize();
        // prepare input stream
        byte[] inputBlock = new byte[size];
        int length = openDataIS.read(inputBlock);
        inputBlock = modifyInput(inputBlock, vector, length);
        // create OutputStream which will hold all encrypted data
        ByteArrayOutputStream outputBlockOS = new ByteArrayOutputStream(size);
        algorithm.encrypt(eKey, new ByteArrayInputStream(inputBlock), outputBlockOS);
        // write encrypted data into desired output
        byte[] outputBlock = modifyOutput(outputBlockOS.toByteArray(), vector, length);
        encryptedDataOS.write(Arrays.copyOf(outputBlock, length));
        outputBlockOS.close();
    }

    @Override
    protected void applyDecryptionAlgorithm(K dKey, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        byte[] vector = vectorProvider.nextDecryptionVector();
        int size = getCryptableBlockSize();
        // prepare input stream
        byte[] inputBlock = new byte[size];
        int length = encryptedDataIS.read(inputBlock);
        inputBlock = modifyOutput(inputBlock, vector, length);
        // create OutputStream which will hold all encrypted data
        ByteArrayOutputStream outputBlockOS = new ByteArrayOutputStream(size);
        algorithm.decrypt(dKey, new ByteArrayInputStream(inputBlock, 0, length), outputBlockOS);
        // write encrypted data into desired output
        byte[] outputBlock = modifyInput(outputBlockOS.toByteArray(), vector, length);
        openDataOS.write(Arrays.copyOf(outputBlock, length));
        outputBlockOS.close();
    }

    @Override
    public void reset() {
        System.out.println("Resetting " + getClass());
//        vectorProvider.reset();
    }

    //
    //
    //

    protected abstract byte[] modifyInput(byte[] openData, byte[] vector, int inputLength);
    protected abstract byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength);
}