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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 钱包变动日志表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:43:36
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberWalletLog.TABLE_NAME)
@TableName(MemberWalletLog.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberWalletLog.TABLE_NAME, comment = "钱包变动日志表")
public class MemberWalletLog extends BaseSuperEntity<MemberWalletLog, Long> {

    public static final String TABLE_NAME = "tt_wallet_log";
    /** 会员id */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
    private Long memberId;

    /** 会员id */
    @Column(name = "member_name", columnDefinition = "bigint not null comment '会员id'")
    private String memberName;

    /** 金额 */
    @Column(name = "money", columnDefinition = "decimal(10,2) not null comment '金额'")
    private BigDecimal money;

    /**
     * 业务类型
     *
     * @see DepositServiceTypeEnum
     */
    @Column(name = "service_type", columnDefinition = "varchar(32) not null comment '业务类型'")
    private String serviceType;

    /** 日志明细 */
    @Column(name = "detail", columnDefinition = "varchar(32) not null comment '日志明细'")
    private String detail;

    /**
     * 构建新的预存款日志对象
     *
     * @param memberName 会员名称
     * @param memberWalletUpdateDTO 变动模型
     */
    public MemberWalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO) {
        this.setMemberId(memberWalletUpdateDTO.getMemberId());
        this.setMemberName(memberName);
        this.setMoney(memberWalletUpdateDTO.getMoney());
        this.setDetail(memberWalletUpdateDTO.getDetail());
        this.setServiceType(memberWalletUpdateDTO.getServiceType());
    }

    /**
     * 构建新的预存款日志对象
     *
     * @param memberName 会员名称
     * @param memberWalletUpdateDTO 变动模型
     * @param isReduce 是否是消费
     */
    public MemberWalletLog(String memberName, MemberWalletUpdateDTO memberWalletUpdateDTO, boolean isReduce) {
        this.setMemberId(memberWalletUpdateDTO.getMemberId());
        this.setMemberName(memberName);
        this.setMoney(isReduce ? memberWalletUpdateDTO.getMoney().negate() : memberWalletUpdateDTO.getMoney());
        this.setDetail(memberWalletUpdateDTO.getDetail());
        this.setServiceType(memberWalletUpdateDTO.getServiceType());
    }
}
