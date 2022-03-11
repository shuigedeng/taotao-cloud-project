package com.taotao.cloud.goods.biz.entity;

import cn.lili.modules.goods.entity.enums.GoodsAuthEnum;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.util.Date;

/**
 * 商品sku
 *
 * 
 * @since 2020-02-23 9:14:33
 */
@Entity
@Table(name = GoodsSku.TABLE_NAME)
@TableName(GoodsSku.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsSku.TABLE_NAME, comment = "商品sku对象")
public class GoodsSku extends BaseSuperEntity<GoodsSku, Long> {

	public static final String TABLE_NAME = "li_goods_sku";

    @ApiModelProperty(value = "商品id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsId;

    @ApiModelProperty(value = "规格信息json", hidden = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @JsonIgnore
    private String specs;

    @ApiModelProperty(value = "规格信息")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String simpleSpecs;

    @ApiModelProperty(value = "配送模版id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String freightTemplateId;

    @ApiModelProperty(value = "是否是促销商品")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean isPromotion;

    @ApiModelProperty(value = "促销价")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double promotionPrice;

    @ApiModelProperty(value = "商品名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsName;

    @Length(max = 30, message = "商品规格编号太长，不能超过30个字符")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    @ApiModelProperty(value = "商品编号")
    private String sn;

    @ApiModelProperty(value = "品牌id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String brandId;

    @ApiModelProperty(value = "分类path")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String categoryPath;

    @ApiModelProperty(value = "计量单位")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsUnit;

    @ApiModelProperty(value = "卖点")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String sellingPoint;

    @ApiModelProperty(value = "重量")
    @Max(value = 99999999, message = "重量不能超过99999999")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double weight;
    /**
     * @see GoodsStatusEnum
     */
    @ApiModelProperty(value = "上架状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String marketEnable;

    @ApiModelProperty(value = "商品详情")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String intro;

    @Max(value = 99999999, message = "价格不能超过99999999")
    @ApiModelProperty(value = "商品价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double price;

    @Max(value = 99999999, message = "成本价格99999999")
    @ApiModelProperty(value = "成本价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double cost;

    @ApiModelProperty(value = "浏览数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer viewCount;

    @ApiModelProperty(value = "购买数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer buyCount;

    @Max(value = 99999999, message = "库存不能超过99999999")
    @ApiModelProperty(value = "库存")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer quantity;

    @ApiModelProperty(value = "商品好评率")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double grade;

    @ApiModelProperty(value = "缩略图路径")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String thumbnail;

    @ApiModelProperty(value = "大图路径")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String big;

    @ApiModelProperty(value = "小图路径")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String small;

    @ApiModelProperty(value = "原图路径")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String original;

    @ApiModelProperty(value = "店铺分类id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeCategoryPath;

    @ApiModelProperty(value = "评论数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer commentNum;

    @ApiModelProperty(value = "卖家id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeId;

    @ApiModelProperty(value = "卖家名字")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String storeName;

    @ApiModelProperty(value = "运费模板id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String templateId;

    /**
     * @see GoodsAuthEnum
     */
    @ApiModelProperty(value = "审核状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String isAuth;

    @ApiModelProperty(value = "审核信息")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String authMessage;

    @ApiModelProperty(value = "下架原因")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String underMessage;

    @ApiModelProperty(value = "是否自营")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean selfOperated;

    @ApiModelProperty(value = "商品移动端详情")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String mobileIntro;

    @ApiModelProperty(value = "商品视频")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsVideo;

    @ApiModelProperty(value = "是否为推荐商品", required = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean recommend;

    @ApiModelProperty(value = "销售模式", required = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String salesModel;
    /**
     * @see cn.lili.modules.goods.entity.enums.GoodsTypeEnum
     */
    @ApiModelProperty(value = "商品类型", required = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsType;

    public Double getWeight() {
        if (weight == null) {
            return 0d;
        }
        return weight;
    }

    @Override
    public Date getUpdateTime() {
        if (super.getUpdateTime() == null) {
            return new Date(1593571928);
        } else {
            return super.getUpdateTime();
        }
    }
}
