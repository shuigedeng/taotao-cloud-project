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
import com.taotao.cloud.promotion.api.model.vo.SeckillVO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;

/**
 * 秒杀活动实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:28
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Seckill.TABLE_NAME)
@TableName(Seckill.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = Seckill.TABLE_NAME, comment = "秒杀活动实体类")
public class Seckill extends BasePromotions<Seckill, Long> {

    public static final String TABLE_NAME = "tt_seckill";
    /** 报名截至时间 */
    @Column(name = "apply_end_time", columnDefinition = "datetime not null  comment '报名截至时间'")
    private LocalDateTime applyEndTime;
    /** 申请规则 */
    @Column(name = "seckill_rule", columnDefinition = "varchar(255) not null  comment '申请规则'")
    private String seckillRule;
    /** 开启几点场 例如：6，8，12 */
    @Column(name = "hours", columnDefinition = "varchar(255) not null  comment '开启几点场 例如：6，8，12'")
    private String hours;

    /** 已参与此活动的商家id集合 商家id集合以逗号分隔 */
    @Column(name = "store_ids", columnDefinition = "varchar(1024) not null  comment '已参与此活动的商家id集合 商家id集合以逗号分隔'")
    private String storeIds;
    /** 商品数量 */
    @Column(name = "goods_num", columnDefinition = "int not null  comment '商品数量'")
    private Integer goodsNum;

    public Seckill(int day, String hours, String seckillRule) {
        // 默认创建*天后的秒杀活动
        DateTime dateTime = DateUtil.beginOfDay(DateUtil.offset(new DateTime(), DateField.DAY_OF_YEAR, day));
        this.applyEndTime = dateTime;
        this.hours = hours;
        this.seckillRule = seckillRule;
        this.goodsNum = 0;
        // BasePromotion
        this.setStoreName("platform");
        this.setStoreId("platform");
        this.setPromotionName(DateUtil.formatDate(dateTime) + " 秒杀活动");
        this.setStartTime(dateTime);
        this.setEndTime(DateUtil.endOfDay(dateTime));
    }

    public Seckill(SeckillVO seckillVO) {
        BeanUtils.copyProperties(seckillVO, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Seckill seckill = (Seckill) o;
        return getId() != null && Objects.equals(getId(), seckill.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
