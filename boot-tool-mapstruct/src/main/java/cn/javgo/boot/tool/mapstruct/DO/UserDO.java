package cn.javgo.boot.tool.mapstruct.DO;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * Description: User DO
 *
 * @author javgo
 * @date 2024/06/09
 * @version: 1.0
 */
@Data
@Accessors(chain = true)
public class UserDO {

    /**
     * 主键
     */
    private Integer id;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
