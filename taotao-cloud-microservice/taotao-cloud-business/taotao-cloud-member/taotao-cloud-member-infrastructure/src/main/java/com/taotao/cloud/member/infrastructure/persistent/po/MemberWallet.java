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
 * 会员预存款表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:32:26
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberWallet.TABLE_NAME)
@TableName(MemberWallet.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberWallet.TABLE_NAME, comment = "会员预存款表")
public class MemberWallet extends BaseSuperEntity<MemberWallet, Long> {

    public static final String TABLE_NAME = "tt_member_wallet";

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long memberId;

    /** 会员预存款 */
    @Column(name = "member_wallet", columnDefinition = "decimal(10,2) not null default 0 comment '会员预存款'")
    private BigDecimal memberWallet;

    /** 会员预存款冻结金额,提现使用 */
    @Column(
            name = "member_frozen_wallet",
            columnDefinition = "decimal(10,2) not null default 0 comment '会员预存款冻结金额,提现使用'")
    private BigDecimal memberFrozenWallet;

    /** 预存款密码 */
    @Column(name = "wallet_password", columnDefinition = "varchar(32) not null comment '预存款密码'")
    private String walletPassword;
}
