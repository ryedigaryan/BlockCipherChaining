package chaining.block;

import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class OFB<K> extends BlockCrypter<K> {

    private byte[] previousOpenData;

    public OFB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        previousOpenData = openData;
        return vector;
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        delegate.setNextBlockVector(encryptedData);
        return Utils.xor(encryptedData, previousOpenData);
    }
}
