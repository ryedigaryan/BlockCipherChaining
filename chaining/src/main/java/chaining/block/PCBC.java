package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

public class PCBC<K> extends BlockCrypter<K> {

    public PCBC(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
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
