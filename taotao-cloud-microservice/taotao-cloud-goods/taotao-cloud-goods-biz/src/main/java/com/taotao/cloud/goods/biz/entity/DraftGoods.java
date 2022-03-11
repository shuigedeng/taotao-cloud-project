package com.taotao.cloud.goods.biz.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import cn.lili.modules.goods.entity.enums.DraftGoodsSaveType;
import cn.lili.modules.goods.entity.enums.GoodsStatusEnum;
import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;

/**
 * 草稿商品
 *
 * 
 * @since 2020-02-23 9:14:33
 */
@Entity
@Table(name = DraftGoods.TABLE_NAME)
@TableName(DraftGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = DraftGoods.TABLE_NAME, comment = "草稿商品")
public class DraftGoods extends BaseSuperEntity<DraftGoods, Long> {

	public static final String TABLE_NAME = "li_draft_goods";

    @ApiModelProperty(value = "商品名称")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsName;

    @Max(value = 99999999, message = "价格不能超过99999999")
    @ApiModelProperty(value = "商品价格")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Double price;


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

    /**
     * @see GoodsStatusEnum
     */
    @ApiModelProperty(value = "上架状态")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String marketEnable;

    @ApiModelProperty(value = "详情")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String intro;


    @ApiModelProperty(value = "商品移动端详情")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String mobileIntro;

    @ApiModelProperty(value = "购买数量")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer buyCount;

    @Max(value = 99999999, message = "库存不能超过99999999")
    @ApiModelProperty(value = "库存")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer quantity;

    @ApiModelProperty(value = "可用库存")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer enableQuantity;

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

    @ApiModelProperty(value = "是否自营")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Boolean selfOperated;

    @ApiModelProperty(value = "商品视频")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsVideo;

    @ApiModelProperty(value = "是否为推荐商品")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private boolean recommend;

    @ApiModelProperty(value = "销售模式")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String salesModel;

    /**
     * @see DraftGoodsSaveType
     */
    @ApiModelProperty(value = "草稿商品保存类型")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String saveType;

    @ApiModelProperty(value = "分类名称JSON")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String categoryNameJson;

    @ApiModelProperty(value = "商品参数JSON")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsParamsListJson;

    @ApiModelProperty(value = "商品图片JSON")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsGalleryListJson;

    @ApiModelProperty(value = "sku列表JSON")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String skuListJson;

    /**
     * @see cn.lili.modules.goods.entity.enums.GoodsTypeEnum
     */
    @ApiModelProperty(value = "商品类型", required = true)
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String goodsType;

    public String getIntro() {
        if (CharSequenceUtil.isNotEmpty(intro)) {
            return HtmlUtil.unescape(intro);
        }
        return intro;
    }

    public String getMobileIntro() {
        if (CharSequenceUtil.isNotEmpty(mobileIntro)) {
            return HtmlUtil.unescape(mobileIntro);
        }
        return mobileIntro;
    }

}
