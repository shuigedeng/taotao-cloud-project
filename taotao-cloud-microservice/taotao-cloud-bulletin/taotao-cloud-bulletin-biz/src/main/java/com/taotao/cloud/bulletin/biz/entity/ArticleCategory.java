package com.taotao.cloud.bulletin.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 文章分类
 *
 * 
 * 
 * @since 2020/12/10 17:42
 */
@Data
@TableName("li_article_category")
@ApiModel(value = "文章分类")
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @Schema(description =  "分类名称")
    @NotEmpty(message = "分类名称不能为空")
    private String articleCategoryName;

    @Schema(description =  "父分类ID")
    private String parentId;

    @Schema(description =  "排序")
    @Min(value = 0,message = "排序值最小0，最大9999999999")
    @Max(value = 999999999,message = "排序值最小0，最大9999999999")
    @NotNull(message = "排序值不能为空")
    private Integer sort;

    @Schema(description =  "层级")
    @Min(value = 0,message = "层级最小为0")
    @Max(value = 3,message = "层级最大为3")
    private Integer level;

    /**
     * @see ArticleCategoryEnum
     */
    @Schema(description =  "类型")
    private String type;

    public Integer getSort() {
        if (sort == null) {
            return 0;
        }
        return sort;
    }

    public Integer getLevel() {
        if (level == null) {
            return 1;
        }
        return level;
    }
}
