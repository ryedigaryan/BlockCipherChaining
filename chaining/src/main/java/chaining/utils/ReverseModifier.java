package chaining.utils;

public class ReverseModifier implements Modifier {

    private Modifier modifier;

    public ReverseModifier(Modifier rootModifier) {
        modifier = rootModifier;
    }

    @Override
    public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
        return modifier.secondModification(data, vector, inputLength);
    }

    @Override
    public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
        return modifier.firstModification(data, vector, inputLength);
    }
}
