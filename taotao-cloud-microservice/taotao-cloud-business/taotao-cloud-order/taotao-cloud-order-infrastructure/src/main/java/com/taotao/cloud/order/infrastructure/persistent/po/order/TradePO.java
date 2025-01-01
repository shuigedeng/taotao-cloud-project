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

package com.taotao.cloud.order.infrastructure.persistent.po.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.order.sys.model.dto.cart.TradeDTO;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
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
 * 交易表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:09
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TradePO.TABLE_NAME)
@TableName(TradePO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = TradePO.TABLE_NAME, comment = "交易表")
public class TradePO extends BaseSuperEntity<TradePO, Long> {

    public static final String TABLE_NAME = "tt_trade";

    @Serial
    private static final long serialVersionUID = 5177608752643561827L;

    /** 交易编号 */
    @Column(name = "sn", columnDefinition = "varchar(64) not null comment '交易编号'")
    private String sn;
    /** 买家id */
    @Column(name = "member_id", columnDefinition = "varchar(64) not null comment '买家id'")
    private String memberId;
    /** 买家用户名 */
    @Column(name = "member_name", columnDefinition = "varchar(64) not null comment '买家用户名'")
    private String memberName;
    /** 支付方式 */
    @Column(name = "payment_method", columnDefinition = "varchar(64) not null comment '支付方式'")
    private String paymentMethod;

    /**
     * 付款状态
     *
     * @see PayStatusEnum
     */
    @Column(name = "pay_status", columnDefinition = "varchar(64) not null comment '会员ID'")
    private String payStatus;
    /** 总价格 */
    @Column(name = "flow_price", columnDefinition = "varchar(64) not null comment '总价格'")
    private BigDecimal flowPrice;
    /** 原价 */
    @Column(name = "goods_price", columnDefinition = "varchar(64) not null comment '原价'")
    private BigDecimal goodsPrice;
    /** 运费 */
    @Column(name = "freight_price", columnDefinition = "varchar(64) not null comment '运费'")
    private BigDecimal freightPrice;
    /** 优惠的金额 */
    @Column(name = "discount_price", columnDefinition = "varchar(64) not null comment '优惠的金额'")
    private BigDecimal discountPrice;

    /**
     * 配送方式
     *
     * @see DeliveryMethodEnum
     */
    @Column(name = "delivery_method", columnDefinition = "varchar(64) not null comment '配送方式'")
    private String deliveryMethod;
    /** 收货人姓名 */
    @Column(name = "consignee_name", columnDefinition = "varchar(64) not null comment '收货人姓名'")
    private String consigneeName;
    /** 收件人手机 */
    @Column(name = "consignee_mobile", columnDefinition = "varchar(64) not null comment '收件人手机'")
    private String consigneeMobile;
    /** 地址名称 逗号分割 */
    @Column(name = "consignee_address_path", columnDefinition = "varchar(64) not null comment '地址名称 逗号分割'")
    private String consigneeAddressPath;
    /** 地址id逗号分割 */
    @Column(name = "consignee_address_id_path", columnDefinition = "varchar(64) not null comment '地址id逗号分割'")
    private String consigneeAddressIdPath;

    public TradePO(TradeDTO tradeDTO) {
        Long originId = this.getId();
        if (tradeDTO.getMemberAddress() != null) {
            BeanUtils.copyProperties(tradeDTO.getMemberAddress(), this);
            this.setConsigneeMobile(tradeDTO.getMemberAddress().getMobile());
            this.setConsigneeName(tradeDTO.getMemberAddress().getName());
        }
        BeanUtils.copyProperties(tradeDTO, this);
        BeanUtils.copyProperties(tradeDTO.getPriceDetailDTO(), this);
        this.setId(originId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        TradePO tradePO = (TradePO) o;
        return getId() != null && Objects.equals(getId(), tradePO.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
