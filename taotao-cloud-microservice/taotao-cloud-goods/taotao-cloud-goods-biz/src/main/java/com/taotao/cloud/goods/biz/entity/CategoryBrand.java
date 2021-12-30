package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


/**
 * 分类品牌关联
 *
 * 
 * @since 2020-03-02 09:34:02
 */
@Entity
@Table(name = CategoryBrand.TABLE_NAME)
@TableName(CategoryBrand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategoryBrand.TABLE_NAME, comment = "商品分类品牌关联")
public class CategoryBrand extends BaseSuperEntity<CategoryBrand, Long> {

	public static final String TABLE_NAME = "li_category_brand";

    @CreatedBy
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者", hidden = true)
    private String createBy;

    @CreatedDate
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    @ApiModelProperty(value = "分类id")
    private String categoryId;
    /**
     * 品牌id
     */
    @TableField(value = "brand_id")
    @ApiModelProperty(value = "品牌id")
    private String brandId;

    public CategoryBrand(String categoryId, String brandId) {
        this.categoryId = categoryId;
        this.brandId = brandId;
    }
}
