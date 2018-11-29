package chaining.utils;

public class Utils {
    public static byte[] xor(byte[] a, byte[] b) {
        assert a.length == b.length : "xor operands must have same length";

        byte[] r = new byte[a.length];
        for(int i = 0; i < a.length; i++) {
            r[i] = (byte) (a[i] ^ b[i]);
        }
        return r;
    }
}
