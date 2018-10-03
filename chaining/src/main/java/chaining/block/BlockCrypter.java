package chaining.block;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterKeyProvider;
import chaining.helper.BlockCrypterVectorProvider;
import chaining.helper.EncryptionAlgorithmDecorator;
import chaining.helper.Resetable;
import cryptoalgo.EncryptionAlgorithm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BlockCrypter<K> extends EncryptionAlgorithmDecorator<K> implements Resetable {
    protected BlockCrypterKeyProvider<K> keyProvider;
    protected BlockCrypterVectorProvider vectorProvider;
    protected BlockCrypterDelegate delegate;

    private int cryptableBlockSize;

    public BlockCrypter(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm);
        this.cryptableBlockSize = cryptableBlockSize;
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        setKey(keyProvider.nextKey());
        if(getCryptableBlockSize() != -1) {
            byte[] block = new byte[getCryptableBlockSize()];
            encrypt(openDataIS, encryptedDataOS, block);
        }
        else {
            super.encrypt(openDataIS, encryptedDataOS);
        }
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        setKey(keyProvider.nextKey());
        if(getCryptableBlockSize() != -1) {
            byte[] block = new byte[getCryptableBlockSize()];
            decrypt(encryptedDataIS, openDataOS, block);
        }
        else {
            super.decrypt(encryptedDataIS, openDataOS);
        }
    }

    private void encrypt(InputStream openDataIS, OutputStream encryptedDataOS, byte[] holder) throws IOException {
        super.encrypt(refactor(openDataIS, holder), encryptedDataOS);
    }

    private void decrypt(InputStream encryptedDataIS, OutputStream openDataOS, byte[] holder) throws IOException {
        super.decrypt(refactor(encryptedDataIS, holder), openDataOS);
    }

    private static InputStream refactor(InputStream stream, byte[] holder) throws IOException {
        int readBytes;
        if((readBytes = stream.read(holder)) == -1) {
            throw new IllegalStateException("BlockCrypter.encrypt/decrypt called when input stream has no more elements");
        }
        if(readBytes == holder.length) {
            return new ByteArrayInputStream(holder);
        }
        return new ByteArrayInputStream(holder, 0, readBytes);
    }

    @Override
    public void reset() {
        System.out.println("Resetting BlockCrypter");
        keyProvider.reset();
    }

    public int getCryptableBlockSize() {
        return cryptableBlockSize;
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
}
