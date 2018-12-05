package rub.labs.chaining.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class RandomNumberKeyProvider implements BlockCrypterKeyProvider {

    private final long range;

    private final ArrayList<Long> mainKeyStorage = new ArrayList<>();
    private Iterator<Long> keyStorageIterator = mainKeyStorage.iterator();

    private LinkedList<Long> generatedKeyStorage = new LinkedList<>();

    public RandomNumberKeyProvider() {
        this(Long.MAX_VALUE);
    }

    public RandomNumberKeyProvider(long positiveRange) {
        range = positiveRange;
    }

    @Override
    public Object get() {
        final Long randomKey;
        if(keyStorageIterator.hasNext()) {
            System.out.println("RandomNumberKeyProvider: providing existing key");
            randomKey = keyStorageIterator.next();
        } else {
            System.out.println("RandomNumberKeyProvider: providing newly generated key");
            generatedKeyStorage.add(randomKey = generateRandomKey());
        }
        return randomKey;
    }

    @Override
    public void reset() {
        // add all generated keys to main storage
        mainKeyStorage.addAll(generatedKeyStorage);
        // reset iterator to point to new storage
        keyStorageIterator = mainKeyStorage.iterator();
        // create new storage for generated keys
        generatedKeyStorage = new LinkedList<>();
    }

    private Long generateRandomKey() {
        return Math.random() > 0 ?
                (long) (Math.random() * range):
                (long) (Math.random() * -range);
    }
}
