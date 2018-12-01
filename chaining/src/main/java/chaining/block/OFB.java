package chaining.block;

import chaining.utils.Modifier;
import chaining.utils.Utils;
import cryptoalgo.EncryptionAlgorithm;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OFB<K> extends BlockCrypter<K> {

    private byte[] lastVector;

    private class EncryptionModifier implements Modifier {

        private byte[] plainText;

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            plainText = data;
            return vector;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            return Utils.xor(lastVector = data, plainText);
        }
    }

    private final EncryptionModifier modifier = new EncryptionModifier();

    public OFB(EncryptionAlgorithm<K> rootAlgorithm) {
        super(rootAlgorithm);
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
    public byte[] getLastGeneratedVector() {
        return lastVector;
    }
}
