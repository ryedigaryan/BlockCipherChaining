package cryptoalgo.caesar;

class Constants {
    static final byte UPPERCASE_MIN = 'A';
    static final byte UPPERCASE_MAX = 'Z';
    static final byte LOWERCASE_MIN = 'a';
    static final byte LOWERCASE_MAX = 'z';
    static final byte KEY_MIN       = 0; // included
    static final byte KEY_MAX       = 'z' - 'a' + 1; // excluded
    static final byte SYMBOL_MAX    = KEY_MAX;
}
