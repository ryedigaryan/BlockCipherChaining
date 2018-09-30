package cryptoalgo;

import cryptoalgo.caesarhelper.ByteShifter;
import cryptoalgo.caesarhelper.LetterShifter;
import cryptoalgo.caesarhelper.NonShifter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CaesarCipher extends EncryptionAlgorithm<Byte> {

    private static final byte UPPERCASE_MIN = 'A';
    private static final byte UPPERCASE_MAX = 'Z';
    private static final byte LOWERCASE_MIN = 'a';
    private static final byte LOWERCASE_MAX = 'z';
    private static final byte KEY_MIN       = 0; // included
    private static final byte KEY_MAX       = 'z' - 'a' + 1; // excluded
    private static final byte SYMBOL_MAX    = KEY_MAX;

    private static final ByteShifter upperShifter = new LetterShifter(UPPERCASE_MIN, SYMBOL_MAX);
    private static final ByteShifter lowerShifter = new LetterShifter(LOWERCASE_MIN, SYMBOL_MAX);
    private static final ByteShifter nonShifter   = new NonShifter();

    public CaesarCipher(long key) {
        super.setKey(normalizeKey(key));
    }

    @Override
    public void setKey(Byte key) {
        super.setKey(normalizeKey(key));
    }

    /**
     * {@inheritDoc}
     */
    public void encrypt(InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        encrypt(openDataIS, encryptedDataOS, key);
    }


    /**
     * {@inheritDoc}
     */
    public void decrypt(InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        encrypt(encryptedDataIS, openDataOS, decryptionKey());
    }

    /**
     * Constructs decryption key from encryption key.
     * @return key, used in decryption.
     */
    private byte decryptionKey() {
        return (byte)(SYMBOL_MAX - key);
    }

    /**
     * Normalizes given key, and returns normalized.<br>
     *     In other words, this function fits key in range [0, 26)
     * @param key the key, which must be normalized.
     * @return normalized key.
     */
    private static byte normalizeKey(long key) {
        if(KEY_MIN <= key && key < KEY_MAX)
            return (byte)key;
        // convert key to fit in acceptable range
        long inRange = key % KEY_MAX;
        // if key is negative, then convert it to positive
        if(inRange < 0) {
            return (byte)(KEY_MAX + inRange);
        }
        return (byte)inRange;
    }

    /**
     * Encrypts given input using given key, and writes the encrypted data into given output.
     * @param input input, which must be encrypted
     * @param output output, where the encrypted data must be written
     * @param key encryption key
     * @throws IOException input and output streams can throw this exception
     */
    private static void encrypt(InputStream input, OutputStream output, byte key) throws IOException {
        int openByte;
        while((openByte = input.read()) != -1) {
            byte encryptedByte = shift((byte)openByte, key);
            output.write(encryptedByte);
        }
    }

    /**
     * @param letter letter which must be encrypted
     * @param positiveShift must be in range 0..25
     */
    private static byte shift(byte letter, byte positiveShift) {
        // pick up shifter logic
        ByteShifter shifter;
        if(isUpperCase(letter))
            shifter = upperShifter;
        else if(isLowerCase(letter))
            shifter = lowerShifter;
        else
            shifter = nonShifter;
        // apply shifter logic
        return shifter.shift(letter, positiveShift);
    }

    /**
     * Checks whether the symbol is uppercase english letter or not.
     * @param symbol symbol, which must be checked.
     * @return true - if {@code symbol} is uppercase english letter, false - otherwise
     */
    private static boolean isUpperCase(byte symbol) {
        return UPPERCASE_MIN <= symbol && symbol <= UPPERCASE_MAX;
    }

    /**
     * Checks whether the symbol is lower english letter or not.
     * @param symbol symbol, which must be checked.
     * @return true - if {@code symbol} is lower english letter, false - otherwise
     */
    private static boolean isLowerCase(byte symbol) {
        return LOWERCASE_MIN <= symbol && symbol <= LOWERCASE_MAX;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(obj instanceof CaesarCipher) {
            return key.equals(((CaesarCipher)obj).key);
        }
        return false;
    }
}
