package test;

import chaining.block.ECB;
import chaining.chain.Chain;
import chaining.chain.Chain.Node;
import chaining.utils.BlockCrypterKeyProvider;
import cryptoalgo.byteshuffle.BasicShifter;
import cryptoalgo.caesar.CaesarCipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//TODO: THINK ABOUT CHAIN .... FUCK
public class Tester {
    public static void main(String[] args) throws Throwable {
//        ecbWithShifter();
        shifter();
    }

    static void ecbWithShifter() {
        BasicShifter bs = new BasicShifter();
        bs.setKey(-84);

        String basePath = "src/main/resources/BasicShifter/";

    }

    static void caesar() throws Throwable {
        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";

        CaesarCipher cc = new CaesarCipher();
        cc.setKey(-22L);

        String basePath = "src/main/resources/Caesar/";

        try(FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");
        FileInputStream encryptIn = new FileInputStream(basePath + "ExpectedEncrypt.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt")) {

            cc.encrypt(openIn, encryptOut);
            cc.decrypt(encryptIn, decryptOut);
        }
    }

    static void shifter() throws IOException {
        BasicShifter bs = new BasicShifter();
        bs.setKey(-84);

        String basePath = "src/main/resources/BasicShifter/";

        System.out.println(bs.getEncryptionKey());
        System.out.println(bs.getDecryptionKey());

        try(FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
            FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt")) {

            while(openIn.available() > 0)
            bs.encrypt(openIn, encryptOut);

        }

        try(FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
            FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt")) {
            while(encryptIn.available() > 0)
                bs.decrypt(encryptIn, decryptOut);
        }
    }

    static private void ecb() throws Throwable {

        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";

        String basePath = "src/main/resources/Caesar/";

        FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");


        CaesarCipher cc = new CaesarCipher();
//        cc.setKey(-22L);
        ECB<Byte> ecb = new ECB<>(cc, 1);
        BlockCrypterKeyProvider<Byte> keyProvider = new BlockCrypterKeyProvider<Byte>() {
            public Byte get() {
                System.out.println(getClass() + " provides next key");
                return -22;
            }

            public void reset() {
                System.out.println("Resetting KeyProvider");
            }
        };

//        ecb.encrypt(openIn, encryptOut);
//        ecb.decrypt(encryptIn, decryptOut);

        Node<Byte> ecbCI = new Node<>(ecb, keyProvider, 10);
        Node<Byte> ecbCI2 = new Node<>(ecb, keyProvider, 5);

        Chain ecbChain = new Chain(null, ecbCI, ecbCI2);
        ecbChain.encrypt(openIn, encryptOut);
        ecbChain.decrypt(encryptIn, decryptOut);

        openIn.close();
        encryptOut.close();
        encryptIn.close();
        decryptOut.close();

    }
}

class Tst {
    private Tst() {
        System.out.println("in a private constructor");
    }

    public static Tst create() {
        return new Tst();
    }
}