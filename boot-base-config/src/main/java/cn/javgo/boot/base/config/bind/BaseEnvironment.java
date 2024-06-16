package cn.javgo.boot.base.config.bind;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Description: 绑定配置：通过注入 Environment Bean 获取配置
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Component
public class BaseEnvironment {

    @Resource
    private Environment environment;

    String getProperty(String key) {
        return environment.getProperty(key);
    }
}
