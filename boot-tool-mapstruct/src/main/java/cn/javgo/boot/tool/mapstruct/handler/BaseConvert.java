package cn.javgo.boot.tool.mapstruct.handler;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Description: MapStruct 公共映射接口（泛型：S-源对象，T-目标对象）
 *
 * @author javgo
 * @date 2024/06/09
 * @version: 1.0
 */
public interface BaseConvert<S, T> {

    /**
     * 映射源对象到目标对象
     *
     * @param source 源对象
     * @return 目标对象
     */
    @InheritConfiguration
    T convertTo(S source);

    /**
     * 映射目标对象到源对象
     *
     * @param target 目标对象
     * @return 源对象
     */
    @InheritInverseConfiguration(name = "convertTo")
    S convertFrom(T target);

    /**
     * 映射源对象列表到目标对象列表
     *
     * @param sourceList 源对象列表
     * @return 目标对象列表
     */
    default List<T> convertToList(List<S> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return null;
        }
        List<T> result = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> {
            if (Objects.nonNull(source)) {
                result.add(convertTo(source));
            }
        });
        return result;
    }

    /**
     * 映射目标对象列表到源对象列表
     *
     * @param targetList 目标对象列表
     * @return 源对象列表
     */
    default List<S> convertFromList(List<T> targetList) {
        if (CollectionUtils.isEmpty(targetList)) {
            return null;
        }
        List<S> result = new ArrayList<>(targetList.size());
        targetList.forEach(target -> {
            if (Objects.nonNull(target)) {
                result.add(convertFrom(target));
            }
        });
        return result;
    }

    /**
     * 源对象到目标对象转换后的额外处理
     *
     * @param source 源对象
     * @param target 目标对象
     */
    default void afterConvertTo(S source, T target) {
        // TODO 覆盖此方法处理源对象到目标对象转换后的额外处理
    }

    /**
     * 目标对象到源对象转换后的额外处理
     *
     * @param target 目标对象
     * @param source 源对象
     */
    default void afterConvertFrom(T target, S source) {
        // TODO 覆盖此方法处理目标对象到源对象转换后的额外处理
    }

    /**
     * 源对象到目标对象转换后的额外处理
     *
     * @param source 源对象
     * @param target 目标对象
     */
    @AfterMapping
    default void handleAfterConvertTo(S source, @MappingTarget T target) {
        afterConvertTo(source, target);
    }

    /**
     * 目标对象到源对象转换后的额外处理
     *
     * @param target 目标对象
     * @param source 源对象
     */
    @AfterMapping
    default void handleAfterConvertFrom(T target, S source) {
        afterConvertFrom(target, source);
    }

    /**
     * 映射源对象流到目标对象流
     *
     * @param sourceStream 源对象流
     * @return 目标对象流
     */
    @InheritConfiguration(name = "convertTo")
    Stream<T> convertToStream(Stream<S> sourceStream);

    /**
     * 映射目标对象流到源对象流
     *
     * @param targetStream 目标对象流
     * @return 源对象流
     */
    @InheritConfiguration(name = "convertFrom")
    Stream<S> convertFromStream(Stream<T> targetStream);
}
