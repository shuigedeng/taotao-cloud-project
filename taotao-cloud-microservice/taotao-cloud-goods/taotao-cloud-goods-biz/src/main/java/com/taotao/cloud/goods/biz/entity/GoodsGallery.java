package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;

/**
 * 商品相册
 *
 * 
 * @since 2020-02-23 9:14:33
 */
@Entity
@Table(name = GoodsGallery.TABLE_NAME)
@TableName(GoodsGallery.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsGallery.TABLE_NAME, comment = "商品相册")
public class GoodsGallery extends BaseSuperEntity<GoodsGallery, Long> {

	public static final String TABLE_NAME = "li_goods_gallery";


    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;

    /**
     * 商品主键
     */
    @ApiModelProperty(value = "商品id")
    private String goodsId;

    /**
     * 缩略图路径
     */
    @ApiModelProperty(value = "缩略图路径")
    private String thumbnail;

    /**
     * 小图路径
     */
    @ApiModelProperty(value = "小图路径")
    private String small;

    /**
     * 原图路径
     */
    @ApiModelProperty(value = "原图路径", required = true)
    private String original;

    /**
     * 是否是默认图片1   0没有默认
     */
    @ApiModelProperty(value = "是否是默认图片1   0没有默认")
    private Integer isDefault;

    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", required = true)
    private Integer sort;

}
