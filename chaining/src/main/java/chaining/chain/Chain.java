package chaining.chain;

import chaining.helper.BlockCrypterDelegate;
import chaining.helper.BlockCrypterVectorProvider;
import chaining.helper.ImmutableCollection.NonRemovableIterator;
import cryptoalgo.Cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class Chain implements Iterable<ChainItem>, Cipher, BlockCrypterDelegate, BlockCrypterVectorProvider {
    // these fields are protected because derived classes must have access to them
    protected List<byte[]> vectors;
    protected List<ChainItem> chainItems;

    public Chain(byte[] initialVector, ChainItem... chainItems) {
        this.chainItems = Arrays.asList(chainItems);
        // 1st(at 0 index) is the initialVector
        int blocksCount = 1 + Arrays.stream(chainItems)
                .mapToInt(chainItem -> {
                    chainItem.getBlockCrypter().setVectorProvider(this);
                    chainItem.getBlockCrypter().setDelegate(this);
                    return chainItem.getExecutionCount();
                })
                .sum();
        vectors = new ArrayList<>(blocksCount);
        setNextBlockVector(initialVector);
    }

    @Override
    public Iterator<ChainItem> iterator() {
        return new NonRemovableIterator<>(chainItems.iterator());
    }
}
