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
 * 岗位表
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
@Table(name = Position.TABLE_NAME)
@TableName(Position.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Position.TABLE_NAME, comment = "岗位表")
public class Position extends BaseSuperEntity<Position, Long> {

    public static final String TABLE_NAME = "tt_position";

    /** 岗位名称 */
    @Column(name = "name", columnDefinition = "varchar(32) not null comment '岗位名称'")
    private String name;

    /** 部门id */
    @Column(name = "dept_id", columnDefinition = "bigint not null comment '部门id'")
    private Long deptId;

    /** 公司id */
    @Column(name = "org_id", columnDefinition = "bigint not null comment '组织id'")
    private Long orgId;

    /** 备注 */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /** 排序值 */
    @Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
    private Integer sortNum;

    /** 租户id */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;

    @Builder
    public Position(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String name,
            Long deptId,
            String remark,
            Integer sortNum,
            String tenantId) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.name = name;
        this.deptId = deptId;
        this.remark = remark;
        this.sortNum = sortNum;
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Position position = (Position) o;
        return getId() != null && Objects.equals(getId(), position.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
