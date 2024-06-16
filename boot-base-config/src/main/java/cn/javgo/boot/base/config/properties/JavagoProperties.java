package cn.javgo.boot.base.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

/**
 * Description: 参数绑定
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Data
@Validated // 启用参数校验
@ConfigurationProperties(prefix = "javgo")
public class JavagoProperties {

    private boolean enabled;

    @NotNull
    private String name;

    private String site;

    private String author;

    private List<String> users;

    private Map<String, String> params;

    private Security security;

}
