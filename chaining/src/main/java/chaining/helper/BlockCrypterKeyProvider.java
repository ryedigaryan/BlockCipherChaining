package chaining.helper;

public interface BlockCrypterKeyProvider<K> {
    K getKey(int blockNumber);
}
