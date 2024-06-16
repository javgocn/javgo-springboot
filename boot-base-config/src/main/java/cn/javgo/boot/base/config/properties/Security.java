package cn.javgo.boot.base.config.properties;

import lombok.Data;

/**
 * Description: TODO
 *
 * @author javgo
 * @date 2024/06/15
 * @version: 1.0
 */
@Data
public class Security {

    private String securityKey;

    private String securityCode;
}
