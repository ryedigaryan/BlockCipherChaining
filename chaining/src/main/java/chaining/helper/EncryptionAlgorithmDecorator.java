package chaining.helper;

import cryptoalgo.EncryptionAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class EncryptionAlgorithmDecorator<K> extends EncryptionAlgorithm<K> {
    private EncryptionAlgorithm<K> rootAlgorithm;

    public EncryptionAlgorithmDecorator(EncryptionAlgorithm<K> rootAlgorithm) {
        this.rootAlgorithm = rootAlgorithm;
    }

    @Override
    public void setKey(K key) {
        rootAlgorithm.setKey(key);
    }

    @Override
    public K getKey() {
        return rootAlgorithm.getKey();
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        rootAlgorithm.encrypt(openDataIS, encryptedDataOS);
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        rootAlgorithm.decrypt(encryptedDataIS, openDataOS);
    }
}
