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

package com.taotao.cloud.member.infrastructure.persistent.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员提现申请表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:33:20
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberWithdrawApply.TABLE_NAME)
@TableName(MemberWithdrawApply.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberWithdrawApply.TABLE_NAME, comment = "会员提现申请表")
public class MemberWithdrawApply extends BaseSuperEntity<MemberWithdrawApply, Long> {

    public static final String TABLE_NAME = "tt_member_withdraw_apply";

    /** 申请提现金额 */
    @Column(name = "apply_money", columnDefinition = "decimal(10,2) not null default 0 comment '申请提现金额'")
    private BigDecimal applyMoney;

    /** 提现状态 */
    @Column(name = "apply_status", columnDefinition = "varchar(32) not null comment '提现状态'")
    private String applyStatus;

    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
    private Long memberId;

    /** 审核备注 */
    @Column(name = "inspect_remark", columnDefinition = "varchar(32) not null comment '审核备注'")
    private String inspectRemark;

    /** 审核时间 */
    @Column(name = "inspect_time", columnDefinition = "datetime  null comment '审核时间'")
    private LocalDateTime inspectTime;

    /** sn */
    @Column(name = "sn", columnDefinition = "varchar(32) not null comment 'sn'")
    private String sn;
}
