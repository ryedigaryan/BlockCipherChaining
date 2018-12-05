package rub.labs.chaining.utils;

import java.util.function.Supplier;

public interface BlockCrypterKeyProvider<K> extends Supplier<K>, Resettable {
}
