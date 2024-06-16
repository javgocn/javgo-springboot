package cn.javgo.boot.base.config.bind;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description: 配置绑定：基于 @Value 注解
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
//@Component
public class BaseValue {

    @Value("${base.name}")
    private String name;

    @Value("${base.age}")
    private Integer age;
}
