package cn.javgo.boot.base.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Description: 构造绑定
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "member")
public class MemberProperties {

    private String name;
    private int sex;
    private int age;
    private String country;
    private Date birthday;

    public MemberProperties(String name, int sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    @ConstructorBinding // 构造绑定
    public MemberProperties(String name, int sex, int age,
                            @DefaultValue("China") String country,
                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date birthday) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.country = country;
        this.birthday = birthday;
    }
}
