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

package com.taotao.cloud.member.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员预存款充值记录
 *
 * @author pikachu
 * @since 2020-02-25 14:10:16
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberReceipt.TABLE_NAME)
@TableName(MemberReceipt.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberReceipt.TABLE_NAME, comment = "预存款充值记录")
public class MemberRecharge extends BaseSuperEntity<MemberRecharge, Long> {

    public static final String TABLE_NAME = "tt_recharge";

    @Serial
    private static final long serialVersionUID = -1529240544327161096L;
    /** 充值订单编号 */
    @Column(name = "recharge_sn", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String rechargeSn;
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private Long memberId;
    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String memberName;
    /** 充值金额 */
    @Column(name = "recharge_money", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private BigDecimal rechargeMoney;
    /** 充值方式，如：支付宝，微信不能为空 */
    @Column(name = "recharge_way", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String rechargeWay;
    /** 支付状态 */
    @Column(name = "pay_status", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String payStatus;
    /** 支付插件id */
    @Column(name = "payment_plugin_id", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String paymentPluginId;
    /** 第三方流水 */
    @Column(name = "receivable_no", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String receivableNo;
    /** 支付时间 */
    @Column(name = "pay_time", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private LocalDateTime payTime;

    /**
     * 构建充值账单信息
     *
     * @param rechargeSn 充值订单号
     * @param memberId 会员id
     * @param memberName 会员名称
     * @param money 充值金额
     */
    public MemberRecharge(String rechargeSn, Long memberId, String memberName, BigDecimal money) {
        this.rechargeSn = rechargeSn;
        this.memberId = memberId;
        this.memberName = memberName;
        this.rechargeMoney = money;
        this.payStatus = PayStatusEnum.UNPAID.name();
    }
}
