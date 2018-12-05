package rub.labs.cryptoalgo.byteshuffle;

import rub.labs.cryptoalgo.EncryptionAlgorithm;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//TODO: add support for choosing an encryptable block size in bits
@NoArgsConstructor
public class BasicShifter extends EncryptionAlgorithm<Integer> {

    public BasicShifter(Number key) {
        setKey(key);
    }

    @Override
    protected Integer normalizeKey(Object key) throws IllegalArgumentException {
        if(key instanceof Number) {
            Number keyAsNumber = (Number) key;
            return (int)Constants.KEY_RANGE.fit(keyAsNumber.longValue());
        }
        throw new IllegalArgumentException("key must be Number");
    }

    @Override
    protected Integer decryptionKey(Integer eKey) {
        return normalizeKey(-eKey);
    }

    private byte mask(int count) {
        byte mask = 0;
        for(int i = 1; i <= count; i++) {
            mask <<= 1;
            mask |= 1;
        }
        return mask;
    }

    @Override
    protected void applyEncryptionAlgorithm(Integer eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        int data;
        while((data = openDataIS.read()) >= 0) {
            int frontBits = (data & mask(eKey));
            // 8 is bits count in a byte
            frontBits <<= 8 - eKey;
            data >>= eKey;
            data |= frontBits;
            encryptedDataOS.write(data);
        }
    }

    @Override
    protected void applyDecryptionAlgorithm(Integer dKey, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        applyEncryptionAlgorithm(dKey, encryptedDataIS, openDataOS);
    }
}
