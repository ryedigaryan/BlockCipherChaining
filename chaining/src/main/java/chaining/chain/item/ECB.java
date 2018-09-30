package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

public class ECB<K> extends ChainItem<K> {
    public ECB(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        super(blockCrypter, executionCount, keyProvider);
    }
}