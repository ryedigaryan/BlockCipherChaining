package cryptoalgo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Cipher {

    /**
     * Encrypts data read from {@code openDataIS} and writes the encrypted data into {@code encryptedDataOS}
     * @param openDataIS open data provider
     * @param encryptedDataOS encrypted data handler
     */
    void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException;

    /**
     * Encrypts data read from {@code encryptedDataIS} and writes the encrypted data into {@code openDataOS}
     * @param encryptedDataIS encrypted data provider
     * @param openDataOS decrypted data handler
     */
    void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException;
}