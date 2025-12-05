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

package com.taotao.cloud.promotion.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.cloud.promotion.api.feign.fallback.FeignPromotionApiFallback;

import java.util.List;
import java.util.Map;

import com.taotao.cloud.promotion.api.model.vo.PromotionGoodsVO;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@HttpExchange(
        contextId = "IFeignPromotionService",
        value = ServiceNameConstants.TAOTAO_CLOUD_PROMOTION,
        fallbackFactory = FeignPromotionApiFallback.class)
public interface IFeignPromotionApi {

    @GetMapping(value = "/withdraw/info/}")
    Map<String, Object> getGoodsSkuPromotionMap(Long storeId, Long goodsIndexId);

    List<PromotionGoodsVO> findSkuValidPromotions(List<String> categories, List<String> skuIds);

	/**
	 * 根据促销商品信息包装促销信息
	 *
	 * @param promotionGoodsList 促销商品信息
	 * @return 促销信息
	 */
	Map<String, Object> wrapperPromotionMapList(List<PromotionGoodsVO> promotionGoodsList);
}
