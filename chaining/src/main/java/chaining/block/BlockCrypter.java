package chaining.block;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterKeyProvider;
import chaining.helper.BlockCrypterVectorProvider;
import cryptoalgo.EncryptionAlgorithm;

public abstract class BlockCrypter<K> extends EncryptionAlgorithm<K> {
    protected EncryptionAlgorithm<K> algorithm;

    protected BlockCrypterKeyProvider<K> keyProvider;
    protected BlockCrypterVectorProvider vectorProvider;
    protected BlockCrypterDelegate delegate;

    private int cryptableBlockSize;

    public BlockCrypter(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        this.algorithm = rootAlgorithm;
        this.cryptableBlockSize = cryptableBlockSize;
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
