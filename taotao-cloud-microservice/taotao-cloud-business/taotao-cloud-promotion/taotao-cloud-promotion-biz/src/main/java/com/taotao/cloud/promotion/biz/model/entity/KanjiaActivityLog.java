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

package com.taotao.cloud.promotion.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 砍价活动商品实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:50
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = KanjiaActivityLog.TABLE_NAME)
@TableName(KanjiaActivityLog.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = KanjiaActivityLog.TABLE_NAME, comment = "砍价活动日志对象")
public class KanjiaActivityLog extends BaseSuperEntity<KanjiaActivityLog, Long> {

    public static final String TABLE_NAME = "tt_kanjia_activity_log";
    /** 砍价活动参与记录id */
    @Column(name = "kanjia_activity_id", columnDefinition = "bigint not null  comment '砍价活动参与记录id'")
    private Long kanjiaActivityId;
    /** 砍价会员id */
    @Column(name = "kanjia_member_id", columnDefinition = "bigint not null  comment '砍价会员id'")
    private Long kanjiaMemberId;
    /** 砍价会员名称 */
    @Column(name = "kanjia_member_name", columnDefinition = "varchar(255) not null  comment '砍价会员名称'")
    private String kanjiaMemberName;
    /** 砍价会员头像 */
    @Column(name = "kanjia_member_face", columnDefinition = "varchar(255) not null  comment '砍价会员头像'")
    private String kanjiaMemberFace;
    /** 砍价金额 */
    @Column(name = "kanjia_price", columnDefinition = "decimal(10,2) not null  comment '砍价金额'")
    private BigDecimal kanjiaPrice;
    /** 剩余购买金额 */
    @Column(name = "surplus_price", columnDefinition = "decimal(10,2) not null  comment '剩余购买金额'")
    private BigDecimal surplusPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        KanjiaActivityLog kanjiaActivityLog = (KanjiaActivityLog) o;
        return getId() != null && Objects.equals(getId(), kanjiaActivityLog.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
