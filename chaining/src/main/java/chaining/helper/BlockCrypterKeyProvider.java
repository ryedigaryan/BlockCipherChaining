package chaining.helper;

public interface BlockCrypterKeyProvider<K> extends Resettable {
    K nextKey();
}
