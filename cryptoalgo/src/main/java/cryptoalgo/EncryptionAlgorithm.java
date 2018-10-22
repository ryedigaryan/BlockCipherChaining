package cryptoalgo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

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
    public void setKey(Object key) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    final public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        Objects.requireNonNull(eKey, "encryption key is null");
        applyEncryptionAlgorithm(eKey, openDataIS, encryptedDataOS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        Objects.requireNonNull(dKey, "decryption key is null");
        applyDecryptionAlgorithm(dKey, encryptedDataIS, openDataOS);
    }

    /**
     * Alternative encryption function which can be used for passing custom key on each execution
     * @param eKey normalized key for encryption
     */
    final public void encrypt(K eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        eKey = normalizeKey(eKey);
        applyEncryptionAlgorithm(eKey, openDataIS, encryptedDataOS);
    }

    /**
     * Alternative decryption function which can be used for passing custom key on each execution
     * @param eKey normalized key for decryption
     */
    final public void decrypt(K eKey, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        eKey = normalizeKey(eKey);
        K dKey = decryptionKey(eKey);
        applyDecryptionAlgorithm(dKey, encryptedDataIS, openDataOS);
    }

    /**
     * Normalizes given encryption key for using in future encryption
     * @param key raw key, which must be normalized
     * @return normalized key
     * @throws IllegalArgumentException if {@code key} contains something wrong
     */
    abstract public K normalizeKey(Object key) throws IllegalArgumentException;

    /**
     * Constructs decryption key from normalized encryption key
     * @param eKey encryption key from which decryption key must be constructed
     * @return decryption key for corresponding {@code encryptionKey}
     */
    abstract public K decryptionKey(K eKey);

    /**
     * The function which actually does encryption. Here can be assumed that passed key meets all the requirements of
     * current algorithm.
     * @param eKey normalized encryption key
     * @param openDataIS open data input stream, which must be encrypted
     * @param encryptedDataOS encrypted data output stream, where encrypted data must be written to
     * @throws IOException read/write exception
     */
    abstract protected void applyEncryptionAlgorithm(K eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException;

    /**
     * The function which actually does decryption. Here can be assumed that passed key meets all the requirements of
     * current algorithm.
     * @param dKey normalized decryption key
     * @param encryptedDataIS encrypted data input stream, which must be decrypted
     * @param openDataOS decrypted data output stream, where decrypted data must be written to
     * @throws IOException read/write exception
     */
    abstract protected void applyDecryptionAlgorithm(K dKey, InputStream encryptedDataIS, OutputStream openDataOS) throws  IOException;

    //
    // Object
    //

    @Override
    public int hashCode() {
        return Objects.hashCode(eKey);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof EncryptionAlgorithm) {
            EncryptionAlgorithm other = (EncryptionAlgorithm)obj;
            return Objects.equals(eKey, other.eKey);
        }
        return false;
    }
}
