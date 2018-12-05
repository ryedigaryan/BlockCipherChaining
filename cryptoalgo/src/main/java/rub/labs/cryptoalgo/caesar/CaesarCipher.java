package rub.labs.cryptoalgo.caesar;

import rub.labs.cryptoalgo.EncryptionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CaesarCipher extends EncryptionAlgorithm<Byte> {
    //
    // EncryptionAlgorithm
    //
    @Override
    protected void applyEncryptionAlgorithm(Byte eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        int openByte;
        while((openByte = openDataIS.read()) != -1) {
            byte encryptedByte = Utils.shift((byte)openByte, eKey);
            encryptedDataOS.write(encryptedByte);
        }
    }

    @Override
    protected void applyDecryptionAlgorithm(Byte dKey, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        applyEncryptionAlgorithm(dKey, encryptedDataIS, openDataOS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte decryptionKey(Byte eKey) {
        return normalizeKey(-eKey);
    }

    @Override
    public Byte normalizeKey(Object key) throws IllegalArgumentException {
        if(key instanceof Number) {
            Number keyAsNumber = (Number) key;
            return (byte)Constants.KEY_RANGE.fit(keyAsNumber.longValue());//CaesarCipher.normalizeKey(keyAsNumber.longValue());
        }
        throw new IllegalArgumentException("key must be Number");
    }
}
