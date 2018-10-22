package cryptoalgo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Symmetric Encryption Algorithm base class.<br>
 * This class provides new functions which give ability to pass new key on each encrypt/decrypt operation, but
 * its derive classes must not override {@link Cipher}'s functions, instead of that derives must implement
 * {@link #applyEncryptionAlgorithm(K, InputStream, OutputStream)} and
 * {@link #applyDecryptionAlgorithm(K, InputStream, OutputStream)}
 * and assume that passed {@code key} is already normalized
 *
 * @param <K> the type of key of algorithm.
 */
public abstract class EncryptionAlgorithm<K> implements Cipher {

    /**
     * Encryption Key
     */
    private K eKey;

    /**
     * Decryption Key
     */
    private K dKey;

    /**
     * Setup encryption & decryption keys.
     * @param key encryption key
     */
    public void setKey(K key) {
        this.eKey = normalizeKey(key);
        this.dKey = decryptionKey(this.eKey);
    }

    public K getEncryptionKey() {
        return eKey;
    }

    public K getDecryptionKey() {
        return dKey;
    }

    //
    // Cipher
    //

    @Override
    final public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        applyEncryptionAlgorithm(eKey, openDataIS, encryptedDataOS);
    }

    @Override
    final public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        applyDecryptionAlgorithm(eKey, encryptedDataIS, openDataOS);
    }

    /**
     * Alternative encryption function which can be used for passing custom key on each execution
     *
     * @param key normalized key for encryption
     */
    final public void encrypt(K key, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        normalizeKey(key);
        applyEncryptionAlgorithm(key, openDataIS, encryptedDataOS);
    }

    /**
     * Alternative decryption function which can be used for passing custom key on each execution
     *
     * @param key normalized key for decryption
     */
    final public void decrypt(K key, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        normalizeKey(key);
        applyDecryptionAlgorithm(key, encryptedDataIS, openDataOS);
    }

    /**
     * Normalizes given encryption key for using in future encryption
     * @param key raw key, which must be normalized
     * @return normalized key
     * @throws IllegalArgumentException if {@code key} contains something wrong
     */
    abstract protected K normalizeKey(Object key) throws IllegalArgumentException;

    /**
     * Constructs decryption key from normalized encryption key
     * @param encryptionKey encryption key from which decryption key must be constructed
     * @return decryption key for corresponding {@code encryptionKey}
     */
    abstract protected K decryptionKey(K encryptionKey);

    abstract protected void applyEncryptionAlgorithm(K key, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException;

    abstract protected void applyDecryptionAlgorithm(K key, InputStream encryptedDataIS, OutputStream openDataOS) throws  IOException;

    //
    // Object
    //

    @Override
    public int hashCode() {
        return eKey.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof EncryptionAlgorithm) {
            EncryptionAlgorithm other = (EncryptionAlgorithm)obj;
            if(eKey == other.eKey)
                return true;
            return eKey.equals(other.eKey);
        }
        return false;
    }
}
