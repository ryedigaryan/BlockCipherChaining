package chaining.block;

import chaining.chain.Chain;
import chaining.utils.Modifier;
import cryptoalgo.EncryptionAlgorithm;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ECB<K> extends BlockCrypter<K> {

    /**
     * This will hold the value of vector which have been passed to {@code ECB}'s modifier ({@link NonModifier}).<br>
     * {@code ECB} does not use or generate any vector at all. But as it should be able to be used in {@link Chain} with
     * other {@link BlockCrypter}s - different than {@link ECB}, so it should provide the vector, which have been passed
     * to him, from the previous {@code BlockCrypter}
     */
    private byte[] lastVector;

    private class NonModifier implements Modifier {

        @Override
        public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
            return data;
        }

        @Override
        public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
            lastVector = vector;
            return data;
        }
    }

    private NonModifier modifier = new NonModifier();

    public ECB(EncryptionAlgorithm<K> rootAlgorithm) {
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
    protected byte[] getLastGeneratedVector() {
        return lastVector;
    }

}