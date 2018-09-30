package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

public class CFB<K> extends ChainItem<K> {
    public CFB(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        super(blockCrypter, executionCount, keyProvider);
    }
}
