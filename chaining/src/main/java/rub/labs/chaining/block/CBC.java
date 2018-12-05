package rub.labs.chaining.block;

import rub.labs.chaining.utils.Modifier;
import rub.labs.chaining.utils.ReverseModifier;
import rub.labs.chaining.utils.Utils;
import rub.labs.cryptoalgo.EncryptionAlgorithm;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CBC<K> extends BlockCrypter<K> {

    private byte[] lastVector;

    private class EncryptionModifier implements Modifier {
        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            return Utils.xor(data, vector);
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            lastVector = data;
            return data;
        }
    }

    private final EncryptionModifier eMod = new EncryptionModifier();
    private final ReverseModifier dMod = new ReverseModifier(eMod);

    public CBC(EncryptionAlgorithm<K> rootAlgorithm) {
        super(rootAlgorithm);
    }

    @Override
    protected Modifier encryptionModifier() {
        return eMod;
    }

    @Override
    protected Modifier decryptionModifier() {
        return dMod;
    }

    @Override
    public byte[] getLastGeneratedVector() {
        return lastVector;
    }
}