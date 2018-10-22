package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

public class OFB<K> extends BlockCrypter<K> {

    public OFB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        return new byte[0];
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        return new byte[0];
    }
}
