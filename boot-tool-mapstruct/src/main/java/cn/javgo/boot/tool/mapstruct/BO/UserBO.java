package cn.javgo.boot.tool.mapstruct.BO;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Description: User BO
 *
 * @author javgo
 * @date 2024/06/09
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class UserBO {

    /**
     * 主键
     */
    // private Integer id;
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
