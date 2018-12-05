package rub.labs.chaining.utils;

public class Utils {
    public static byte[] xor(byte[] a, byte[] b) {
        assert a.length == b.length : "xor operands must have same length";

        int min = Math.min(a.length, b.length);
        byte[] r = new byte[min];
        for(int i = 0; i < min; i++) {
            r[i] = (byte) (a[i] ^ b[i]);
        }
        return r;
    }
}
