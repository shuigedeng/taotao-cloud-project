package com.taotao.cloud.product.biz.entity;

import cn.lili.mybatis.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 分类参数组关联
 *
 * 
 * @since 2020-02-26 10:34:02
 */
@Entity
@Table(name = CategoryParameterGroup.TABLE_NAME)
@TableName(CategoryParameterGroup.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategoryParameterGroup.TABLE_NAME, comment = "分类绑定参数组")
public class CategoryParameterGroup extends BaseSuperEntity<CategoryParameterGroup, Long> {

	public static final String TABLE_NAME = "li_category_parameter_group";

    /**
     * 参数组名称
     */
    @ApiModelProperty(value = "参数组名称", required = true)
    @NotEmpty(message = "参数组名称不能为空")
    @Length(max = 20, message = "参数组名称不能超过20字")
    private String groupName;
    /**
     * 关联分类id
     */
    @ApiModelProperty(value = "关联分类id", required = true)
    @NotNull(message = "关联的分类不能为空")
    private String categoryId;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", hidden = true)
    private Integer sort;

}
