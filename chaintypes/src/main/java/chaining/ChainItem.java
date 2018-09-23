package chaining;

import chaining.helper.ChainItemDataSource;
import chaining.helper.CipherDecorator;
import cryptoalgo.Cipher;

public abstract class ChainItem extends CipherDecorator implements ChainItemDataSource {
    private ChainItemDataSource encryptionDataSource;
    private ChainItemDataSource decryptionDataSource;

    public ChainItem(Cipher rootCipher) {
        super(rootCipher);
    }

    public void setEncryptionDataSource(ChainItemDataSource encryptionDataSource) {
        this.encryptionDataSource = encryptionDataSource;
    }

    public void setDecryptionDataSource(ChainItemDataSource decryptionDataSource) {
        this.decryptionDataSource = decryptionDataSource;
    }
}
