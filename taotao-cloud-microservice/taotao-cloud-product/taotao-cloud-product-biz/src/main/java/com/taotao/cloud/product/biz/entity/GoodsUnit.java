package com.taotao.cloud.product.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 商品计量单位
 *
 * 
 * @since 2020/11/26 16:08
 */
@Entity
@Table(name = GoodsUnit.TABLE_NAME)
@TableName(GoodsUnit.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsUnit.TABLE_NAME, comment = "商品计量单位")
public class GoodsUnit extends BaseSuperEntity<GoodsUnit, Long> {

	public static final String TABLE_NAME = "li_goods_unit";

    @NotEmpty(message = "计量单位名称不能为空")
    @Size(max = 5, message = "计量单位长度最大为5")
    @ApiModelProperty(value = "计量单位名称")
    private String name;
}
