package chaining.helper;

public interface BlockCrypterVectorProvider extends Resettable {
    byte[] nextEncryptionVector();
    byte[] nextDecryptionVector();
}
