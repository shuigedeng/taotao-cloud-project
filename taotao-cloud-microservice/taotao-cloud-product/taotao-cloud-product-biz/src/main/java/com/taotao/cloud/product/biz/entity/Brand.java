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

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 商品品牌
 *
 * 
 * @since 2020-02-18 15:18:56
 */
@Entity
@Table(name = Brand.TABLE_NAME)
@TableName(Brand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Brand.TABLE_NAME, comment = "商品品牌")
public class Brand extends BaseSuperEntity<Brand, Long> {

	public static final String TABLE_NAME = "li_brand";

    @NotEmpty(message = "品牌名称不能为空")
    @Length(max = 20, message = "品牌名称应该小于20长度字符")
    @ApiModelProperty(value = "品牌名称", required = true)
    private String name;

    @NotEmpty(message = "品牌图标不能为空")
    @Length(max = 255, message = "品牌图标地址长度超过255字符")
    @ApiModelProperty(value = "品牌图标", required = true)
    private String logo;

}
