package chaining.helper;

public interface BlockCrypterVectorProvider extends Resetable {
    byte[] nextEncryptionVector();
    byte[] nextDecryptionVector();
}
