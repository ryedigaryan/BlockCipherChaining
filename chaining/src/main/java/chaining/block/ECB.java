package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

public class ECB<K> extends BlockCrypter<K> {

    public ECB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        return openData;
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        return encryptedData;
    }
}