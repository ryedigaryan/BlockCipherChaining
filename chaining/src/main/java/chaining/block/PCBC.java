package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PCBC<K> extends BlockCrypter<K> {

    public PCBC(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {

    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {

    }
}
