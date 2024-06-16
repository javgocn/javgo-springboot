package cn.javgo.boot.base.config;

import cn.javgo.boot.base.config.pojo.OtherMember;
import cn.javgo.boot.base.config.properties.JavagoProperties;
import cn.javgo.boot.base.config.properties.MemberProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Slf4j
@RequiredArgsConstructor
@ConfigurationPropertiesScan // 启用配置属性扫描
//@EnableConfigurationProperties(value = {JavagoProperties.class, MemberProperties.class}) // 启用配置属性
@SpringBootApplication
public class ConfigApplication {

    public static void main(String[] args) {
        // SpringApplication.run(ConfigApplication.class, args);
        // 启用 dev 和 test Profile
        SpringApplication springApplication = new SpringApplication(ConfigApplication.class);
        springApplication.setAdditionalProfiles("dev", "test");
        springApplication.run(args);
    }

    private final JavagoProperties javagoProperties;
    private final MemberProperties memberProperties;
    private final OtherMember otherMember;

    @Value("${javgo.username}")
    private String username;

    @Value("${javgo.password}")
    private String password;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return (args) -> {
            log.info("javgoProperties: {}", javagoProperties);
            log.info("memberProperties: {}", memberProperties);
            log.info("otherMember: {}", otherMember);

            log.info("javgo.username = {}", username);
            log.info("javgo.password = {}", password);
        };
    }
}
