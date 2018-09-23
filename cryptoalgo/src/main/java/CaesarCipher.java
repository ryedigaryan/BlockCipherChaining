import caesarhelper.ByteShifter;
import caesarhelper.LetterShifter;
import caesarhelper.NonShifter;

public class CaesarCipher extends EncryptionAlgorithm<Byte> {

    private static final byte UPPERCASE_MIN = 'A';
    private static final byte UPPERCASE_MAX = 'Z';
    private static final byte LOWERCASE_MIN = 'a';
    private static final byte LOWERCASE_MAX = 'z';
    private static final byte KEY_MAX       = 'z' - 'a' + 1;
    private static final byte SYMBOL_MAX    = KEY_MAX;

    private static final ByteShifter upperShifter = new LetterShifter(UPPERCASE_MIN, SYMBOL_MAX);
    private static final ByteShifter lowerShifter = new LetterShifter(LOWERCASE_MIN, SYMBOL_MAX);
    private static final ByteShifter nonShifter   = new NonShifter();

    public CaesarCipher(Long key) {
        super((byte)(key % KEY_MAX));
    }

    public byte[] encrypt(byte[] message) {
        return shift(message, key);
    }

    public byte[] decrypt(byte[] encrypted) {
        return shift(encrypted, (byte)-key);
    }

    /**
     * @param shift must be in range 0..25
     */
    private static byte[] shift(byte[] bytes, byte shift) {
        if(shift < 0) {
            shift = (byte) (SYMBOL_MAX + shift);
        }
        byte[] result = new byte[bytes.length];
        for (int i = 0, bytesLength = bytes.length; i < bytesLength; i++) {
            byte plainByte = bytes[i];
            ByteShifter shifter;
            if(isUpperCase(plainByte))
                shifter = upperShifter;
            else if(isLowerCase(plainByte))
                shifter = lowerShifter;
            else
                shifter = nonShifter;
            result[i] = shifter.shift(plainByte, shift);
        }
        return result;
    }

    private static boolean isUpperCase(byte symbol) {
        return UPPERCASE_MIN <= symbol && symbol <= UPPERCASE_MAX;
    }

    private static boolean isLowerCase(byte symbol) {
        return LOWERCASE_MIN <= symbol && symbol <= LOWERCASE_MAX;
    }

    // tests
    public static void main(String[] args) {
        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";
        CaesarCipher cc = new CaesarCipher(30L);
        byte[] encrypted = cc.encrypt(plain.getBytes());
        System.out.println("encrypted: " + new String(encrypted));
        assert expect.equals(new String(encrypted)) : "WRONG ENcryption";
        byte[] decrypted = cc.decrypt(encrypted);
        System.out.println("decrypted: " + new String(decrypted));
        assert plain.equals(new String(decrypted)) : "WRONG DEcryption";
    }
}
