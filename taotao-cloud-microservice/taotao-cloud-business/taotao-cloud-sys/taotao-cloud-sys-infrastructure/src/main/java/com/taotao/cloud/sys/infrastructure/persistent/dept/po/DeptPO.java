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

package com.taotao.cloud.sys.infrastructure.persistent.dept.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.web.base.entity.BaseSuperEntity;
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
 * 部门表
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
@Table(name = DeptPO.TABLE_NAME)
@TableName(DeptPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = DeptPO.TABLE_NAME)
public class DeptPO extends BaseSuperEntity<DeptPO, Long> {

    public static final String TABLE_NAME = "tt_dept";

    /** 部门名称 */
    @Column(name = "name", columnDefinition = "varchar(32) not null comment '部门名称'")
    private String name;

    /** 上级部门id */
    @Column(name = "parent_id", columnDefinition = "bigint not null default 0 comment '上级部门id'")
    private Long parentId;

    /** 公司id */
    @Column(name = "org_id", columnDefinition = "bigint not null comment '组织id'")
    private Long orgId;

    /** 备注 */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /** 备注 */
    @Column(name = "id_tree", columnDefinition = "varchar(4096) comment 'id树，逗号连接'")
    private String idTree;

    /** 当前深度 */
    @Column(name = "depth", columnDefinition = "int not null default 0 comment '当前深度 已1开始'")
    private Integer depth;

    /** 排序值 */
    @Column(name = "sort_num", columnDefinition = "int not null default 0 comment '排序值'")
    private Integer sortNum;

    /** 租户id */
    @Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
    private String tenantId;

	private String path;

    @Builder
    public DeptPO(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String name,
            Long parentId,
            String remark,
            Integer sortNum,
            String tenantId) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.name = name;
        this.parentId = parentId;
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
        DeptPO deptPO = (DeptPO) o;
        return getId() != null && Objects.equals(getId(), deptPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
