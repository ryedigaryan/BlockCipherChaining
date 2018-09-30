package chaining.helper;

public interface BlockCrypterKeyProvider<KeyType> {
    KeyType getKey(int blockNumber);
}
