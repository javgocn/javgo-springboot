# SpringBoot - MapStruct

* 官方文档：[https://mapstruct.org/](https://mapstruct.org/)

![](https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-140228.png)

> TIP：完整代码见 [boot-tool-mapstruct](https://github.com/javgocn/javgo-springboot/tree/release/boot-tool-mapstruct)

## MapStruct 概述

为了使应用代码更易于维护和扩展，通常会对项目进行分层设计。《[阿里巴巴 Java 开发手册](https://github.com/alibaba/p3c/blob/master/阿里巴巴Java开发手册（华山版）.pdf)》推荐了如下的分层架构：

<img src="https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-064401.png" style="zoom:33%;" />

每一层都有其特定的领域模型，各层之间通过不同类型的对象进行数据传输，这些对象包括：

* **DO**（Data Object）：数据对象，与数据库表结构一一对应，通过 DAO 层传输数据。
* **DTO**（Data Transfer Object）：数据传输对象，用于在 Service 或 Manager 层向外传输数据。
* **BO**（Business Object）：业务对象，由 Service 层生成，封装了业务逻辑。
* **VO**（View Object）：视图对象，通常用于向前端传输数据。
* **PO**（Persistent Object）：持久化对象，与数据库的持久化数据对应。

在不同层之间进行对象转换是常见需求。例如，从数据库查询的用户数据对象（`UserDO`）需要转换为业务对象（`UserBO`）：

```java
// 从数据库中查询用户
UserDO userDO = userMapper.selectById(id);

// 手动对象转换
UserBO userBO = new UserBO();
userBO.setId(userDO.getId());
userBO.setUsername(userDO.getUsername());
// ... 设置其他属性
```

手动转换对象不仅繁琐，还容易出错，并且在开发中效率低下。为了解决这些问题，可以借助一些工具或框架来自动化对象转换，例如：

- **Spring BeanUtils**：一个简单的工具类，适用于简单对象的拷贝。
- **Apache BeanUtils**：功能较为丰富，但性能较差。
- **Dozer**：一个支持复杂转换的工具，使用反射机制，性能较低。
- **Orika**：提供了灵活的映射方式，但需要较多的配置。
- **MapStruct**：基于 Java 注解处理器，性能优异，使用便捷。（推荐）
- **ModelMapper**：功能强大，配置灵活，但性能稍差。
- **JMapper**：支持复杂对象映射，配置较为复杂。

在众多对象转换工具中，**MapStruct**脱颖而出，成为常用的选择之一。MapStruct 基于[JSR 269 Java 注解处理器](https://jcp.org/en/jsr/detail?id=269)，通过编译时生成对象转换代码，实现高效且类型安全的对象映射。

MapStruct 的优势：

1. **性能优异**：MapStruct 生成的代码使用纯 Java 方法进行调用，不依赖于反射机制，因此具有更高的执行效率。
2. **类型安全**：在编译期进行类型检查，确保只能在相互兼容的对象之间进行映射，减少了运行时错误的可能性。
3. **易于理解和维护**：生成的映射代码直观且易于理解，有助于提高代码的可读性和维护性。
4. **快速反馈**：编译时生成映射代码，可以立即发现和修复映射错误，缩短开发调试周期。

MapStruct 官方首页给出了最简单的使用 Demo，只需创建一个映射器接口，并定义对象转换方法，MapStruct 会在编译时自动生成该接口的实现。例如，定义一个简单的用户转换映射器：

```java
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "name", target = "username")
    UserBO toUserBO(UserDO userDO);
}
```

MapStruct 会生成 `UserMapperImpl` 类，实现 `toUserBO` 方法，并在源对象和目标对象之间进行映射。开发者只需调用接口方法，便可完成对象的转换。当然，上面的 Demo 在实际的生产环境中稍显吃力，因此下面我们会使用一个更加灵活的方式进行展开。

## MapStruct 快速上手

### 引入依赖

在 [`pom.xml`](https://github.com/YunaiV/SpringBoot-Labs/blob/master/lab-55/lab-55-mapstruct-demo/pom.xml) 文件中引入 MapStruct 相关依赖，对应的依赖版本维护在了 boot-dependencies 的 [pom.xml](https://github.com/javgocn/javgo-springboot/blob/release/boot-dependencies/pom.xml) 中，感兴趣的可以登录仓库查看源码。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>cn.javgo</groupId>
        <artifactId>javgo-springboot</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>

    <artifactId>boot-tool-mapstruct</artifactId>
    <packaging>jar</packaging>

    <name>boot-tool-mapstruct</name>
    <description>SpringBoot 集成 MapStruct 实现对象转换</description>
    <url>https://github.com/javgocn/javgo-springboot</url>

    <properties>
        <lombok.version>1.18.32</lombok.version>
    </properties>

    <dependencies>
        <!-- Lombok 依赖: 这里手动指定一下最新版本的 Lombok 依赖, 否则会报错找不到 Getter And Setter -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
        </dependency>

        <!-- Apache Commons Collections -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-collections4</artifactId>
        </dependency>

        <!-- SpringBoot Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- SpringBoot Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot Maven 插件 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <!-- Maven 编译插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <!-- 添加注解处理器 -->
                    <annotationProcessorPaths>
                        <!-- Lombok -->
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </path>
                        <!-- Lombok MapStruct Binding -->
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                        </path>
                        <!-- 声明 mapstruct-processor 为 JSR 269 的 Java 注解处理器 -->
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                        </path>
                    </annotationProcessorPaths>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

核心配置要点解析：

1. **Lombok 依赖**：Lombok 是一个 Java 库，通过注解形式简化 Java 开发中的常见样板代码（如 getter、setter 和构造器等）。在 MapStruct 的配置中，Lombok 提供了便捷的方式定义数据传输对象。
2. **MapStruct 依赖**：MapStruct 是一个 Java 注解处理器，用于在编译时生成类型安全且高效的对象映射代码。与其他使用反射机制的对象映射框架不同，MapStruct 使用纯 Java 方法进行对象属性的映射，具有更高的执行性能。
3. **Maven 编译插件配置**：为了让 MapStruct 和 Lombok 能够在编译时生效，需要在 Maven 编译插件中添加注解处理器配置。配置 `maven-compiler-plugin` 插件，用于编译代码，并指定注解处理器路径。其中，`lombok-mapstruct-binding` 是 Lombok 与 MapStruct 之间的桥接库，确保 Lombok 自动生成的属性能够被 MapStruct 正确识别和映射。

### 创建 DO（Data Object）

在实际生产环境中，**Data Object (DO)** 主要用于与数据库进行交互，通常直接映射数据库表结构。每一个 DO 对象通常对应数据库中的一张表，包含了表中的所有字段。DO 对象在数据持久层传输和操作中使用，提供了一种面向对象的方式来管理和操作数据库中的数据。

> TIP：DO 使用场景
>
> 1. **数据库操作**：DO 对象是 DAO（数据访问对象）层与数据库交互的载体，用于 CRUD（创建、读取、更新、删除）操作。
> 2. **持久化管理**：DO 对象封装了数据的持久化逻辑，提供了一种面向对象的方式来管理数据。
> 3. **数据传输**：在数据访问层和其他层（如服务层）之间传输数据。

创建 `UserDO` 类，代表用户的 Data Object：

```java
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
```

### 创建 BO

**Business Object (BO)** 代表业务逻辑中的一个实体对象，通常由服务层生成或使用。BO 对象用来封装业务逻辑和操作，是业务逻辑层与其他层之间的数据交换载体。与 DO 不同，BO 通常不直接对应数据库表，而是通过服务层处理后的业务对象。

> TIP：BO 使用场景
>
> 1. **业务逻辑处理**：BO 对象用于封装业务逻辑，帮助在服务层处理和管理数据。
> 2. **数据传输**：BO 对象在业务层与其他层之间传输数据，提供数据处理后的结果。
> 3. **业务操作**：BO 对象可以包含与业务相关的方法和属性，用于支持业务操作和流程。

创建 `UserBO` 类，代表用户的 Business Object：

```java
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
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;
}
```

### 创建 BaseConvert

**BaseConvert** 是一个通用的映射接口，提供了基本的对象转换方法。之所以这么做主要是想通过抽象化对象转换逻辑，提高代码的复用性和可维护性。在实际开发中，业务对象之间的转换需求非常常见，而这些转换逻辑往往是重复且繁琐的。通过定义一个通用的 BaseConvert 接口，可以统一管理和实现这些转换逻辑。

**BaseConvert** 的核心代码如下：

```java
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
```

BaseConvert 核心要点：

1. **统一转换接口**：通过泛型定义了通用的 `convertTo` 和 `convertFrom` 方法，适用于不同类型的对象转换。这个接口实现了从源对象到目标对象以及从目标对象到源对象的双向转换。
2. **列表和流的支持**：提供了 `convertToList` 和 `convertFromList` 方法，实现了列表数据的批量转换。同时，还支持流数据的转换，通过 `convertToStream` 和 `convertFromStream` 方法，可以处理大量数据的转换。
3. **转换后的处理**：定义了 `afterConvertTo` 和 `afterConvertFrom` 方法，允许开发者在对象转换后进行额外的处理。这些方法可以被覆盖，以实现特定的业务逻辑。
4. **映射配置继承**：使用 `@InheritConfiguration` 和 `@InheritInverseConfiguration` 注解，使得子类可以继承父类的映射配置，减少重复代码。

### 创建 Convert

**UserConvert** 接口是 `BaseConvert` 的一个具体实现，用于用户对象（`UserDO` 和 `UserBO`）之间的转换。通过 `@Mapper` 注解，MapStruct 自动生成接口的实现类，简化了转换代码的编写。

**UserConvert** 的核心代码如下：

```java
package cn.javgo.boot.tool.mapstruct.convert;

import cn.javgo.boot.tool.mapstruct.BO.UserBO;
import cn.javgo.boot.tool.mapstruct.DO.UserDO;
import cn.javgo.boot.tool.mapstruct.handler.BaseConvert;
import org.mapstruct.Mapper;

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
}
```

UserConvert 核心要点：

1. **接口继承**：`UserConvert` 接口继承了 `BaseConvert`，实现了 `UserDO` 到 `UserBO` 的转换。通过这种方式，`UserConvert` 可以复用 `BaseConvert` 中定义的通用转换逻辑。
2. **自定义逻辑**：在 `afterConvertTo` 方法中，实现了从 `UserDO` 到 `UserBO` 的自定义逻辑，特别是时间字段的格式化处理。这种方法允许开发者根据实际业务需求添加转换逻辑，提高灵活性。
3. **注解声明**：通过 `@Mapper` 注解，声明这是一个 MapStruct 转换器接口，并指定 `componentModel` 为 `spring`，表示它将被 Spring 容器管理，方便在 Spring 应用中进行依赖注入和管理。
4. **日期格式处理**：为了适应不同业务需求，时间字段的格式化通常是转换过程中的一个重点。这里使用 `DateTimeFormatter` 格式化 `createTime` 和 `updateTime` 字段，确保目标对象中的日期格式统一且符合预期。

### 测试转换

下面我们将通过一个简单的测试类来验证 `UserDO` 和 `UserBO` 之间的转换。代码如下：

```java
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
```

执行上述测试类，预期结果如下：

```bash
UserDO(id=1, username=javgo, password=123456, createTime=2024-06-09T21:37:27.299562, updateTime=2024-06-09T21:37:27.299575)

UserBO(id=1, username=javgo, password=123456, createTime=2024-06-09 21:37:27, updateTime=2024-06-09 21:37:27)
```

从测试结果中可以看到，`UserDO` 对象中的数据被正确地映射到 `UserBO` 对象中，并且日期格式按照我们在 `UserConvert` 中定义的格式进行了转换。

在编译过程中，MapStruct 会自动为我们生成实现类。在 `target/generated-sources/annotations` 目录下，可以找到自动生成的 `UserConvertImpl` 类。

```java
package cn.javgo.boot.tool.mapstruct.convert;

import cn.javgo.boot.tool.mapstruct.BO.UserBO;
import cn.javgo.boot.tool.mapstruct.DO.UserDO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-09T22:33:45+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
@Component
public class UserConvertImpl implements UserConvert {

    @Override
    public UserBO convertTo(UserDO source) {
        if ( source == null ) {
            return null;
        }

        UserBO userBO = new UserBO();

        userBO.setId( source.getId() );
        userBO.setUsername( source.getUsername() );
        userBO.setPassword( source.getPassword() );
        if ( source.getCreateTime() != null ) {
            userBO.setCreateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( source.getCreateTime() ) );
        }
        if ( source.getUpdateTime() != null ) {
            userBO.setUpdateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( source.getUpdateTime() ) );
        }

        handleAfterConvertTo( source, userBO );

        return userBO;
    }

    @Override
    public UserDO convertFrom(UserBO target) {
        if ( target == null ) {
            return null;
        }

        UserDO userDO = new UserDO();

        userDO.setId( target.getId() );
        userDO.setUsername( target.getUsername() );
        userDO.setPassword( target.getPassword() );
        if ( target.getCreateTime() != null ) {
            userDO.setCreateTime( LocalDateTime.parse( target.getCreateTime() ) );
        }
        if ( target.getUpdateTime() != null ) {
            userDO.setUpdateTime( LocalDateTime.parse( target.getUpdateTime() ) );
        }

        return userDO;
    }

    @Override
    public Stream<UserBO> convertToStream(Stream<UserDO> sourceStream) {
        if ( sourceStream == null ) {
            return null;
        }

        return sourceStream.map( userDO -> convertTo( userDO ) );
    }

    @Override
    public Stream<UserDO> convertFromStream(Stream<UserBO> targetStream) {
        if ( targetStream == null ) {
            return null;
        }

        return targetStream.map( userBO -> convertFrom( userBO ) );
    }
}
```

## @Mapping

在对象转换过程中，源对象和目标对象之间的属性名称可能不完全一致。此时，我们可以利用 MapStruct 提供的 `@Mapping` 注解来配置具体的映射关系，从而实现灵活的属性映射。

假设我们有两个对象，`UserDO` 和 `UserBO`，其中 `UserDO` 对象的属性 `id` 需要映射到 `UserBO` 对象的 `userId` 属性上。为了实现这一映射关系，我们可以使用 `@Mapping` 注解进行配置。

对应调整代码如下：

```java
// UserBO.java

/**
 * 主键
 */
// private Integer id;
private Integer userId;
```

为了将 `UserDO` 的 `id` 属性映射到 `UserBO` 的 `userId` 属性，我们在 `UserConvert` 接口中使用 `@Mapping` 注解。在下面的代码中，`@Mapping` 注解的 `source` 参数指定了源对象的属性名称，`target` 参数指定了目标对象的属性名称。通过这种配置，我们可以将 `UserDO` 的 `id` 属性映射到 `UserBO` 的 `userId` 属性。

```java
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

    // ......

    @Mappings(
            @Mapping(source = "id", target = "userId") // 将 id 属性映射到 userId 属性
    )
    @Override
    UserBO convertTo(UserDO source);
}
```

测试结果如下，符合预期：

```bash
UserDO(id=1, username=javgo, password=123456, createTime=2024-06-09T23:06:08.525996, updateTime=2024-06-09T23:06:08.526009)

UserBO(userId=1, username=javgo, password=123456, createTime=2024-06-09 23:06:08, updateTime=2024-06-09 23:06:08)
```

从结果中可以看到，`UserBO` 中的 `userId` 属性被成功映射为 `UserDO` 中的 `id` 属性，验证了 `@Mapping` 注解的正确性。

## IDEA MapStruct 插件

MapStruct 还提供了 [IDEA MapStruct Support](https://plugins.jetbrains.com/plugin/10036-mapstruct-support/) 插件，让我们在 IDEA 中可以更愉快的使用 MapStruct：

![](https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-144752.png)

提供的具体功能，可以阅读官方文档[《MapStruct support for IntelliJ IDEA》](https://mapstruct.org/news/2017-09-19-announcing-mapstruct-idea/)，下面是一些示例：

属性和枚举常量的补全：

![](https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-070122.gif)

转到来自注解的声明：

![](https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-070152.gif)

发现调用处：

<img src="https://javgo-images.oss-cn-beijing.aliyuncs.com/2024-06-09-070207.png" style="zoom: 60%;" />

<hr/>

参考资料：

* https://www.iocoder.cn/Spring-Boot/MapStruct
* https://mapstruct.org/
* https://projectlombok.org/