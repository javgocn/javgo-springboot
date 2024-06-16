package cn.javgo.boot.base.config.config;

import cn.javgo.boot.base.config.pojo.OtherMember;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Profile(value = "main") // 只在 main 配置文件生效
@SpringBootConfiguration
public class MainConfig {

    @Bean
    @ConfigurationProperties(prefix = "member")
    public OtherMember otherMember() {
        return new OtherMember();
    }
}
