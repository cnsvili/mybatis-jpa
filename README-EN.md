# mybatis-jpa

[![Mybatis](https://img.shields.io/badge/mybatis-3.4.x-brightgreen.svg)](https://maven-badges.herokuapp.com/maven-central/org.mybatis/mybatis)
[![JDK 1.7](https://img.shields.io/badge/JDK-1.7-green.svg)]()
[![maven central](https://img.shields.io/badge/version-2.1.0-brightgreen.svg)](http://search.maven.org/#artifactdetails%7Ccom.github.cnsvili%7Cmybatis-jpa%7C2.1.0%7C)
[![APACHE 2 License](https://img.shields.io/badge/license-Apache2-blue.svg?style=flat)](LICENSE)

:book: English Documentation | [:book: 中文文档](README.md)

The plugins for mybatis, in order to provider the ability to handler jpa.

## maven

```xml
        <dependency>
            <groupId>com.littlenb</groupId>
            <artifactId>mybatis-jpa</artifactId>
            <version>2.0.1</version>
        </dependency>
```

## Plugin boom

+ ResultTypePlugin [![plugin](https://img.shields.io/badge/plugin-resolved-green.svg)]()

### ResultTypePlugin

对于常规的结果映射,不需要再构建ResultMap,ResultTypePlugin增加了Mybatis对结果映射(JavaBean/POJO)中JPA注解的处理。

映射规则：

+ 名称匹配默认为驼峰(Java Field)与下划线(SQL Column)

+ 使用@Column注解中name属性指定SQL Column

类型处理:

+ Boolean-->BooleanTypeHandler

+ Enum默认为EnumTypeHandler

+ 使用@Enumerated(EnumType.ORDINAL) 指定为 EnumOrdinalTypeHandler

结果集嵌套:

+ 支持OneToOne
+ 支持OneToMany

e.g.

mybatis.xml

```xml
<configuration>
    <plugins>
		<plugin interceptor="com.mybatis.jpa.plugin.ResultTypePlugin">
		</plugin>
	</plugins>
</configuration>
```

JavaBean

```JAVA
@Entity
public class UserArchive {// <resultMap id="xxx" type="userArchive">

    @Id
    private Long userId;// <id property="id" column="user_id" />
                           
    /** 默认驼峰与下划线转换 */
    private String userName;// <result property="username" column="user_name"/>

    /** 枚举类型 */
    @Enumerated(EnumType.ORDINAL)
    private SexEnum sex;// <result property="sex" column="sex" typeHandler=EnumOrdinalTypeHandler/>

    /** 属性名与列名不一致 */
    @Column(name = "gmt_create")
    private Date createTime;// <result property="createTime" column="gmt_create"/>
}// </resultMap>
```

mapper.xml

```xml
<!-- in xml,declare the resultType -->
<select id="selectById" resultType="userArchive">
	SELECT * FROM t_sys_user_archive WHERE user_id = #{userId}
</select>
```

Please view test package where has more examples.