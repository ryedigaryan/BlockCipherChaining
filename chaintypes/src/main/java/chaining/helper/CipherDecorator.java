package chaining.helper;

import cryptoalgo.Cipher;

public abstract class CipherDecorator implements Cipher {
    Cipher cipher;

    public CipherDecorator(Cipher rootCipher) {
        cipher = rootCipher;
    }

    public byte[] encrypt(byte[] message) {
        return cipher.encrypt(message);
    }

    public byte[] decrypt(byte[] encrypted) {
        return cipher.decrypt(encrypted);
    }
}
