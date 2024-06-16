package cn.javgo.boot.base.config.bind;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Description: 配置绑定：基于 @PropertySource 注解（只能用于 .properties 文件）
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Component
@PropertySource(value = {"classpath:config/db-config.properties"})
public class BasePropertySource {

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;
}
