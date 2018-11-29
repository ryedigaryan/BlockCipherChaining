package cryptoalgo.caesar;

import helpers.Range;

class Constants {
    static final byte UPPERCASE_MIN = 'A';
    static final byte UPPERCASE_MAX = 'Z';
    static final byte LOWERCASE_MIN = 'a';
    static final byte LOWERCASE_MAX = 'z';

    // LOWERCASE_MAX - LOWERCASE_MIN == UPPERCASE_MAX - UPPERCASE_MIN
    static final Range KEY_RANGE    = new Range(0, LOWERCASE_MAX - LOWERCASE_MIN + 1);
    static final byte SYMBOL_MAX    = (byte)KEY_RANGE.getEnd();
}
