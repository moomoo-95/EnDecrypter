package com.moomoo.endecrypter.util;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @author Hyeon seong, Lim
 * 평문과 암호문 간 Encrypt, Decrypt를 처리하는 클래스
 */
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

    /**
     * 키 값을 생성하는 메서드
     * @return 정의된 알고리즘을 토대로 생성한 키 값
     * @throws NoSuchAlgorithmException
     */
    public static String generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return ENCODER.encodeToString(secretKey.getEncoded());
    }

    /**
     * 키 값을 생성하는 메서드
     * @return 텍스트를 기반 생성한 키 값
     * @throws NoSuchAlgorithmException
     */
    public static String customKey(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return ENCODER.encodeToString(keyBytes);
    }

    /**
     * 평문에 대해 암호화한 값을 생성하는 메서드
     * @param plainData 암호화할 평문 데이터
     * @param key 암호화에 사용할 키 값
     * @return 암호화된 값
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String encrypt(String plainData, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
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

    /**
     * 암호문에 대해 복호화한 값을 생성하는 메서드
     * @param encryptedData 복호화할 값
     * @param key 복호화에 사용할 키 값
     * @return 복호화된 값
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String decrypt(String encryptedData, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
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
