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
        int readBytes;
        int blockSize = getCryptableBlockSize();
        byte[] block = new byte[blockSize];
        setKey(keyProvider.nextKey());
        if((readBytes = openDataIS.read(block)) == -1) {
            throw new IllegalStateException("BlockCrypter.encrypt called when openDataInputStream has no more elements");
        }
        else if(readBytes == blockSize) {
            super.encrypt(new ByteArrayInputStream(block), encryptedDataOS);
        }
        else {
            super.encrypt(new ByteArrayInputStream(block, 0, readBytes), encryptedDataOS);
        }
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        int readBytes;
        int blockSize = getCryptableBlockSize();
        byte[] block = new byte[blockSize];
        setKey(keyProvider.nextKey());
        if((readBytes = encryptedDataIS.read(block)) == -1) {
            throw new IllegalStateException("BlockCrypter.decrypt called when openDataInputStream has no more elements");
        }
        else if(readBytes == blockSize) {
            super.decrypt(new ByteArrayInputStream(block), openDataOS);
        }
        else {
            super.decrypt(new ByteArrayInputStream(block, 0, readBytes), openDataOS);
        }
    }

    @Override
    public void reset() {
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
