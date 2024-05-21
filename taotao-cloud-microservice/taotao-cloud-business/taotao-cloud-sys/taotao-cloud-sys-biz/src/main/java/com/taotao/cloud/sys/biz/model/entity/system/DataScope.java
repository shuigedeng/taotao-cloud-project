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

package com.taotao.cloud.sys.biz.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.data.mybatis.mybatisplus.handler.typehandler.JacksonListTypeHandler;
import com.taotao.cloud.data.mybatis.mybatisplus.interceptor.datascope.dataPermission.enums.DataScopeEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

/**
 * 数据权限表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DataScope.TABLE_NAME)
@TableName(value = DataScope.TABLE_NAME, autoResultMap = true)
// @org.hibernate.annotations.Table(appliesTo = DataScope.TABLE_NAME, comment = "数据权限表")
public class DataScope extends BaseSuperEntity<DataScope, Long> {

    public static final String TABLE_NAME = "tt_data_scope";

    /** 部门范围限制的字段名称 */
    private String scopeDeptFiledName = "dept_id";
    /** 公司范围限制的字段名称 */
    private String scopeOrgFiledName = "org_id";
    /** 个人范围限制的字段名称 */
    private String scopeSelfFiledName = "create_by";

    /** 编码 */
    @Column(name = "code", unique = true, columnDefinition = "varchar(255) not null comment '编码'")
    private String code;

    /** 名称 */
    @Column(name = "名称", unique = true, columnDefinition = "varchar(255) not null comment '名称'")
    private String name;

    /**
     * 数据范围类型
     *
     * @see DataScopeEnum
     */
    @Column(name = "type", columnDefinition = "int not null comment '数据范围类型'")
    private Integer type;

    /** 备注 */
    @Column(name = "备注", columnDefinition = "varchar(1024) null comment '备注'")
    private String remark;

    /** 组织id列表 */
    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonListTypeHandler.class)
    @Column(name = "org_ids", columnDefinition = "json null comment '组织id列表'")
    private List<Long> orgIds;

    /** 部门id */
    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonListTypeHandler.class)
    @Column(name = "dept_ids", columnDefinition = "json null comment '部门id列表'")
    private List<Long> deptIds;

    /** 用户id */
    @Type(value = JsonType.class)
    @TableField(typeHandler = JacksonListTypeHandler.class)
    @Column(name = "user_ids", columnDefinition = "json null comment '用户id列表'")
    private List<Long> userIds;

    /** 租户id */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DataScope dept = (DataScope) o;
        return getId() != null && Objects.equals(getId(), dept.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
