package chaining.block;

import chaining.utils.Modifier;
import cryptoalgo.EncryptionAlgorithm;

public class ECB<K> extends BlockCrypter<K> {

    private class NonModifier implements Modifier {

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            return data;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            return data;
        }
    }

    private NonModifier modifier = new NonModifier();

    public ECB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected Modifier encryptionModifier() {
        return modifier;
    }

    @Override
    protected Modifier decryptionModifier() {
        return modifier;
    }

    @Override
    protected byte[] getLastGeneratedVector() {
        return null;
    }

}