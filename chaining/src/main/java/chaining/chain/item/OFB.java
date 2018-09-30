package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

public class OFB<K> extends ChainItem<K> {
    public OFB(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        super(blockCrypter, executionCount, keyProvider);
    }
}
