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
import com.taotao.cloud.promotion.api.feign.fallback.FeignKanjiaActivityApiFallback;
import com.taotao.cloud.promotion.api.model.dto.KanjiaActivityGoodsDTO;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * IFeignKanjiaActivityService
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-07 22:09
 */
@HttpExchange(value = ServiceNameConstants.TAOTAO_CLOUD_PROMOTION, fallbackFactory = FeignKanjiaActivityApiFallback.class)
public interface IFeignKanjiaActivityGoodsApi {

    @GetMapping(value = "/updateById")
    void updateById(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO);

    @GetMapping(value = "/getKanjiaGoodsDetail")
    KanjiaActivityGoodsDTO getKanjiaGoodsDetail(Long kanjiaActivityGoodsId);
}
