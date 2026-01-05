/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.biz.service.business;

import com.taotao.cloud.goods.biz.model.vo.StoreGoodsLabelVO;
import com.taotao.cloud.goods.biz.model.entity.StoreGoodsLabel;
import com.taotao.boot.webagg.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * 店铺商品分类业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:05
 */
public interface StoreGoodsLabelService extends BaseSuperService<StoreGoodsLabel, Long> {

	/**
	 * 根据商家ID获取店铺分类列表
	 *
	 * @param storeId 商家ID
	 * @return 店铺分类列表
	 */
	List<StoreGoodsLabelVO> listByStoreId(String storeId);

	/**
	 * 根据分类id集合获取所有店铺分类根据层级排序
	 *
	 * @param ids 商家ID
	 * @return 店铺分类列表
	 */
	List<StoreGoodsLabel> listByStoreIds(List<String> ids);

	/**
	 * 根据分类id集合获取所有店铺分类根据层级排序
	 *
	 * @param ids 商家ID
	 * @return 店铺分类列表
	 */
	List<Map<String, Object>> listMapsByStoreIds(List<String> ids, String columns);

	/**
	 * 添加商品分类
	 *
	 * @param storeGoodsLabel 店铺商品分类
	 * @return 店铺商品分类
	 */
	StoreGoodsLabel addStoreGoodsLabel(StoreGoodsLabel storeGoodsLabel);

	/**
	 * 修改商品分类
	 *
	 * @param storeGoodsLabel 店铺商品分类
	 * @return 店铺商品分类
	 */
	StoreGoodsLabel editStoreGoodsLabel(StoreGoodsLabel storeGoodsLabel);

	/**
	 * 删除商品分类
	 *
	 * @param storeLabelId 店铺 分类 ID
	 */
	void removeStoreGoodsLabel(String storeLabelId);
}
