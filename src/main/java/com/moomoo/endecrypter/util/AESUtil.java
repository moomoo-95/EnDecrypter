package com.moomoo.endecrypter.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;  // GCM 권장 IV 사이즈는 12바이트
    private static final int TAG_SIZE = 128; // 128비트 인증 태그

    private static final Base64.Decoder DECODER = Base64.getDecoder();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    private AESUtil() {
        throw new IllegalStateException("Utility class");
    }

    // 키 생성 함수
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return ENCODER.encodeToString(secretKey.getEncoded());
    }

    // 암호화 함수
    public static String encrypt(String plainData, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(DECODER.decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(plainData.getBytes());

        byte[] encryptedIvAndText = new byte[IV_SIZE + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, IV_SIZE);
        System.arraycopy(encryptedBytes, 0, encryptedIvAndText, IV_SIZE, encryptedBytes.length);

        return ENCODER.encodeToString(encryptedIvAndText);
    }

    // 복호화 함수
    public static String decrypt(String encryptedData, String key) throws Exception {
        byte[] encryptedIvAndTextBytes = Base64.getDecoder().decode(encryptedData);

        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedIvAndTextBytes, 0, iv, 0, IV_SIZE);

        byte[] encryptedBytes = new byte[encryptedIvAndTextBytes.length - IV_SIZE];
        System.arraycopy(encryptedIvAndTextBytes, IV_SIZE, encryptedBytes, 0, encryptedBytes.length);

        SecretKeySpec secretKeySpec = new SecretKeySpec(DECODER.decode(key), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(TAG_SIZE, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes);
    }
}
