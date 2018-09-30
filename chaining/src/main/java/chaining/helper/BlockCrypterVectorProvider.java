package chaining.helper;

public interface BlockCrypterVectorProvider {
    byte[] nextEncryptionVector();
    byte[] nextDecryptionVector();
}
