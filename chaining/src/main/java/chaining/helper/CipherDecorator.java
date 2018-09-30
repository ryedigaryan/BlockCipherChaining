package chaining.helper;

import cryptoalgo.Cipher;

public abstract class CipherDecorator implements Cipher {
    Cipher cipher;

    public CipherDecorator(Cipher decorated) {
        cipher = decorated;
    }

//    public byte[] encrypt(byte[] message) {
//        return cipher.encrypt(message);
//    }
//
//    public byte[] decrypt(byte[] encrypted) {
//        return cipher.decrypt(encrypted);
//    }
}
