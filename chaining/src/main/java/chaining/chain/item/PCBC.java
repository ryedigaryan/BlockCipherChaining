package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

public class PCBC<K> extends ChainItem<K> {
    public PCBC(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        super(blockCrypter, executionCount, keyProvider);
    }
}
