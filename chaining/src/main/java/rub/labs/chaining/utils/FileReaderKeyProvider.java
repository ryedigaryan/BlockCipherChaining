package rub.labs.chaining.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

public class FileReaderKeyProvider<K> implements BlockCrypterKeyProvider<K>, AutoCloseable {

    private BufferedReader fileReader;
    // file which holds keys
    private final File keyFile;
    private final Function<String, K> stringToKeyConverter;


    public FileReaderKeyProvider(File keyFile, Function<String, K> stringToKeyConverter) throws FileNotFoundException {
        this.keyFile = keyFile;
        this.stringToKeyConverter = stringToKeyConverter;
        fileReader = new BufferedReader(new FileReader(keyFile));
    }

    @Override
    public K get() {
        try {
            if(!fileReader.ready())
                reset();
            K key = stringToKeyConverter.apply(fileReader.readLine());
            System.out.println(getClass().getSimpleName() + " providing key: " + key);
            return key;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reset() {
        try {
            fileReader.close();
            fileReader = new BufferedReader(new FileReader(keyFile));
            System.out.println(getClass().getSimpleName() + " reset");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        System.out.println(getClass().getSimpleName() + " close");
        fileReader.close();
    }
}
