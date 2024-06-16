package cn.javgo.boot.base.config.config.encipher;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Description: 自定义 AES 加密算法
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
public class Encrypt {

    /**
     * 算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 使用 AES 加密算法解密字符串
     *
     * @param encryptedText 要解密的文本
     * @param key 加密密钥
     * @return 解密后的字符串
     */
    public static String decrypt(String encryptedText, String key) {
        try {
            // 解码 base64 编码的字符串
            byte[] decodedKey = Base64.getDecoder().decode(key);
            // 使用 SecretKeySpec 重建密钥
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);

            // AES 解密
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] decryptedByte = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

            return new String(decryptedByte);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while decrypting data", e);
        }
    }

    /**
     * 生成新的加密密钥
     *
     * @return Base64 编码的密钥
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            // 使用 256 位高强度密钥
            keyGen.init(256, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while generating key", e);
        }
    }
}
