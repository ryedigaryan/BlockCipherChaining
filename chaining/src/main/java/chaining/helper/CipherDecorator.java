package chaining.helper;

import cryptoalgo.Cipher;

public abstract class CipherDecorator implements Cipher {
    Cipher cipher;

    public CipherDecorator(Cipher decorated) {
        cipher = decorated;
    }
}
