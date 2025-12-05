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

package com.taotao.cloud.stock.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.cloud.stock.api.feign.fallback.FeignProductFallback;
import com.taotao.cloud.stock.api.model.dto.ProductDTO;
import com.taotao.cloud.stock.api.model.vo.ProductVO;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用订单模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@HttpExchange(
        contextId = "RemoteProductService",
        value = ServiceNameConstants.TAOTAO_CLOUD_GOODS,
        fallbackFactory = FeignProductFallback.class)
public interface IFeignProductApi {

    /**
     * 根据id查询商品信息
     *
     * @param id id
     * @return com.taotao.boot.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
     * @author shuigedeng
     * @since 2020/11/20 下午3:23
     * @version 2022.03
     */
    @GetMapping(value = "/product/info/id/{id:[0-9]*}")
    ProductVO findProductInfoById(@PathVariable("id") Long id);

    /**
     * 添加商品信息
     *
     * @param productDTO productDTO
     * @return com.taotao.boot.core.model.Result<com.taotao.cloud.product.api.vo.ProductVO>
     * @author shuigedeng
     * @since 2020/11/20 下午3:23
     * @version 2022.03
     */
    @PostMapping(value = "/product")
    ProductVO saveProduct(@RequestBody ProductDTO productDTO);
}
