package com.taotao.cloud.operation.api.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文章VO
 *
 */
@Data
public class ArticleVO {

    @Schema(description =  "文章ID")
    private String id;

    @Schema(description =  "文章标题")
    private String title;

    @Schema(description =  "分类名称")
    private String articleCategoryName;

    @Schema(description =  "文章排序")
    private Integer sort;

    @Schema(description =  "开启状态")
    private Boolean openStatus;
}
