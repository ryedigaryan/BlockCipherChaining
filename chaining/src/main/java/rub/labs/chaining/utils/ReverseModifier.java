package rub.labs.chaining.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ReverseModifier implements Modifier {

    private Modifier modifier;

    @Override
    public byte[] firstModification(byte[] data, byte[] vector, int inputLength) {
        return modifier.secondModification(data, vector, inputLength);
    }

    @Override
    public byte[] secondModification(byte[] data, byte[] vector, int inputLength) {
        return modifier.firstModification(data, vector, inputLength);
    }
}
