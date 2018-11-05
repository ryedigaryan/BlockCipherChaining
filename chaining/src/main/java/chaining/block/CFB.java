package chaining.block;

import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class CFB<K> extends BlockCrypter<K> {

    private byte[] previousOpenData;

    public CFB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        previousOpenData = openData;
        return vector;
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        byte[] newVector = Utils.xor(encryptedData, previousOpenData);
        delegate.setNextBlockVector(newVector);
        return newVector;
    }
}
