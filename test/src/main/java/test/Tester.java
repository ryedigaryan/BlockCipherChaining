package test;

import cryptoalgo.CaesarCipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;

//TODO: THINK ABOUT CHAIN .... FUCK
public class Tester {
    public static void main(String[] args) throws Throwable {
        Tester t = new Tester();
        t.caesarTest();
    }

    void caesarTest() throws Throwable {
        String plain = "abcd, efgh ijkl mnop - qrst uvw xyz **ABCD EFGH I#JKL) MNOP QRST UVW XYZ1234";
        String expect = "efgh, ijkl mnop qrst - uvwx yza bcd **EFGH IJKL M#NOP) QRST UVWX YZA BCD1234";

        CaesarCipher cc = new CaesarCipher(-22L);

        String basePath = "src/main/resources/Caesar/";

        FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");

        cc.encrypt(openIn, encryptOut);
        cc.decrypt(encryptIn, decryptOut);

    }
}
