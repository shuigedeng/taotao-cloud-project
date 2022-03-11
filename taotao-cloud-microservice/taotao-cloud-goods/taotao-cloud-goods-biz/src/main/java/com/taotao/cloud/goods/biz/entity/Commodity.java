package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;


/**
 * 小程序直播商品
 * 
 * @since 2021/5/17 9:34 上午
 *
 */
@Entity
@Table(name = Commodity.TABLE_NAME)
@TableName(Commodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Commodity.TABLE_NAME, comment = "小程序直播商品")
public class Commodity extends BaseSuperEntity<Commodity, Long> {

	public static final String TABLE_NAME = "li_commodity";

    @ApiModelProperty(value = "图片")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsImage;

    @ApiModelProperty(value = "商品名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String name;

    /**
     * 1：一口价（只需要传入price，price2不传）
     * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
     * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
     */
    @ApiModelProperty(value = "价格类型")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer priceType;

    @ApiModelProperty(value = "价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double price;

    @ApiModelProperty(value = "价格2")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double price2;

    @ApiModelProperty(value = "商品详情页的小程序路径")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String url;

    @ApiModelProperty(value = "微信程序直播商品ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer liveGoodsId;

    @ApiModelProperty(value = "审核单ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String auditId;

    @ApiModelProperty(value = "审核状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String auditStatus;

    @ApiModelProperty(value = "店铺ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;

    @ApiModelProperty(value = "商品ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsId;

    @ApiModelProperty(value = "规格ID")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String skuId;

}
