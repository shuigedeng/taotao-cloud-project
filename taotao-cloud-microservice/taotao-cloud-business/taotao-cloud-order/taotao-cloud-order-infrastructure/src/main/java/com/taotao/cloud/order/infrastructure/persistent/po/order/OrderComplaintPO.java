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
import com.taotao.cloud.order.api.enums.aftersale.ComplaintStatusEnum;
import com.taotao.boot.webagg.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
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
 * 订单交易投诉表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:27
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderComplaintPO.TABLE_NAME)
@TableName(OrderComplaintPO.TABLE_NAME)
@org.springframework.data.relational.core.mapping.Table(name = OrderComplaintPO.TABLE_NAME, comment = "订单交易投诉表")
public class OrderComplaintPO extends BaseSuperEntity<OrderComplaintPO, Long> {

    public static final String TABLE_NAME = "tt_order_complaint";

    @Serial
    private static final long serialVersionUID = 7185050229757228184L;

    /** 投诉主题 */
    @Column(name = "complain_topic", columnDefinition = "varchar(255) not null comment '投诉主题'")
    private String complainTopic;
    /** 投诉内容 */
    @Column(name = "content", columnDefinition = "varchar(255) not null comment '投诉内容'")
    private String content;
    /** 投诉凭证图片 */
    @Column(name = "images", columnDefinition = "varchar(255) not null comment '投诉凭证图片'")
    private String images;

    /**
     * 交易投诉状态
     *
     * @see ComplaintStatusEnum
     */
    @Column(name = "complain_status", columnDefinition = "varchar(255) not null comment '交易投诉状态'")
    private String complainStatus;
    /** 申诉商家内容 */
    @Column(name = "appeal_content", columnDefinition = "varchar(255) not null comment '申诉商家内容'")
    private String appealContent;
    /** 申诉商家时间 */
    @Column(name = "appeal_time", columnDefinition = "varchar(255) not null comment '申诉商家时间'")
    private LocalDateTime appealTime;
    /** 申诉商家上传的图片 */
    @Column(name = "appealImages", columnDefinition = "varchar(255) not null comment '申诉商家上传的图片'")
    private String appealImages;
    /** 订单号 */
    @Column(name = "order_sn", columnDefinition = "varchar(255) not null comment '订单号'")
    private String orderSn;
    /** 下单时间 */
    @Column(name = "order_time", columnDefinition = "varchar(255) not null comment '下单时间'")
    private LocalDateTime orderTime;
    /** 商品名称 */
    @Column(name = "goods_name", columnDefinition = "varchar(255) not null comment '商品名称'")
    private String goodsName;
    /** 商品id */
    @Column(name = "goods_id", columnDefinition = "varchar(255) not null comment '商品id'")
    private Long goodsId;
    /** sku主键 */
    @Column(name = "sku_id", columnDefinition = "varchar(255) not null comment 'sku主键'")
    private Long skuId;
    /** 商品价格 */
    @Column(name = "goods_price", columnDefinition = "varchar(255) not null comment '商品价格'")
    private BigDecimal goodsPrice;
    /** 商品图片 */
    @Column(name = "goods_image", columnDefinition = "varchar(255) not null comment '商品图片'")
    private String goodsImage;
    /** 购买的商品数量 */
    @Column(name = "num", columnDefinition = "varchar(255) not null comment '购买的商品数量'")
    private Integer num;
    /** 运费 */
    @Column(name = "freight_price", columnDefinition = "varchar(255) not null comment '运费'")
    private BigDecimal freightPrice;
    /** 订单金额 */
    @Column(name = "order_price", columnDefinition = "varchar(255) not null comment '订单金额'")
    private BigDecimal orderPrice;
    /** 物流单号 */
    @Column(name = "logistics_no", columnDefinition = "varchar(255) not null comment '物流单号'")
    private String logisticsNo;
    /** 商家id */
    @Column(name = "store_id", columnDefinition = "varchar(255) not null comment '商家id'")
    private Long storeId;
    /** 商家名称 */
    @Column(name = "store_name", columnDefinition = "varchar(255) not null comment '商家名称'")
    private String storeName;
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "varchar(255) not null comment '会员id'")
    private Long memberId;
    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(255) not null comment '会员名称'")
    private String memberName;
    /** 收货人 */
    @Column(name = "consignee_name", columnDefinition = "varchar(255) not null comment '收货人'")
    private String consigneeName;
    /** 收货地址 */
    @Column(name = "consignee_address_path", columnDefinition = "varchar(255) not null comment '收货地址'")
    private String consigneeAddressPath;
    /** 收货人手机 */
    @Column(name = "consignee_mobile", columnDefinition = "varchar(255) not null comment '收货人手机'")
    private String consigneeMobile;
    /** 仲裁结果 */
    @Column(name = "arbitration_result", columnDefinition = "varchar(255) not null comment '仲裁结果'")
    private String arbitrationResult;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        OrderComplaintPO that = (OrderComplaintPO) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
