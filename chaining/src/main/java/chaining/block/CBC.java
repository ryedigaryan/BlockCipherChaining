package chaining.block;

import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class CBC<K> extends BlockCrypter<K> {

    public CBC(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        return Utils.xor(openData, vector);
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        delegate.setNextBlockVector(encryptedData);
        return encryptedData;
    }
}