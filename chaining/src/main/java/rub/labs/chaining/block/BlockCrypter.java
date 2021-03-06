package rub.labs.chaining.block;

import rub.labs.chaining.utils.Modifier;
import rub.labs.cryptoalgo.EncryptionAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter @Setter
@NoArgsConstructor
@RequiredArgsConstructor
public abstract class BlockCrypter<K> extends EncryptionAlgorithm<K> {
    @NonNull
    private EncryptionAlgorithm<K> algorithm;

    private Supplier<byte[]> vectorProvider;
    private Consumer<byte[]> vectorStorage;

    /**
     * The block which size is less than cryptable block size (mainly last one) will be filled with this byte
     */
    //TODO: change this to Byte, so when fill == null => no fill will be made
    private byte fill;

    //
    // EncryptionAlgorithm
    //

    /**
     * This class must not do any normalization, because the root encryption {@link #algorithm} will do that when it's
     * necessarily (mainly in {@link EncryptionAlgorithm#encrypt(Object, InputStream, OutputStream)} &
     * {@link EncryptionAlgorithm#decrypt(Object, InputStream, OutputStream)} methods)
     * @param key raw key, which must be normalized
     * @return the same {@code key} which passed as argument
     * @throws IllegalArgumentException if passed {@code key} type is not {@link K}
     */
    @Override
    protected final K normalizeKey(Object key) throws IllegalArgumentException {
        try {
            return (K) key;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e); //TODO: mb e.getMessage() is better?
        }
    }

    /**
     * This class must not do any calculation, because the root encryption {@link #algorithm} will do that when it's
     * needed (mainly in {@link EncryptionAlgorithm#decrypt(Object, InputStream, OutputStream)} method)
     * @param eKey encryption key from which decryption key must be constructed
     * @return the same {@code eKey} which passed as argument
     */
    @Override
    protected final K decryptionKey(K eKey) {
        return eKey;
    }

    //TODO: pass here the block and return block instead of streams in encrypt(...) methods write response into ostream
    @Override
    protected void applyEncryptionAlgorithm(K eKey, InputStream openDataIS, OutputStream encryptedDataOS) throws IOException {
        byte[] vector = getVectorProvider().get();
        int size = vector.length;
        // prepare input stream
        byte[] inputBlock = new byte[size];
        fillMissingBytes(inputBlock, openDataIS.read(inputBlock));
        inputBlock = encryptionModifier().firstModification(inputBlock, vector, size);
        // create OutputStream which will hold all encrypted data
        ByteArrayOutputStream outputBlockOS = new ByteArrayOutputStream(size);
        getAlgorithm().encrypt(eKey, new ByteArrayInputStream(inputBlock), outputBlockOS);
        // write encrypted data into desired output
        byte[] outputBlock = encryptionModifier().secondModification(outputBlockOS.toByteArray(), vector, size);
        encryptedDataOS.write(Arrays.copyOf(outputBlock, size));
        outputBlockOS.close();
        getVectorStorage().accept(getLastGeneratedVector());
    }

    @Override
    protected void applyDecryptionAlgorithm(K dKey, InputStream encryptedDataIS, OutputStream openDataOS) throws IOException {
        byte[] vector = getVectorProvider().get();
        int size = vector.length;
        // prepare input stream
        byte[] inputBlock = new byte[size];
        // in theory there should be no case when the read length is less than cryptable block size
        fillMissingBytes(inputBlock, encryptedDataIS.read(inputBlock));
        inputBlock = decryptionModifier().firstModification(inputBlock, vector, size);
        // create OutputStream which will hold all encrypted data
        ByteArrayOutputStream outputBlockOS = new ByteArrayOutputStream(size);
        getAlgorithm().decrypt(dKey, new ByteArrayInputStream(inputBlock, 0, size), outputBlockOS);
        // write encrypted data into desired output
        byte[] outputBlock = decryptionModifier().secondModification(outputBlockOS.toByteArray(), vector, size);
        openDataOS.write(Arrays.copyOf(outputBlock, size));
        outputBlockOS.close();
        getVectorStorage().accept(getLastGeneratedVector());
    }

    void fillMissingBytes(final byte[] bytes, int startIndex) {
        while(startIndex < bytes.length) {
            bytes[startIndex] = fill;
            startIndex++;
        }
    }

    protected abstract Modifier encryptionModifier();
    protected abstract Modifier decryptionModifier();

    protected abstract byte[] getLastGeneratedVector();
}