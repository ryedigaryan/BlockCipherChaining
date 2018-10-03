package test;

import chaining.block.ECB;
import chaining.chain.ChainItem;
import chaining.chain.SimpleChain;
import chaining.helper.BlockCrypterKeyProvider;
import cryptoalgo.CaesarCipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;

//TODO: THINK ABOUT CHAIN .... FUCK
public class Tester {
    public static void main(String[] args) throws Throwable {
//        Tst t = Tst.create();
//        Constructor<Tst> c = Tst.class.getDeclaredConstructor();
//        c.setAccessible(true);
//        Tst t2 = c.newInstance();


        Tester t = new Tester();
//        t.caesarTest();

        t.ecbTest();
    }

    void caesarTest() throws Throwable {
        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";

        CaesarCipher cc = new CaesarCipher(-22L);

        String basePath = "src/main/resources/Caesar/";

        try(FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");) {

            cc.encrypt(openIn, encryptOut);
            cc.decrypt(encryptIn, decryptOut);
        }
    }

    void ecbTest() throws Throwable {

        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";

        String basePath = "src/main/resources/Caesar/";

        FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");


        CaesarCipher cc = new CaesarCipher(-22L);
        ECB<Byte> ecb = new ECB<Byte>(cc, 1);
        BlockCrypterKeyProvider<Byte> keyProvider = new BlockCrypterKeyProvider<Byte>() {
            public Byte nextKey() {
                return -22;
            }

            public void reset() {
                System.out.println("Resetting KeyProvider");
            }
        };
        ecb.setKeyProvider(keyProvider);

//        ecb.encrypt(openIn, encryptOut);
//        ecb.decrypt(encryptIn, decryptOut);

        ChainItem<Byte> ecbCI = new ChainItem<>(ecb, 70);
        ChainItem<Byte> ecbCI2 = new ChainItem<>(ecb, 6);

        SimpleChain ecbSimpleChain = new SimpleChain(null, ecbCI, ecbCI2);
        ecbSimpleChain.encrypt(openIn, encryptOut);
        ecbSimpleChain.decrypt(encryptIn, decryptOut);

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