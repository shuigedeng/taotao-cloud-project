package com.taotao.cloud.product.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品信息表
 *
 * @author dengtao
 * @date 2020/4/30 16:03
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_product")
@org.hibernate.annotations.Table(appliesTo = "tt_product", comment = "商品信息表")
public class Product extends BaseEntity {

    /**
     * 商品名称
     */
    @Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '商品名称'")
    private String name;

    /**
     * 供应商id
     */
    @Column(name = "supplier_id", nullable = false, columnDefinition = "bigint not null comment '供应商id'")
    private Long supplierId;

    /**
     * 图片id
     */
    @Column(name = "pic_id", nullable = false, columnDefinition = "bigint not null comment '图片id'")
    private Long picId;

    /**
     * 视频id
     */
    @Column(name = "video_id", columnDefinition = "bigint default 0 comment '视频id'")
    private Long videoId;

    /**
     * 商品详情图片id
     */
    @Column(name = "detail_pic_id", columnDefinition = "bigint default 0 comment '商品详情图片id'")
    private Long detailPicId;

    /**
     * 商品第一张图片id
     */
    @Column(name = "first_pic_id", columnDefinition = "bigint default 0 comment '商品第一张图片id'")
    private Long firstPicId;

    /**
     * 商品海报id
     */
    @Column(name = "poster_pic_id", columnDefinition = "bigint default 0 comment '商品海报id'")
    private Long posterPicId;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
    private String remark;

    /**
     * 商品状态
     */
    @Builder.Default
    @Column(name = "status", columnDefinition = "int not null default 0 comment '商品状态'")
    private Integer status = 0;
}
