public interface Cipher {

    byte[] encrypt(byte[] message);

    byte[] decrypt(byte[] encrypted);
}