package chaining.block;

import chaining.helper.Modifier;
import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class OFB<K> extends BlockCrypter<K> {

    private class EncryptionModifier implements Modifier {

        private byte[] plainText;

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            plainText = data;
            return vector;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            delegate.setNextBlockVector(data);
            return Utils.xor(data, plainText);
        }
    }

    private final EncryptionModifier modifier = new EncryptionModifier();

    public OFB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
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
}
