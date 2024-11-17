/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.infrastructure.persistent.dict.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.web.base.entity.BaseSuperEntity;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * @Entity：
 * 注解表示该类是一个实体类，在项目启动时会根据该类自动生成一张表，表的名称即@Entity注解中name的值，如果不配置name，默认表名为类名
 * 指定实体名称（表名）：
 * 没有指定name属性且没有使用@Table，命名为类名生成
 * 指定name属性且没有使用@Table，命名为name属性value值
 * 指定name属性且使用了@Table指定name，命名以@Table的name的value
 *
 * @Table：
 * 注解用来标识实体类与数据表的对应关系。
 * name：表示该实体类映射的表名。
 * catalog：指定数据库名称，默认为当前连接url配置的数据库。
 * schema：指定数据库的用户名 ，默认为当前配置的用户。
 * uniqueConstraints：用于批量设置唯一约束。
 *
 * @Id：
 * 所有的实体类都要有的主键，@Id注解表示该属性是一个主键
 *
 * @GeneratedValue：
 * 注解表示主键自动生成，strategy则表示主键的生成策略
 * JPA自带的几种主键生成策略：
 * TABLE：使用一个特定的数据库表格来保存主键
 * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。这个值要与generator一起使用，generator指定生成主键的生成器
 * IDENTITY：主键由数据库自动生成（主要支持自动增长的数据库，如mysql）
 * AUTO：主键由程序控制，也是GenerationType的默认值，mysql不支持，会报错：test.hibernate_sequence不存在
 *
 * @GenericGenerator：
 * 自定义主键生成策略，由@GenericGenerator实现。
 * name属性指定生成器名称。 与@GeneratorValue中 generator 的值对应。
 * strategy属性指定具体生成器的类名。
 * parameters得到strategy指定的具体生成器所用到的参数
 *
 * Hibernate主键生成策略和各自的具体生成器之间的关系，在IdentifierGeneratorFactory接口中已经定义了。由DefaultIdentifierGeneratorFactory工厂去实现的
 * 几种常见的主键策略生成器：
 * native：对于 oracle 采用 Sequence 方式；对于 MySQL 和 SQL Server 采用 Identity 方式。native就是将主键的生成工作交由数据库完成，hibernate不管。
 * uuid：采用128位的uuid算法生成主键，uuid被编码为一个32位16进制数字的字符串，占用空间大。
 * guid：采用数据库底层的guid算法机制，对应MYSQL的uuid()函数，SQL Server的newid()函数，ORACLE的rawtohex(sys_guid())函数等。
 * assigned: 在插入数据的时候主键由程序处理，等同于JPA中的AUTO，这是@GenericGenerator的默认设置。
 *
 * @Column：
 * @Column注解来标识实体类中属性与数据表中字段的对应关系。
 * 查看@Column源码发现@Column注解共有10个属性，且均为可选属性。
 * @Target({ElementType.METHOD, ElementType.FIELD})
 * @Retention(RetentionPolicy.RUNTIME)
 * public @interface Column {
 *     //name属性定义了被标注字段在数据库表中所对应字段的名称。
 *     String name() default "";
 *
 *     //unique属性表示该字段是否为唯一标识，默认为false.
 *     boolean unique() default false;
 *
 *     //nullable属性表示该字段是否可以为null值，默认为true。
 *     boolean nullable() default true;
 *
 *     //insertable属性表示在使用“INSERT”脚本插入数据时，是否需要插入该字段的值。
 *     //insertable属性一般多用于只读的属性，例如主键和外键等。这些字段的值通常是自动生成的。
 *     boolean insertable() default true;
 *
 *     //updatable属性表示在使用“UPDATE”脚本插入数据时，是否需要更新该字段的值。
 *     //updatable属性一般多用于只读的属性，例如主键和外键等。这些字段的值通常是自动生成的。
 *     boolean updatable() default true;
 *
 *     //columnDefinition属性表示创建表时，该字段创建的SQL语句
 *     String columnDefinition() default "";
 *
 *     //table属性定义了包含当前字段的表名。
 *     String table() default "";
 *
 *     //length属性表示字段的长度，当字段的类型为varchar时，该属性才有效。
 *     int length() default 255;
 *
 *     //precision属性和scale属性表示精度，当字段类型为double时，precision表示数值的总长度，scale表示小数点所占的位数。
 *     int precision() default 0;
 *     int scale() default 0;
 * }
 *
 * @Transient：注解表示在生成数据库的表时，该属性被忽略，即不生成对应的字段
 *
 * 字典表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DictPO.TABLE_NAME)
@TableName(DictPO.TABLE_NAME)
@EntityListeners({DictPO.DictEntityListener.class})
//@NamedQuery(name = "User.findByEmailAddress",
//	query = "select u from User u where u.emailAddress = ?1")
@org.springframework.data.relational.core.mapping.Table(name = DictPO.TABLE_NAME)
public class DictPO extends BaseSuperEntity<DictPO, Long> {

    public static final String TABLE_NAME = "tt_dict";

    /** 字典名称 */
    @Column(name = "dict_name", columnDefinition = "varchar(255) not null  comment '字典名称'")
    private String dictName;

    /** 字典编码 */
    @Column(name = "dict_code", unique = true, columnDefinition = "varchar(255) not null comment '字典编码'")
    private String dictCode;

    /** 描述 */
    @Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
    private String description;

    /** 排序值 */
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum;

    /** 备注信息 */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注信息'")
    private String remark;

    @Builder
    public DictPO(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String dictName,
            String dictCode,
            String description,
            Integer sortNum,
            String remark) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.dictName = dictName;
        this.dictCode = dictCode;
        this.description = description;
        this.sortNum = sortNum;
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DictPO dictPO = (DictPO) o;
        return getId() != null && Objects.equals(getId(), dictPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static class DictEntityListener {

        /**
         * 在新实体持久化之前（添加到EntityManager）
		 * EntityManager 保存记录前回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PrePersist
        public void prePersist(Object object) {
            LogUtils.info(" DictEntityListener prePersis: {}", object);
        }

        /**
         * 在数据库中存储新实体（在commit或期间flush）
		 * EntityManager 保存记录后被回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PostPersist
        public void postPersist(Object object) {
            LogUtils.info("DictEntityListener postPersist: {}", object);
        }

        /**
         * 从数据库中检索实体后。
		 * 实体加载到 Entity 后回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:55
         */
        @PostLoad
        public void postLoad(Object object) {
            LogUtils.info("DictEntityListener postLoad: {}", object);
        }

        /**
         * 当一个实体被识别为被修改时EntityManager
		 * 数据库记录修改前回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PreUpdate
        public void preUpdate(Object object) {
            LogUtils.info("DictEntityListener preUpdate: {}", object);
        }

        /**
         * 更新数据库中的实体（在commit或期间flush）
		 * 数据库记录修改后回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PostUpdate
        public void postUpdate(Object object) {
            LogUtils.info("DictEntityListener postUpdate: {}", object);
        }

        /**
         * 在EntityManager中标记要删除的实体时
		 * EntityManager 删除记录前被回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PreRemove
        public void preRemove(Object object) {
            LogUtils.info("DictEntityListener preRemove: {}", object);
        }

        /**
         * 从数据库中删除实体（在commit或期间flush）
		 * EntityManager 删除记录后回调方法
         *
         * @param object 对象
         * @since 2022-10-21 11:59:55
         */
        @PostRemove
        public void postRemove(Object object) {
            LogUtils.info("DictEntityListener postRemove: {}", object);
        }

        /**
         * 前摧毁
         *
         * @param object 对象
         * @since 2022-10-21 11:59:54
         */
        @PreDestroy
        public void preDestroy(Object object) {
            LogUtils.info("DictEntityListener preDestroy: {}", object);
        }
    }
}
