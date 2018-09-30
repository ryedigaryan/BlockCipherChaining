package cryptoalgo;

/**
 * Encryption Algorithm base class.
 * @param <K> the type of key of algorithm.
 */
public abstract class EncryptionAlgorithm<K> implements Cipher {

    K key;

    public void setKey(K key) {
        this.key = key;
    }

    public K getKey() {
        return key;
    }
}
