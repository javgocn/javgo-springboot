package cn.javgo.boot.base.starters.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/16
 * @version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "mail")
public class CustomerMailProperties {

    String from;

    String personal;

    String bcc;

    String subject;
}
