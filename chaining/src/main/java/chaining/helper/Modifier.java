package chaining.helper;

public interface Modifier {

    byte[] firstModification(byte[] data, byte[] vector, int inputLength);

    byte[] secondModification(byte[] data, byte[] vector, int inputLength);
}
