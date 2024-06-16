package cn.javgo.boot.starter.config;

import cn.javgo.boot.starter.service.TestService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Description: 测试自动配置类
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@AutoConfiguration // 启用自动配置
@ConditionalOnProperty(prefix = "javgo.starter", name = "enable", havingValue = "true") // 启用条件
public class TestServiceAutoConfiguration {

    // 条件成立时，创建 TestService 对象
    @Bean
    public TestService testService() {
        return new TestService();
    }
}
