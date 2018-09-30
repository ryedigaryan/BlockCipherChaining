package chaining.helper;

import chaining.ChainItem;

import java.util.function.Supplier;

public abstract class ChainItemProvider implements Supplier<ChainItem> {

    /**
     * Start supplying ChainItems from beginning.
     */
    public abstract void restart();

}
