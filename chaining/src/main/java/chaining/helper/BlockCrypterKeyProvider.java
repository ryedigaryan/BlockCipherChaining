package chaining.helper;

public interface BlockCrypterKeyProvider<K> extends Resetable {
    K nextKey();
}
