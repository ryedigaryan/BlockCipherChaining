package chaining;

import chaining.helper.BlockCrypterKeyProvider;

import java.util.function.Consumer;

public class ChainItem<KeyType> implements BlockCrypterKeyProvider<KeyType> {
    private BlockCrypter<KeyType> blockCrypter;
    private int executionCount;
    private KeyType[] keys;

    public ChainItem(BlockCrypter<KeyType> blockCrypter, int executionCount, KeyType[] keys) {
        if(keys.length != executionCount) {
            throw new IllegalArgumentException("ChainItem keys.length must be equal to executionCount");
        }
        blockCrypter.setKeyProvider(this);
        this.blockCrypter = blockCrypter;
        this.keys = keys;
        this.executionCount = executionCount;
    }

    public BlockCrypter<KeyType> getBlockCrypter() {
        return blockCrypter;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public void setBlockCrypter(BlockCrypter<KeyType> blockCrypter) {
        this.blockCrypter = blockCrypter;
    }

    public void setExecutionCount(int executionCount) {
        this.executionCount = executionCount;
    }

    public void onEachExecution(Consumer<Integer> action) {
        int execCount = getExecutionCount();
        while(--execCount >= 0) {
            action.accept(execCount);
        }
    }

    public void onEachExecution(Runnable action) {
        int execCount = getExecutionCount();
        while(--execCount >= 0) {
            action.run();
        }
    }

    @Override
    public KeyType getKey(int blockNumber) {
        return keys[blockNumber];
    }
}
