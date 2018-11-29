package chaining.utils;

public interface Modifier {

    byte[] firstModification(byte[] data, byte[] vector, int inputLength);

    byte[] secondModification(byte[] data, byte[] vector, int inputLength);
}
