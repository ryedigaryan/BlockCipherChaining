package cryptoalgo;

public abstract class EncryptionAlgorithm<KeyType> implements Cipher {

    KeyType key;

    public void setKey(KeyType key) {
        this.key = key;
    }

    public KeyType getKey() {
        return key;
    }
}
