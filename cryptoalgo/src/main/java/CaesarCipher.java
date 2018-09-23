public class CaesarCipher extends EncryptionAlgorithm<Byte> {

    private static final byte UPPERCASE_MIN = 'A';
    private static final byte LOWERCASE_MIN = 'a';
    private static final byte UPPERCASE_MAX = 'Z';
    private static final byte LOWERCASE_MAX = 'z';
    private static final byte KEY_MAX       = 'z' - 'a' + 1;
    private static final byte SYMBOL_MAX    = KEY_MAX;

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
     *
     * @param bytes
     * @param shift in range 0..25
     * @return
     */
    private static byte[] shift(byte[] bytes, byte shift) {
        if(shift < 0) {
            shift = (byte) (SYMBOL_MAX + shift);
        }
        byte[] shifted = new byte[bytes.length];
        for (int i = 0, bytesLength = bytes.length; i < bytesLength; i++) {
            byte plainByte = bytes[i];
            int shiftedByte;
            byte min;
            if(isUpperCase(plainByte))
                min = UPPERCASE_MIN;
            else if(isLowerCase(plainByte))
                min = LOWERCASE_MIN;
            else {
                shifted[i] = plainByte;
                continue;
            }
            shiftedByte = plainByte + shift;
            shiftedByte = min + ((shiftedByte - min) % SYMBOL_MAX);
            shifted[i] = (byte)shiftedByte;
        }
        return shifted;
    }

    private static boolean isUpperCase(byte symbol) {
        return UPPERCASE_MIN <= symbol && symbol <= UPPERCASE_MAX;
    }

    private static boolean isLowerCase(byte symbol) {
        return LOWERCASE_MIN <= symbol && symbol <= LOWERCASE_MAX;
    }

    // tests
    public static void main(String[] args) {
        CaesarCipher cc = new CaesarCipher(30L);
        byte[] encrypted = cc.encrypt("abcd efgh ijkl mnop qrst uvw xyz ABCD EFGH IJKL MNOP QRST UVW XYZ".getBytes());
        System.out.println("encrypted: " + new String(encrypted));
        byte[] decrypted = cc.decrypt(encrypted);
        System.out.println("decrypted: " + new String(decrypted));
    }
}
