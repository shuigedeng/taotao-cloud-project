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

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 权限资源表(url请求)
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = RequestPath.TABLE_NAME)
@TableName(RequestPath.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = RequestPath.TABLE_NAME, comment = "权限资源表(url请求)")
public class RequestPath extends BaseSuperEntity<RequestPath, Long> {

    public static final String TABLE_NAME = "tt_request_path";

    /** 权限标识 (controller类#方法#请求方式) ManagerUserController#page#post */
    @Column(name = "code", unique = true, columnDefinition = "varchar(255) not null comment '权限标识'")
    private String code;

    /** 权限名称 (获取用户分页详情) */
    @Column(name = "name", unique = true, columnDefinition = "varchar(255) not null comment '权限名称'")
    private String name;

    /** 分组名称 (用户管理) */
    @Column(name = "group_name", columnDefinition = "varchar(255) not null comment '分组名称'")
    private String groupName;

    /** 请求类型 (post) */
    @Column(name = "request_type", columnDefinition = "varchar(255) not null comment '请求类型'")
    private String requestType;

    /** 请求路径 (/api/sys/manager/user) */
    @Column(name = "path", columnDefinition = "varchar(1024) not null comment '请求路径'")
    private String path;

    /** 启用鉴权 */
    @Column(name = "enable", columnDefinition = "boolean not null default true comment '启用鉴权'")
    private boolean enable;

    /** 是否通过系统生成的权限 */
    @Column(name = "generate", columnDefinition = "boolean not null default false comment '是否通过系统生成的权限'")
    private boolean generate;

    /** 描述 */
    @Column(name = "remark", columnDefinition = "varchar(1024) null comment '描述'")
    private String remark;

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
        RequestPath position = (RequestPath) o;
        return getId() != null && Objects.equals(getId(), position.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
