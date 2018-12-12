package rub.labs.chaining.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class RandomNumberKeyProvider implements BlockCrypterKeyProvider<Integer> {

    private final long range;

    private final ArrayList<Integer> mainKeyStorage = new ArrayList<>();
    private Iterator<Integer> keyStorageIterator = mainKeyStorage.iterator();

    private LinkedList<Integer> generatedKeyStorage = new LinkedList<>();

    public RandomNumberKeyProvider() {
        this(Integer.MAX_VALUE);
    }

    public RandomNumberKeyProvider(long positiveRange) {
        range = positiveRange;
    }

    @Override
    public Integer get() {
        final Integer randomKey;
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

    private Integer generateRandomKey() {
        return Math.random() > 0 ?
                (int) (Math.random() * range):
                (int) (Math.random() * -range);
    }
}
