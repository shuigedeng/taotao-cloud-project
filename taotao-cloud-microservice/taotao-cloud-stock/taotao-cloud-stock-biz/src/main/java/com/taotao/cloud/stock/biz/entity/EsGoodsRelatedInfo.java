package com.taotao.cloud.stock.biz.entity;

import cn.lili.modules.search.entity.dto.ParamOptions;
import cn.lili.modules.search.entity.dto.SelectorOptions;
import java.util.List;
import lombok.Data;

/**
 * 搜索相关商品品牌名称，分类名称及属性
 *
 * 
 * @since 2020/10/20
 **/
@Data
public class EsGoodsRelatedInfo {

    /**
     * 分类集合
     */
    List<SelectorOptions> categories;

    /**
     * 品牌集合
     */
    List<SelectorOptions> brands;

    /**
     * 参数集合
     */
    List<ParamOptions> paramOptions;


}
