package com.taotao.cloud.goods.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品搜索结果实体
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsGoods {

	private String skuId;

	private String goodsId;

	private String goodsName;


}
