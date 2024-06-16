package cn.javgo.boot.base.config;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@Slf4j
@SpringBootTest
public class JasyptTest {

    /**
     * 字符串加密器
     */
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encrypt() {
        // 加密
        String usernameEnc = stringEncryptor.encrypt("javago");
        String passwordEnc = stringEncryptor.encrypt("javago.cn");

        log.info("test username encrypt is {}", usernameEnc);
        log.info("test password encrypt is {}", passwordEnc);

        // 解密
        log.info("test username decrypt is {}", stringEncryptor.decrypt(usernameEnc));
        log.info("test password decrypt is {}", stringEncryptor.decrypt(passwordEnc));
    }
}
