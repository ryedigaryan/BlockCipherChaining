package cryptoalgo.caesar.shifter;

public class LetterShifter implements ByteShifter {

    private byte minLetter;
    private byte lettersCount;

    public LetterShifter(byte minLetter, byte lettersCount) {
        this.minLetter = minLetter;
        this.lettersCount = lettersCount;
    }

    public byte shift(byte letter, byte shift) {
        int result = letter + shift;
        result = minLetter + ((result - minLetter) % lettersCount);
        return (byte) result;
    }
}
