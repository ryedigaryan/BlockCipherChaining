package rub.labs.cryptoalgo.caesar.shifter;

public class NonShifter implements ByteShifter {
    public byte shift(byte letter, byte shift) {
        return letter;
    }
}