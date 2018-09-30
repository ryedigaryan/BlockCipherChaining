package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

public class CBC<K> extends ChainItem<K> {
    public CBC(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        super(blockCrypter, executionCount, keyProvider);
    }
}
