package cn.javgo.boot.base.autoconfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class},
        excludeName = {"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"}
) // 禁用数据源自动配置
public class AutoConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoConfigApplication.class, args);
    }
}
