package chaining.block;

import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

// order - OK
public class PCBC<K> extends BlockCrypter<K> {

    private byte[] previousOpenData;

    public PCBC(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected byte[] modifyInput(byte[] openData, byte[] vector, int inputLength) {
        previousOpenData = openData;
        return Utils.xor(openData, vector);
    }

    @Override
    protected byte[] modifyOutput(byte[] encryptedData, byte[] vector, int inputLength) {
        delegate.setNextBlockVector(Utils.xor(previousOpenData, encryptedData));
        return encryptedData;
    }
}
