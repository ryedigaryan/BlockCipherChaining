package chaining.block;

import chaining.helper.Modifier;
import chaining.helper.ReverseModifier;
import chaining.helper.Utils;
import cryptoalgo.EncryptionAlgorithm;

public class CBC<K> extends BlockCrypter<K> {

    private class EncryptionModifier implements Modifier {
        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            return Utils.xor(data, vector);
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            delegate.setNextBlockVector(data);
            return data;
        }
    }

    private final EncryptionModifier eMod = new EncryptionModifier();
    private final ReverseModifier dMod = new ReverseModifier(eMod);

    public CBC(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
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