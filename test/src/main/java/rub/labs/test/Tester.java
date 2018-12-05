package rub.labs.test;

import rub.labs.chaining.block.BlockCrypter;
import rub.labs.chaining.block.CBC;
import rub.labs.chaining.block.CFB;
import rub.labs.chaining.block.ECB;
import rub.labs.chaining.block.PCBC;
import rub.labs.chaining.chain.Chain;
import rub.labs.chaining.chain.Chain.Node;
import rub.labs.chaining.utils.BlockCrypterKeyProvider;
import rub.labs.cryptoalgo.byteshuffle.BasicShifter;
import rub.labs.cryptoalgo.caesar.CaesarCipher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Tester {
    static BlockCrypterKeyProvider<Integer> kp = new BlockCrypterKeyProvider<Integer>() {
        @Override
        public void reset() {

        }

        @Override
        public Integer get() {
            return -84;
        }
    };

    public static void main(String[] args) throws Throwable {
//        JOptionPane.showInputDialog("FUCK");

//        shifter();
//        BasicShifter._count = 0;
//        ecbWithShifter();
//        nodesWithShifter(new CBC<>());
        diverseNodesWithShifter();
    }

    static void nodesWithShifter(BlockCrypter<Integer> blockCrypter) throws IOException {

        String basePath = "src/main/resources/BasicShifter/";


        BasicShifter bs = new BasicShifter();
        blockCrypter.setAlgorithm(bs);
        blockCrypter.setFill((byte) 8);
        BlockCrypterKeyProvider<Integer> kp = new BlockCrypterKeyProvider<Integer>() {
            @Override
            public void reset() {

            }

            @Override
            public Integer get() {
                return -84;
            }
        };

        Node<Integer> node = new Node<>(blockCrypter, kp, 1);

        byte[] initialVector = {0, 1, 2};
        Chain chain = new Chain(initialVector, node);

        /// encrypt
        FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");

        chain.encrypt(openIn, encryptOut);
        openIn.close();
        encryptOut.close();

        chain.reset();
        /// decrypt
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");

        chain.decrypt(encryptIn, decryptOut);
        encryptIn.close();
        decryptOut.close();

    }

    static void diverseNodesWithShifter() throws IOException {

        String basePath = "src/main/resources/BasicShifter/";

        BasicShifter bs = new BasicShifter();

        Node[] nodes = new Node[] {
                new Node<>(new CBC<Integer>(), kp, 1),
//                new Node<>(new ECB<Integer>(), kp, 1),
//                new Node<>(new OFB<Integer>(), kp, 1),
//                new Node<>(new CFB<Integer>(), kp, 1),
                new Node<>(new PCBC<Integer>(), kp, 2),
                new Node<>(new CFB<Integer>(), kp, 3),
        };

        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            node.getBlockCrypter().setAlgorithm(bs);
            node.getBlockCrypter().setFill((byte) 8);
        }

        byte[] initialVector = {0, 1, 2, 3, 9, 24, 99};
        Chain chain = new Chain(initialVector, nodes);

        /// encrypt
        FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
        FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt");

        chain.encrypt(openIn, encryptOut);
        openIn.close();
        encryptOut.close();

        chain.reset();
        /// decrypt
        FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
        FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt");

        chain.decrypt(encryptIn, decryptOut);
        encryptIn.close();
        decryptOut.close();
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
        BasicShifter bs = new BasicShifter(-84);

        String basePath = "src/main/resources/BasicShifter/";

        System.out.println(bs.getEncryptionKey());
        System.out.println(bs.getDecryptionKey());

        try(FileInputStream openIn = new FileInputStream(basePath + "OpenData.txt");
            FileOutputStream encryptOut = new FileOutputStream(basePath + "Encrypted.txt")) {

            System.out.println("BasicShifter - encrypt - START");
//            while(openIn.available() > 0)
            bs.encrypt(openIn, encryptOut);
            System.out.println("BasicShifter - encrypt - DONE");

        }

        try(FileInputStream encryptIn = new FileInputStream(basePath + "Encrypted.txt");
            FileOutputStream decryptOut = new FileOutputStream(basePath + "Decrypted.txt")) {
            System.out.println("BasicShifter - decrypt - START");
//            while(encryptIn.available() > 0)
            bs.decrypt(encryptIn, decryptOut);
            System.out.println("BasicShifter - decrypt - DONE");
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
        ECB<Byte> ecb = new ECB<>(cc);
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