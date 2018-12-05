package rub.labs.chaining.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderKeyProvider<K> implements BlockCrypterKeyProvider<K>, AutoCloseable {

    private BufferedReader fileReader;
    // file which holds keys
    private final File keyFile;
    private final Converter<String, K> stringToKeyConverter;


    public FileReaderKeyProvider(File keyFile, Converter<String, K> stringToKeyConverter) throws FileNotFoundException {
        this.keyFile = keyFile;
        this.stringToKeyConverter = stringToKeyConverter;
        fileReader = new BufferedReader(new FileReader(keyFile));
    }

    @Override
    public K get() {
        try {
            return stringToKeyConverter.convert(fileReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        try {
            fileReader.close();
            fileReader = new BufferedReader(new FileReader(keyFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        fileReader.close();
    }
}
