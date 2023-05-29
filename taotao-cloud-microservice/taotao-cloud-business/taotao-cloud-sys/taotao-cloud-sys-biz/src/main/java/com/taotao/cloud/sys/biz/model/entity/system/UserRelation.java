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
import com.taotao.cloud.common.enums.UserObjectEnum;
import com.taotao.cloud.web.base.entity.SuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 用户-角色第三方表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:04:45
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = UserRelation.TABLE_NAME)
@TableName(UserRelation.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = UserRelation.TABLE_NAME, comment = "用户-关系第三方表")
public class UserRelation extends SuperEntity<UserRelation, Long> {

    public static final String TABLE_NAME = "tt_user_relation";

    /** 用户ID */
    @Column(name = "user_id", columnDefinition = "bigint not null comment '用户ID'")
    private Long userId;

    /**
     * 对象类型 org dept position role dataScope
     *
     * @see UserObjectEnum
     */
    @Column(name = "object_type", columnDefinition = "varchar(255) not null comment '对象类型'")
    private String objectType;

    /** 对象id orgId deptId positionId roleId dataScopeId */
    @Column(name = "object_id", columnDefinition = "bigint not null comment '对象id'")
    private Long objectId;

    /** 排序值 */
    @Column(name = "sort_code", columnDefinition = "int null comment '排序值'")
    private Integer sortCode;

    @Builder
    public UserRelation(Long id, Long userId, String objectType, Long objectId, Integer sortCode) {
        super(id);
        this.userId = userId;
        this.objectId = objectId;
        this.sortCode = sortCode;
        this.objectType = objectType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        UserRelation userRole = (UserRelation) o;
        return getId() != null && Objects.equals(getId(), userRole.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
