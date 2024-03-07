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

package com.taotao.cloud.generator.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = GenTable.TABLE_NAME)
@TableName(GenTable.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = GenTable.TABLE_NAME, comment = "业务表")
public class GenTable extends BaseSuperEntity<GenTable, Long> {

    public static final String TABLE_NAME = "tt_gen_table";

    /** 表名称 */
    @Column(name = "table_name", columnDefinition = "varchar(64) not null comment '表名称'")
    private String tableName;

    /** 表描述 */
    @Column(name = "table_comment", columnDefinition = "varchar(64) not null comment '表描述'")
    private String tableComment;

    /** 关联父表的表名 */
    @Column(name = "sub_table_name", columnDefinition = "varchar(64) null comment '关联父表的表名'")
    private String subTableName;

    /** 本表关联父表的外键名 */
    @Column(name = "sub_table_fk_name", columnDefinition = "varchar(64) null comment '本表关联父表的外键名'")
    private String subTableFkName;

    /** 实体类名称(首字母大写) */
    @NotBlank(message = "实体类名称不能为空")
    private String className;

    /** 使用的模板（crud单表操作 tree树表操作 sub主子表操作） */
    @Column(
            name = "tpl_category",
            columnDefinition = "varchar(64) not null comment ' 使用的模板（crud单表操作 tree树表操作 sub主子表操作）'")
    private String tplCategory;

    /** 生成包路径 */
    @Column(name = "package_name", columnDefinition = "varchar(1024) not null comment '生成包路径'")
    private String packageName;

    /** 生成模块名 */
    @Column(name = "module_name", columnDefinition = "varchar(1024) not null comment '生成模块名'")
    private String moduleName;

    /** 生成业务名 */
    @Column(name = "business_name", columnDefinition = "varchar(1024) not null comment '生成业务名'")
    private String businessName;

    /** 生成功能名 */
    @Column(name = "function_name", columnDefinition = "varchar(1024) not null comment '生成功能名'")
    private String functionName;

    /** 生成作者 */
    @Column(name = "function_author", columnDefinition = "varchar(1024) not null comment '生成作者'")
    private String functionAuthor;

    /** 生成代码方式（0zip压缩包 1自定义路径） */
    @Column(name = "gen_type", columnDefinition = "varchar(64) not null comment '生成代码方式（0zip压缩包 1自定义路径）'")
    private String genType;

    /** 生成路径（不填默认项目路径） */
    @TableField(updateStrategy = FieldStrategy.NOT_EMPTY)
    @Column(name = "gen_path", columnDefinition = "varchar(1024) not null comment '生成路径（不填默认项目路径）'")
    private String genPath;

    /** 其它生成选项 */
    @Column(name = "options", columnDefinition = "varchar(1024) null comment '其它生成选项'")
    private String options;

    /** 备注 */
    @Column(name = "remark", columnDefinition = "varchar(255) null comment '备注'")
    private String remark;

    /** 主键信息 */
    @Transient
    @TableField(exist = false)
    private GenTableColumn pkColumn;

    /** 子表信息 */
    @Transient
    @TableField(exist = false)
    private GenTable subTable;

    @Transient
    @TableField(exist = false)
    private Map<String, Object> params = new HashMap<>();

    /** 表列信息 */
    @Transient
    @TableField(exist = false)
    private List<GenTableColumn> columns;

    /** 树编码字段 */
    @Transient
    @TableField(exist = false)
    private String treeCode;

    /** 树父编码字段 */
    @Transient
    @TableField(exist = false)
    private String treeParentCode;

    /** 树名称字段 */
    @Transient
    @TableField(exist = false)
    private String treeName;

    /*
     * 菜单id列表
     */
    @Transient
    @TableField(exist = false)
    private List<Long> menuIds;

    /** 上级菜单ID字段 */
    @Transient
    @TableField(exist = false)
    private String parentMenuId;

    /** 上级菜单名称字段 */
    @Transient
    @TableField(exist = false)
    private String parentMenuName;

    public boolean isSub() {
        return isSub(this.tplCategory);
    }

    public static boolean isSub(String tplCategory) {
        return tplCategory != null && org.apache.commons.lang3.StringUtils.equals(GenConstants.TPL_SUB, tplCategory);
    }

    public boolean isTree() {
        return isTree(this.tplCategory);
    }

    public static boolean isTree(String tplCategory) {
        return tplCategory != null && org.apache.commons.lang3.StringUtils.equals(GenConstants.TPL_TREE, tplCategory);
    }

    public boolean isCrud() {
        return isCrud(this.tplCategory);
    }

    public static boolean isCrud(String tplCategory) {
        return tplCategory != null && org.apache.commons.lang3.StringUtils.equals(GenConstants.TPL_CRUD, tplCategory);
    }

    public boolean isSuperColumn(String javaField) {
        return isSuperColumn(this.tplCategory, javaField);
    }

    public static boolean isSuperColumn(String tplCategory, String javaField) {
        if (isTree(tplCategory)) {
            return org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(
                    javaField, ArrayUtils.addAll(GenConstants.TREE_ENTITY, GenConstants.BASE_ENTITY));
        }
        return org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(javaField, GenConstants.BASE_ENTITY);
    }
}
