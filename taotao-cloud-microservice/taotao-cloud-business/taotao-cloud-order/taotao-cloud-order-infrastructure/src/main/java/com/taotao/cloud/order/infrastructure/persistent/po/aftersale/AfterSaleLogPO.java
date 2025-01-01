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

package com.taotao.cloud.order.infrastructure.persistent.po.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
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
 * 售后日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:00:49
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = AfterSaleLogPO.TABLE_NAME)
@TableName(AfterSaleLogPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = AfterSaleLog.TABLE_NAME, comment = "售后日志")
public class AfterSaleLogPO extends BaseSuperEntity<AfterSaleLogPO, Long> {

    public static final String TABLE_NAME = "tt_after_sale_log";

    /** 售后服务单号 */
    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '售后服务单号'")
    private String sn;

    /** 操作者id(可以是卖家) */
    @Column(name = "operator_id", columnDefinition = "varchar(64) not null comment '操作者id(可以是卖家)'")
    private Long operatorId;

    /**
     * 操作者类型
     *
     * @see UserEnum
     */
    @Column(name = "operator_type", columnDefinition = "varchar(64) not null comment '操作者类型'")
    private String operatorType;

    /** 操作者名称 */
    @Column(name = "operator_name", columnDefinition = "varchar(64) not null comment '操作者名称'")
    private String operatorName;

    /** 日志信息 */
    @Column(name = "message", columnDefinition = "varchar(1024) not null comment '日志信息'")
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AfterSaleLogPO that = (AfterSaleLogPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
