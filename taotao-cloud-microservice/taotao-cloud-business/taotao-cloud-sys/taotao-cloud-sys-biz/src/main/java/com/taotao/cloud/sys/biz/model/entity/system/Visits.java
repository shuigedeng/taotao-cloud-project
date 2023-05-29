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
 * pv 与 ip 统计
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
@Table(name = Visits.TABLE_NAME)
@TableName(Visits.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Visits.TABLE_NAME, comment = "pv与ip统计")
public class Visits extends BaseSuperEntity<Visits, Long> {

    public static final String TABLE_NAME = "tt_visits";

    @Column(name = "date", columnDefinition = "varchar(64) not null comment '日期'")
    private String date;

    @Column(name = "pv_counts", columnDefinition = "bigint not null default 0 comment 'pv'")
    private Long pvCounts;

    @Column(name = "ip_counts", columnDefinition = "bigint not null default 0 comment 'ip'")
    private Long ipCounts;

    @Column(name = "week_day", columnDefinition = "varchar(64) not null comment '天'")
    private String weekDay;

    @Builder
    public Visits(
            Long id,
            LocalDateTime createTime,
            Long createBy,
            LocalDateTime updateTime,
            Long updateBy,
            Integer version,
            Boolean delFlag,
            String date,
            Long pvCounts,
            Long ipCounts,
            String weekDay) {
        super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
        this.date = date;
        this.pvCounts = pvCounts;
        this.ipCounts = ipCounts;
        this.weekDay = weekDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Visits visits = (Visits) o;
        return getId() != null && Objects.equals(getId(), visits.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
