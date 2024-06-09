package cn.javgo.boot.tool.mapstruct.convert;

import cn.javgo.boot.tool.mapstruct.BO.UserBO;
import cn.javgo.boot.tool.mapstruct.DO.UserDO;
import cn.javgo.boot.tool.mapstruct.handler.BaseConvert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.format.DateTimeFormatter;

/**
 * Description: User Convert
 *
 * @author javgo
 * @date 2024/06/09
 * @version: 1.0
 */
@Mapper(componentModel = "spring") // 声明为 MapStruct 的转换器, 默认使用 Spring 的 Spring Bean 容器创建对象
public interface UserConvert extends BaseConvert<UserDO, UserBO> {

    // TODO 如果有需要可以重写 BaseConvert 的 afterConvertTo 和 afterConvertFrom 方法，添加自定义的转换逻辑

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     * 添加自定义的转换逻辑，覆盖 BaseConvert 的 afterConvertTo 方法，这里将 createTime 和 updateTime 格式化成 yyyy-MM-dd HH:mm:ss 格式
     * @param source 源对象
     * @param target 目标对象
     */
    @Override
    default void afterConvertTo(UserDO source, UserBO target) {
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        target.setCreateTime(source.getCreateTime().format(dateTimeFormatter));
        target.setUpdateTime(source.getUpdateTime().format(dateTimeFormatter));
    }

    @Mappings(
            @Mapping(source = "id", target = "userId") // 将 id 属性映射到 userId 属性
    )
    @Override
    UserBO convertTo(UserDO source);
}
