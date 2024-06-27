package com.moomoo.endecrypter;

import static com.moomoo.endecrypter.util.AESUtil.*;

public class EnDecrypterMain {
    public static void main(String[] args) {
        try {
            String plainText = args[0];
            String key = generateKey();
            System.out.println("Plain Text: " + plainText);
            System.out.println("Generate Key: " + key);

            String encryptedText = encrypt(plainText, key);
            System.out.println("Encrypted Text: " + encryptedText);

            String decryptedText = decrypt(encryptedText, key);
            System.out.println("Decrypted Text: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
