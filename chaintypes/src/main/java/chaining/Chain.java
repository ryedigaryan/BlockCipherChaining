package chaining;

import chaining.helper.ChainItemProvider;

import java.util.function.Consumer;

public class Chain{
    private final String name;
    private ChainItemProvider chainItemProvider;

    private Chain(String name, ChainItemProvider chainItemProvider) {
        this.name = name;
        this.chainItemProvider = chainItemProvider;
    }

    public String getName() {
        return name;
    }

    public void encrypt(Iterable<byte[]> plainTextBlockProvider, Consumer<byte[]> encryptedBlockHandler) {
        chainItemProvider.restart();
        byte[] encrypted;
        for(byte[] plainBlock : plainTextBlockProvider) {
            ChainItem chainItem = chainItemProvider.get();
            assert chainItem != null : "ChainItemProvider returned null";
            encrypted = chainItem.encrypt(plainBlock);
            encryptedBlockHandler.accept(encrypted);
        }
    }

    public void decrypt(Iterable<byte[]> encryptedTextBlockProvider, Consumer<byte[]> decryptedBlockHandler) {
        chainItemProvider.restart();
        byte[] decrypted;
        for(byte[] encryptedBlock : encryptedTextBlockProvider) {
            ChainItem chainItem = chainItemProvider.get();
            assert chainItem != null : "ChainItemProvider returned null";
            decrypted = chainItem.decrypt(encryptedBlock);
            decryptedBlockHandler.accept(decrypted);
        }
    }
}