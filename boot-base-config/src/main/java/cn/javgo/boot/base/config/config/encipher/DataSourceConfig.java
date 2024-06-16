package cn.javgo.boot.base.config.config.encipher;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Description: 自定义 DataSource 配置
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@SpringBootConfiguration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final Environment environment;

    // 自定义配置加解密
    @Bean
    public DataSource dataSource() {
        // DruidDataSource dataSource = new DruidDataSource();

        // 解密
//        String username = this.getUsername();
//        if (username.startsWith("{cipher}")) {
//            username = Encrypt.decrypt(username, this.getKey());
//        }
//        dataSource.setUsername(username);

        // ...

//        return dataSource;
        return null;
    }

    public String getUsername() {
        return environment.getProperty("spring.datasource.username");
    }

    public String getKey() {
        // 如果存储在配置文件中，则从配置文件中获取
        // return environment.getProperty("javgo.security.security-key");

        // 更加安全的做法是存储在环境变量中
        return System.getenv("JAVGO_SECURITY_SECURITY_KEY");
    }
}
