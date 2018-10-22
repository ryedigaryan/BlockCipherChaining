package cryptoalgo.caesar;

import cryptoalgo.EncryptionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CaesarCipher extends EncryptionAlgorithm<Byte> {
    //
    // EncryptionAlgorithm
    //
    @Override
    protected void applyEncryptionAlgorithm(Byte key, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        int openByte;
        while((openByte = openDataIS.read()) != -1) {
            byte encryptedByte = Utils.shift((byte)openByte, key);
            encryptedDataOS.write(encryptedByte);
        }
    }

    @Override
    protected void applyDecryptionAlgorithm(Byte key, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        applyEncryptionAlgorithm(key, encryptedDataIS, openDataOS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Byte decryptionKey(Byte encryptionKey) {
        return (byte)(Constants.SYMBOL_MAX - encryptionKey);
    }

    @Override
    public Byte normalizeKey(Object key) throws IllegalArgumentException {
        if(key instanceof Number) {
            Number keyAsNumber = (Number) key;
            return CaesarCipher.normalizeKey(keyAsNumber.longValue());
        }
        throw new IllegalArgumentException("key must be Number");
    }

    /**
     * Normalizes given key, and returns normalized.<br>
     *     In other words, this function fits key in range [0, 26)
     * @param key the key, which must be normalized.
     * @return normalized key.
     */
    private static byte normalizeKey(long key) {
        if(Constants.KEY_MIN <= key && key < Constants.KEY_MAX)
            return (byte)key;
        // convert key to fit in acceptable range
        long inRange = key % Constants.KEY_MAX;
        // if key is negative, then convert it to positive
        if(inRange < 0) {
            return (byte)(Constants.KEY_MAX + inRange);
        }
        return (byte)inRange;
    }

}
