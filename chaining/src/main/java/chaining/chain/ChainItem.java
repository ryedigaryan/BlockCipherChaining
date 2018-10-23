package chaining.chain;

import chaining.block.BlockCrypter;
import chaining.helper.BlockCrypterKeyProvider;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter @Setter
public class ChainItem<K> {
    private BlockCrypter<K> blockCrypter;
    private BlockCrypterKeyProvider<K> keyProvider;
    private int executionCount;

    public ChainItem(BlockCrypter<K> blockCrypter, int executionCount) {
        this.blockCrypter = blockCrypter;
        this.executionCount = executionCount;
    }

    void onEachExecution(Consumer<K> action) {
        int execCount = getExecutionCount();
        while(--execCount >= 0) {
            action.accept(keyProvider.nextKey());
        }
    }
}
