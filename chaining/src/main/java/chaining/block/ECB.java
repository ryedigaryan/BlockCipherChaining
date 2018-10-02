package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ECB<K> extends BlockCrypter<K> {

    public ECB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        super.encrypt(openDataIS, encryptedDataOS);
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        super.decrypt(encryptedDataIS, openDataOS);
    }
}