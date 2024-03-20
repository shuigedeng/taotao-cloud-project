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
import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * MemberReceipt
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:28:38
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberReceipt.TABLE_NAME)
@TableName(MemberReceipt.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberReceipt.TABLE_NAME, comment = "会员发票表")
public class MemberReceipt extends BaseSuperEntity<MemberReceipt, Long> {

    public static final String TABLE_NAME = "tt_member_receipt";

    /** 发票抬头 */
    @Column(name = "receipt_title", columnDefinition = "varchar(32) not null comment '发票抬头'")
    private String receiptTitle;

    /** 纳税人识别号 */
    @Column(name = "taxpayer_id", columnDefinition = "varchar(32) not null comment '纳税人识别号'")
    private String taxpayerId;

    /** 发票内容 */
    @Column(name = "receipt_content", columnDefinition = "varchar(32) not null comment '发票内容'")
    private String receiptContent;

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long memberId;

    /** 会员名称 */
    @Column(name = "member_name", columnDefinition = "varchar(32) not null comment '会员名称'")
    private String memberName;

    /**
     * 发票类型
     *
     * @see MemberReceiptEnum
     */
    @Column(name = "receipt_type", columnDefinition = "varchar(32) not null comment '发票类型'")
    private String receiptType;

    /** 是否为默认选项 0：否，1：是 */
    @Column(name = "defaulted", columnDefinition = "int not null default 0 comment '是否为默认选项 0：否，1：是'")
    private Integer defaulted;

    /** 删除标志 true/false 删除/未删除 */
    @Column(name = "delete_flag", columnDefinition = "boolean not null default false comment '删除标志 true/false 删除/未删除'")
    private Boolean deleteFlag;
}
