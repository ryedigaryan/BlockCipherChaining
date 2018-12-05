package rub.labs.cryptoalgo.caesar;

import rub.labs.cryptoalgo.caesar.shifter.ByteShifter;
import rub.labs.cryptoalgo.caesar.shifter.LetterShifter;
import rub.labs.cryptoalgo.caesar.shifter.NonShifter;

class Utils {

    private static final ByteShifter upperShifter = new LetterShifter(Constants.UPPERCASE_MIN, Constants.SYMBOL_MAX);
    private static final ByteShifter lowerShifter = new LetterShifter(Constants.LOWERCASE_MIN, Constants.SYMBOL_MAX);
    private static final ByteShifter nonShifter   = new NonShifter();

    /**
     * @param letter letter which must be encrypted
     * @param positiveShift must be in range 0..25
     */
    static byte shift(byte letter, byte positiveShift) {
        // pick up shifter logic
        ByteShifter shifter;
        if(isUpperCase(letter))
            shifter = upperShifter;
        else if(isLowerCase(letter))
            shifter = lowerShifter;
        else
            shifter = nonShifter;
        // apply shifter logic
        return shifter.shift(letter, positiveShift);
    }

    /**
     * Checks whether the symbol is uppercase english letter or not.
     * @param symbol symbol, which must be checked.
     * @return true - if {@code symbol} is uppercase english letter, false - otherwise
     */
    private static boolean isUpperCase(byte symbol) {
        return Constants.UPPERCASE_MIN <= symbol && symbol <= Constants.UPPERCASE_MAX;
    }

    /**
     * Checks whether the symbol is lower english letter or not.
     * @param symbol symbol, which must be checked.
     * @return true - if {@code symbol} is lower english letter, false - otherwise
     */
    private static boolean isLowerCase(byte symbol) {
        return Constants.LOWERCASE_MIN <= symbol && symbol <= Constants.LOWERCASE_MAX;
    }
}
