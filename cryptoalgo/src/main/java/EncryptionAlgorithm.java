public abstract class EncryptionAlgorithm<KeyType> implements Cipher {

    KeyType key;

    EncryptionAlgorithm(KeyType key) {
        setKey(key);
    }

    public void setKey(KeyType key) {
        this.key = key;
    }

    public KeyType getKey() {
        return key;
    }
}
