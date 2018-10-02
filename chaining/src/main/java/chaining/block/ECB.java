package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ECB<K> extends BlockCrypter<K> {

    public ECB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        int blockSize = getCryptableBlockSize();
        int readBytes;
        byte[] block = new byte[blockSize];
        algorithm.setKey(keyProvider.nextKey());
        if((readBytes = openDataIS.read(block)) == -1) {
            throw new IllegalStateException("BlockCrypter.encrypt called when openDataInputStream has no more elements");
        }
        else if(readBytes == blockSize) {
            algorithm.encrypt(new ByteArrayInputStream(block), encryptedDataOS);
        }
        else {
            algorithm.encrypt(new ByteArrayInputStream(block, 0, readBytes), encryptedDataOS);
        }
    }

    @Override
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        int blockSize = getCryptableBlockSize();
        int readBytes;
        byte[] block = new byte[blockSize];
        algorithm.setKey(keyProvider.nextKey());
        if((readBytes = encryptedDataIS.read(block)) == -1) {
            throw new IllegalStateException("BlockCrypter.encrypt called when openDataInputStream has no more elements");
        }
        else if(readBytes == blockSize) {
            algorithm.decrypt(new ByteArrayInputStream(block), openDataOS);
        }
        else {
            algorithm.decrypt(new ByteArrayInputStream(block, 0, readBytes), openDataOS);
        }
    }
}