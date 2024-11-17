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
 * 字典子项表 // @SQLDelete(sql = "update tt_dict_item set del_flag = 1 where id = ?") // @Where(clause
 * ="del_flag = 1")
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:09:21
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = DictItemPO.TABLE_NAME)
@TableName(DictItemPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = DictItemPO.TABLE_NAME)
public class DictItemPO extends BaseSuperEntity<DictItemPO, Long> {

    public static final String TABLE_NAME = "tt_dict_item";

    /**
     * 字典id
     *
     * @see DictPO
     */
    @Column(name = "dict_id", columnDefinition = "bigint not null comment '字典id'")
    private Long dictId;

    /** 字典项文本 */
    @Column(name = "item_text", columnDefinition = "varchar(2000) not null comment '字典项文本'")
    private String itemText;

    /** 字典项值 */
    @Column(name = "item_value", columnDefinition = "varchar(2000) not null comment '字典项文本'")
    private String itemValue;

    /** 描述 */
    @Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
    private String description;

    /** 状态 0不启用 1启用 */
    @Column(name = "status", columnDefinition = "int NOT NULL DEFAULT 1 comment ' 状态 0不启用 1启用'")
    private Integer status;

    /** 排序值 */
    @Column(name = "sort_num", columnDefinition = "int not null default 1 comment '排序值'")
    private Integer sortNum;

    @Builder
    public DictItemPO(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            Long dictId,
            String itemText,
            String itemValue,
            String description,
            Integer status,
            Integer sortNum) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.dictId = dictId;
        this.itemText = itemText;
        this.itemValue = itemValue;
        this.description = description;
        this.status = status;
        this.sortNum = sortNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        DictItemPO dictItemPO = (DictItemPO) o;
        return getId() != null && Objects.equals(getId(), dictItemPO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
