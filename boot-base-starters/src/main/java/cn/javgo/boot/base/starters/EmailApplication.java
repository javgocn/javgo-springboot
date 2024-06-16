package cn.javgo.boot.base.starters;

import cn.javgo.boot.base.starters.config.properties.CustomerMailProperties;
import cn.javgo.boot.starter.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@Slf4j
@EnableConfigurationProperties(value = {CustomerMailProperties.class})
@SpringBootApplication
public class EmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(TestService testService) {
        return (args) -> log.info(testService.getServiceName());
    }
}
