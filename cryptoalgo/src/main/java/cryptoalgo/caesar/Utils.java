package cryptoalgo.caesar;

import cryptoalgo.caesar.shifter.ByteShifter;
import cryptoalgo.caesar.shifter.LetterShifter;
import cryptoalgo.caesar.shifter.NonShifter;

import static cryptoalgo.caesar.Constants.LOWERCASE_MAX;
import static cryptoalgo.caesar.Constants.LOWERCASE_MIN;
import static cryptoalgo.caesar.Constants.SYMBOL_MAX;
import static cryptoalgo.caesar.Constants.UPPERCASE_MAX;
import static cryptoalgo.caesar.Constants.UPPERCASE_MIN;

class Utils {

    private static final ByteShifter upperShifter = new LetterShifter(UPPERCASE_MIN, SYMBOL_MAX);
    private static final ByteShifter lowerShifter = new LetterShifter(LOWERCASE_MIN, SYMBOL_MAX);
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
        return UPPERCASE_MIN <= symbol && symbol <= UPPERCASE_MAX;
    }

    /**
     * Checks whether the symbol is lower english letter or not.
     * @param symbol symbol, which must be checked.
     * @return true - if {@code symbol} is lower english letter, false - otherwise
     */
    private static boolean isLowerCase(byte symbol) {
        return LOWERCASE_MIN <= symbol && symbol <= LOWERCASE_MAX;
    }
}
