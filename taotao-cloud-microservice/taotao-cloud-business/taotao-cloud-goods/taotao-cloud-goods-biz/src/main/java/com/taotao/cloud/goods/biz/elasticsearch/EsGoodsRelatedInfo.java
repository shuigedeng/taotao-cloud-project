package com.taotao.cloud.goods.biz.elasticsearch;

import java.util.List;
import lombok.Data;

/**
 * 搜索相关商品品牌名称，分类名称及属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:18:14
 */
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
