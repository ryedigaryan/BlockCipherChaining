package chaining.chain.item;

import chaining.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;

import java.util.function.Consumer;

public class ChainItem<KeyType> {
    private BlockCrypter<KeyType> blockCrypter;
    private int executionCount;

    public ChainItem(BlockCrypter<KeyType> blockCrypter, int executionCount, BlockCrypterKeyProvider<KeyType> keyProvider) {
        blockCrypter.setKeyProvider(keyProvider);
        this.blockCrypter = blockCrypter;
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
}
