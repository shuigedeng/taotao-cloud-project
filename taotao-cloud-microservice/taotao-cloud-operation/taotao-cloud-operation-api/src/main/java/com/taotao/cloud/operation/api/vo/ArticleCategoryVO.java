package com.taotao.cloud.operation.api.vo;

import cn.lili.common.utils.BeanUtil;
import cn.lili.modules.page.entity.dos.ArticleCategory;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 文章分类VO
 *
 */
@Data
public class ArticleCategoryVO extends ArticleCategory {

    @Schema(description =  "子菜单")
    private List<ArticleCategoryVO> children = new ArrayList<>();

    public ArticleCategoryVO() {

    }

    public ArticleCategoryVO(ArticleCategory articleCategory) {
        BeanUtil.copyProperties(articleCategory, this);
    }

    public List<ArticleCategoryVO> getChildren() {
        if (children != null) {
            children.sort(new Comparator<ArticleCategoryVO>() {
                @Override
                public int compare(ArticleCategoryVO o1, ArticleCategoryVO o2) {
                    return o1.getSort().compareTo(o2.getSort());
                }
            });
            return children;
        }
        return null;
    }
}
