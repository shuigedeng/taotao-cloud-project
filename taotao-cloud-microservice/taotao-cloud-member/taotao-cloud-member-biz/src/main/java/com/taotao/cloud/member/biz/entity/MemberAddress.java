package com.taotao.cloud.member.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员收货地址
 *
 * @author dengtao
 * @date 2020/6/15 11:00
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_member_address")
@org.hibernate.annotations.Table(appliesTo = "tt_member_address", comment = "会员收货地址")
public class MemberAddress extends BaseEntity {
    /**
     * 会员id
     *
     * @see Member
     */
    @Column(name = "member_user_id", nullable = false, columnDefinition = "int(11) not null comment '会员id'")
    private Long memberUserId;

    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name", nullable = false, columnDefinition = "varchar(32) not null comment '收货人姓名'")
    private String receiverName;

    /**
     * 手机号
     */
    @Column(name = "phone", unique = true, nullable = false, columnDefinition = "varchar(14) not null comment '手机号'")
    private String phone;

    /**
     * 省份
     */
    @Column(name = "province", nullable = false, columnDefinition = "varchar(32) not null COMMENT '省'")
    private String province;

    /**
     * 市
     */
    @Column(name = "city", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市'")
    private String city;

    /**
     * 区县
     */
    @Column(name = "area", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区县'")
    private String area;

    /**
     * 省code
     */
    @Column(name = "province_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '省code'")
    private String provinceCode;

    /**
     * 市code
     */
    @Column(name = "city_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '市code'")
    private String cityCode;

    /**
     * 区、县code
     */
    @Column(name = "area_code", nullable = false, columnDefinition = "varchar(32) not null COMMENT '区、县code'")
    private String areaCode;

    /**
     * 街道地址
     */
    @Column(name = "address", nullable = false, columnDefinition = "varchar(255) not null COMMENT '街道地址'")
    private String address;

    /**
     * 邮政编码
     */
    @Column(name = "postal_code", columnDefinition = "int(11) comment '邮政编码'")
    private Long postalCode;

    /**
     * 默认收货地址 1=>默认 0非默认
     */
    @Column(name = "is_default", nullable = false, columnDefinition = "tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认收货地址'")
    @Builder.Default
    private Boolean isDefault = false;

}
