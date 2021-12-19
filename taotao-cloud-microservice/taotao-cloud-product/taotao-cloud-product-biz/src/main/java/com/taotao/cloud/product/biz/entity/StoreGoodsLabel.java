package com.taotao.cloud.product.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 店铺商品分类
 *
 * 
 * @since 2020-02-18 15:18:56
 */
@Entity
@Table(name = StoreGoodsLabel.TABLE_NAME)
@TableName(StoreGoodsLabel.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreGoodsLabel.TABLE_NAME, comment = "店铺商品分类")
public class StoreGoodsLabel extends BaseSuperEntity<StoreGoodsLabel, Long> {

	public static final String TABLE_NAME = "li_store_goods_label";

    @ApiModelProperty("店铺ID")
    private String storeId;

    @NotEmpty(message = "店铺商品分类名称不能为空")
    @Length(max = 20,message = "店铺商品分类名称太长")
    @ApiModelProperty("店铺商品分类名称")
    private String labelName;


    @NotNull(message = "店铺商品分类排序不能为空")
    @Max(value = 99999,message = "排序值太大")
    @ApiModelProperty("店铺商品分类排序")
    private BigDecimal sortOrder;

    @NotEmpty(message = "父节点不能为空，需设定根节点或者某节点的子节点")
    @ApiModelProperty(value = "父id, 根节点为0")
    private String parentId;

    @ApiModelProperty(value = "层级, 从0开始")
    private Integer level;


}
