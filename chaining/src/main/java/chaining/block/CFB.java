package chaining.block;

import chaining.helper.Modifier;
import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class CFB<K> extends BlockCrypter<K> {

    private class EncryptionModifier implements Modifier {

        private byte[] plainText;

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            plainText = data;
            return vector;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            byte[] result = Utils.xor(data, plainText);
            delegate.setNextBlockVector(result);
            return result;
        }
    }

    private class DecryptionModifier implements Modifier {

        private byte[] cipherText;

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            cipherText = data;
            return vector;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            delegate.setNextBlockVector(cipherText);
            return Utils.xor(data, cipherText);
        }
    }

    private final EncryptionModifier eMod = new EncryptionModifier();
    private final DecryptionModifier dMod = new DecryptionModifier();

    public CFB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }

    @Override
    protected Modifier encryptionModifier() {
        return eMod;
    }

    @Override
    protected Modifier decryptionModifier() {
        return dMod;
    }
}
