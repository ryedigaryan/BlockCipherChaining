package rub.labs.chaining.block;

import lombok.NoArgsConstructor;
import rub.labs.cryptoalgo.EncryptionAlgorithm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@NoArgsConstructor
public abstract class EncryptorBlockCrypter<K> extends BlockCrypter<K> {

    public EncryptorBlockCrypter(EncryptionAlgorithm<K> algorithm) {
        super(algorithm);
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
        getAlgorithm().encrypt(dKey, new ByteArrayInputStream(inputBlock, 0, size), outputBlockOS);
        // write encrypted data into desired output
        byte[] outputBlock = decryptionModifier().secondModification(outputBlockOS.toByteArray(), vector, size);
        openDataOS.write(Arrays.copyOf(outputBlock, size));
        outputBlockOS.close();
        getVectorStorage().accept(getLastGeneratedVector());
    }
}
