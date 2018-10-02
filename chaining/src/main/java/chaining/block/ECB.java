package chaining.block;

import cryptoalgo.EncryptionAlgorithm;

public class ECB<K> extends BlockCrypter<K> {

    public ECB(EncryptionAlgorithm<K> rootAlgorithm, int cryptableBlockSize) {
        super(rootAlgorithm, cryptableBlockSize);
    }
}