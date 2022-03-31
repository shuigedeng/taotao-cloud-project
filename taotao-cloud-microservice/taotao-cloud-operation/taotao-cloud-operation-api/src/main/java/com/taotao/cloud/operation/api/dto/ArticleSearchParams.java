package com.taotao.cloud.operation.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品查询条件
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchParams extends PageVO {

    @Schema(description =  "分类ID")
    private String categoryId;

    @Schema(description =  "标题")
    private String title;

    @Schema(description =  "分类类型")
    private String type;

    public <T> QueryWrapper<T> queryWrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(categoryId), "category_id", categoryId);
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        return queryWrapper;
    }
}
