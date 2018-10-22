package chaining.block;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterKeyProvider;
import chaining.helper.BlockCrypterVectorProvider;
import chaining.helper.Resettable;
import cryptoalgo.EncryptionAlgorithm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

abstract public class BlockCrypter<K> extends EncryptionAlgorithm<K> implements Resettable {
    private EncryptionAlgorithm<K> algorithm;
    private int cryptableBlockSize;
    public BlockCrypterKeyProvider<K> keyProvider;
    private BlockCrypterVectorProvider vectorProvider;
    protected BlockCrypterDelegate delegate;

    public BlockCrypter(EncryptionAlgorithm<K> algorithm, int cryptableBlockSize) {
        this.algorithm = algorithm;
        this.cryptableBlockSize = cryptableBlockSize;
    }

    //
    // setters
    //

    public void setAlgorithm(EncryptionAlgorithm<K> algorithm) {
        this.algorithm = algorithm;
    }

    public void setKeyProvider(BlockCrypterKeyProvider<K> keyProvider) {
        this.keyProvider = keyProvider;
    }

    public void setVectorProvider(BlockCrypterVectorProvider vectorProvider) {
        this.vectorProvider = vectorProvider;
    }

    public void setDelegate(BlockCrypterDelegate delegate) {
        this.delegate = delegate;
    }

    public void setCryptableBlockSize(int cryptableBlockSize) {
        this.cryptableBlockSize = cryptableBlockSize;
    }

    //
    // getters
    //

    public EncryptionAlgorithm<K> getAlgorithm() {
        return algorithm;
    }

    public int getCryptableBlockSize() {
        return cryptableBlockSize;
    }

    //
    // EncryptionAlgorithm
    //

    @Override
    final public K normalizeKey(Object key) throws IllegalArgumentException {
        return algorithm.normalizeKey(key);
    }

    @Override
    final public K decryptionKey(K eKey) {
        return algorithm.decryptionKey(eKey);
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
        System.out.println("Resetting BlockCrypter");
        keyProvider.reset();
//        vectorProvider.reset();
    }

    //
    //
    //

    abstract protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength);
    abstract protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength);
}
