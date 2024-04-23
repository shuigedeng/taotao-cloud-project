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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员收货地址表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 14:55:28
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberAddress.TABLE_NAME)
@TableName(MemberAddress.TABLE_NAME)
// @org.hibernate.annotations.Table(appliesTo = MemberAddress.TABLE_NAME, comment = "会员收货地址表")
public class MemberAddress extends BaseSuperEntity<MemberAddress, Long> {

    public static final String TABLE_NAME = "tt_member_address";

    /** 会员ID */
    @Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
    private Long memberId;

    /** 收货人姓名 */
    @Column(name = "name", columnDefinition = "varchar(255) not null comment '收货人姓名'")
    private String name;

    /** 手机号码 */
    @Column(name = "mobile", columnDefinition = "varchar(255) not null comment '手机号码'")
    private String mobile;

    /** 地址名称，逗号分割 */
    @Column(name = "consignee_address_path", columnDefinition = "varchar(255) not null comment '地址名称，逗号分割'")
    private String consigneeAddressPath;

    /** 地址id,逗号分割 */
    @Column(name = "consignee_address_id_path", columnDefinition = "varchar(255) not null comment '地址id,逗号分割'")
    private String consigneeAddressIdPath;

    /** 省 */
    @Column(name = "province", columnDefinition = "varchar(255) not null COMMENT '省'")
    private String province;

    /** 市 */
    @Column(name = "city", columnDefinition = "varchar(255) not null COMMENT '市'")
    private String city;

    /** 区县 */
    @Column(name = "area", columnDefinition = "varchar(255) not null COMMENT '区县'")
    private String area;

    /** 省code */
    @Column(name = "province_code", columnDefinition = "varchar(255) not null COMMENT '省code'")
    private String provinceCode;

    /** 市code */
    @Column(name = "city_code", columnDefinition = "varchar(255) not null COMMENT '市code'")
    private String cityCode;

    /** 区县code */
    @Column(name = "area_code", columnDefinition = "varchar(255) not null COMMENT '区县code'")
    private String areaCode;

    /** 街道地址 */
    @Column(name = "address", columnDefinition = "varchar(255) not null COMMENT '街道地址'")
    private String address;

    /** 详细地址 */
    @Column(name = "detail", columnDefinition = "varchar(255) not null comment '详细地址'")
    private String detail;

    /** 是否为默认收货地址 */
    @Column(name = "defaulted", columnDefinition = "boolean not null default true comment '是否为默认收货地址'")
    private Boolean defaulted;

    /** 地址别名 */
    @Column(name = "alias", columnDefinition = "varchar(255) comment '地址别名'")
    private String alias;

    /** 经度 */
    @Column(name = "lon", columnDefinition = "varchar(32) comment '经度'")
    private String lon;

    /** 纬度 */
    @Column(name = "lat", columnDefinition = "varchar(32) comment '纬度'")
    private String lat;

    /** 邮政编码 */
    @Column(name = "postal_code", columnDefinition = "varchar(255) comment '邮政编码'")
    private String postalCode;
}
