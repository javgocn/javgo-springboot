package cn.javgo;

import cn.javgo.boot.tool.mapstruct.BO.UserBO;
import cn.javgo.boot.tool.mapstruct.DO.UserDO;
import cn.javgo.boot.tool.mapstruct.MapStructApplication;
import cn.javgo.boot.tool.mapstruct.convert.UserConvert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * Description: MapStruct 基本功能测试
 *
 * @author javgo
 * @date 2024/06/09
 * @version: 1.0
 */
@SpringBootTest(classes = MapStructApplication.class)
public class UserBOTest {

    @Autowired
    private UserConvert userConvert;

    @Test
    void testDoToBo() {
        // 创建 DO
        UserDO userDO = new UserDO()
                .setId(1)
                .setUsername("javgo")
                .setPassword("123456")
                .setCreateTime(LocalDateTime.now())
                .setUpdateTime(LocalDateTime.now());
        System.out.println(userDO);

        // DO -> BO
        UserBO userBO = userConvert.convertTo(userDO);
        System.out.println(userBO);
    }
}
