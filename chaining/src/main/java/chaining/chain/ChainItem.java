package chaining.chain;

import chaining.block.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

import java.util.function.Consumer;

public class ChainItem<K> {
    private BlockCrypter<K> blockCrypter;
    private int executionCount;

    public ChainItem(BlockCrypter<K> blockCrypter, int executionCount) {
        this.blockCrypter = blockCrypter;
        this.executionCount = executionCount;
    }

    public ChainItem(BlockCrypter<K> blockCrypter, int executionCount, BlockCrypterKeyProvider<K> keyProvider) {
        this(blockCrypter, executionCount);
        blockCrypter.setKeyProvider(keyProvider);
    }

    public BlockCrypter<K> getBlockCrypter() {
        return blockCrypter;
    }

    public int getExecutionCount() {
        return executionCount;
    }

    public void setBlockCrypter(BlockCrypter<K> blockCrypter) {
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
}
