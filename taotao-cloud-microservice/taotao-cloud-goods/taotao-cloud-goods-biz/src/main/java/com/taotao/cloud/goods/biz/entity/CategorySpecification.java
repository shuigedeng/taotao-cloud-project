package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类参数组关联
 *
 * 
 * @since 2020-02-26 10:34:02
 */
@Entity
@Table(name = CategorySpecification.TABLE_NAME)
@TableName(CategorySpecification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategorySpecification.TABLE_NAME, comment = "商品分类规格")
public class CategorySpecification extends BaseSuperEntity<CategorySpecification, Long> {

	public static final String TABLE_NAME = "li_category_specification";

    /**
     * 分类id
     */
    @TableField(value = "category_id")
    @ApiModelProperty(value = "分类id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")

    private String categoryId;
    /**
     * 规格id
     */
    @TableField(value = "specification_id")
    @ApiModelProperty(value = "规格id")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String specificationId;
}
